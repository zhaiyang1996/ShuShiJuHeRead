package com.zhai.shuyangwx.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhai.shuyangwx.MainActivity;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookMixATocLocalBean;
import com.zhai.shuyangwx.bean.ChapterRead;
import com.zhai.shuyangwx.dao.BookDataDaoUtils;
import com.zhai.shuyangwx.dao.BookMixATocLocalBeanDaoUtils;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.utils.IOUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 书籍下载服务
 */
public class DownloadService extends Service {
    //线程池
    private final ExecutorService mSingleExecutor = Executors.newSingleThreadExecutor();
    private List<BookMixATocLocalBean> bookMixATocLocalBeans;
    BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils; //书籍章节数据库操作类
    BookDataDaoUtils bookDataDaoUtils; //书籍内容数据库操作类
    String book; //书籍内容
    String bookName;
    int downloadNum = 0; //当前下载进度
    //通知栏进度条
    private NotificationManager mNotificationManager=null;
    private Notification mNotification;
    private int mNotificationNum = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        bookMixATocLocalBeanDaoUtils = new BookMixATocLocalBeanDaoUtils(this);
        bookDataDaoUtils = new BookDataDaoUtils(this);
    }

    /**
     * 下载列队（根据ID像数据库查询目录）
     * @param bookid
     */
    public void download(String bookid,String bookName){
        this.bookName = bookName;
        bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bookid);
        notificationInit();
        if(bookMixATocLocalBeans!=null&&bookMixATocLocalBeans.size()>0){
            BookMixATocLocalBean bookMixATocLocalBean = bookMixATocLocalBeans.get(downloadNum);
            executeTask(bookMixATocLocalBean);
//            //执行任务
//            for(BookMixATocLocalBean bookMixATocLocalBean:bookMixATocLocalBeans){
//                executeTask(bookMixATocLocalBean);
//            }
//            mSingleExecutor.shutdown();
        }else{
            //目录为空，目前貌似没有写的必要
        }
    }
    /**
     * 判断书架中是否有此书
     */
    public boolean isDownload(String bookid){
        bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bookid);
        boolean b;
        if(bookMixATocLocalBeans!=null&&bookMixATocLocalBeans.size()>0){
            b = true;
        }else {
            b = false;
        }
        return b;
    }
    /**
     * 执行下载
     * @param bookMixATocLocalBean
     */
    public void executeTask(final BookMixATocLocalBean bookMixATocLocalBean){
         Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //判断此章节是否为在线，如果为在线就进行下载，否则跳过
                if(bookMixATocLocalBean.isOnline){
                    DataManager.getInstance().getBookChapter(new ProgressSubscriber<ChapterRead>(new SubscriberOnNextListenerInstance() {
                        @Override
                        public void onNext(Object o) {
                            ChapterRead chapterRead = (ChapterRead) o;
                            if (chapterRead.isOk()){
                                book = "\u3000\u3000"+chapterRead.getChapter().getBody().replace("\n","\n\u3000\u3000");
                                String path = "./sdcard/书阳小说/BOOKTXT/"+bookMixATocLocalBean.bookid+"/"+bookMixATocLocalBean.title+".txt";
                                //将书籍文件写入数据库当中
                                //将目录状态进行更新
                                bookMixATocLocalBean.setIsOnline(false);
                                bookMixATocLocalBeanDaoUtils.updateBookMixATocLocalBean(bookMixATocLocalBean);
                                BookData bookData = new BookData(null,bookMixATocLocalBean.bookid,bookMixATocLocalBean.title,path);
                                //将文件写入内存和数据库保存文件路径
                                bookDataDaoUtils.insertBookData(bookData);
                                IOUtils.setText_SD(DownloadService.this,bookMixATocLocalBean.bookid,bookMixATocLocalBean.title,book);
                                bookMixATocLocalBeanDaoUtils.closeConnection();
                                bookDataDaoUtils.closeConnection();
                                downloadNum++;
                                handler.sendEmptyMessage(0x223);
                            }
                        }
                    }, DownloadService.this, null),bookMixATocLocalBean.link);
                }else {
                    downloadNum++;
                    handler.sendEmptyMessage(0x223);
                }
            }
        };
        new Thread(runnable).start();
    }
    private void notificationInit(){
        mNotificationNum++;
        //通知栏内显示下载进度条
        mNotificationManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        mNotification=new Notification();
        mNotification.icon= R.drawable.ic_launcher;
        mNotification.tickerText="《"+bookName+"》开始下载";
        mNotification.contentView=new RemoteViews(getPackageName(),R.layout.item_download);//通知栏中进度布局
        mNotificationManager.notify(bookMixATocLocalBeans.size(),mNotification);
    }
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
                    double i = ((double) downloadNum/(double)bookMixATocLocalBeans.size());
                    //获取格式化对象
                    NumberFormat nt = NumberFormat.getPercentInstance();
                    //设置百分数精确度2即保留两位小数
                    nt.setMinimumFractionDigits(2);
                    mNotification.contentView.setTextViewText(R.id.content_view_text,"《"+bookName+"》正在下载");
                    mNotification.contentView.setTextViewText(R.id.content_view_text1,nt.format(i));
                    mNotificationManager.notify(bookMixATocLocalBeans.size(), mNotification);
                    break;
                case 0x223:
                    if(downloadNum<bookMixATocLocalBeans.size()){
                        BookMixATocLocalBean bookMixATocLocalBean = bookMixATocLocalBeans.get(downloadNum);
                        executeTask(bookMixATocLocalBean);
                        handler.sendEmptyMessage(0x123);
                    }else{
                        handler.sendEmptyMessage(0x333);
                    }
                    break;
                case 0x333:
                    mNotification.contentView.setTextViewText(R.id.content_view_text,"《"+bookName+"》下载完成");
                    mNotification.contentView.setTextViewText(R.id.content_view_text1,"100%");
                    mNotificationManager.notify(bookMixATocLocalBeans.size(), mNotification);
                    break;
            }
        }
    };
    public class MyBind extends Binder{
        public void downloadService(String bookid,String bookName){
            //开始下载
            download(bookid,bookName);
        }
        public boolean downloadIs(String bookid){
            return isDownload(bookid);
        }
    }
}
