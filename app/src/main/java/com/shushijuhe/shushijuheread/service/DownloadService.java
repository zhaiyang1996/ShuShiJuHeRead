package com.shushijuhe.shushijuheread.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.shushijuhe.shushijuheread.bean.BookData;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.ChapterRead;
import com.shushijuhe.shushijuheread.dao.BookDataDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

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
    int downloadNum = 0; //当前下载进度
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
    public void download(String bookid){
        bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bookid);
        if(bookMixATocLocalBeans!=null&&bookMixATocLocalBeans.size()>0){
            //执行任务
            for(BookMixATocLocalBean bookMixATocLocalBean:bookMixATocLocalBeans){
                executeTask(bookMixATocLocalBean,downloadNum);
            }
        }else{
            //目录为空，目前貌似没有写的必要
        }
    }

    /**
     * 执行下载
     * @param bookMixATocLocalBean
     */
    public void executeTask(final BookMixATocLocalBean bookMixATocLocalBean,int downloadNum){
        final Runnable runnable = new Runnable() {
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
                                //将书籍文件写入数据库当中
                                BookData bookData = new BookData(null,bookMixATocLocalBean.bookid,bookMixATocLocalBean.title,book);
                                //将目录状态进行更新
                                if(bookDataDaoUtils.insertBookData(bookData)){
                                    bookMixATocLocalBean.setIsOnline(false);
                                    bookMixATocLocalBeanDaoUtils.updateBookMixATocLocalBean(bookMixATocLocalBean);
                                }
                            }
                        }
                    }, DownloadService.this, null),bookMixATocLocalBean.link);
                }
            }
        };
        mSingleExecutor.execute(runnable);
        ++downloadNum;
        if(downloadNum>=bookMixATocLocalBeans.size()-1){
            //通知状态栏下载完成

        }
    }
    public class MyBind extends Binder{
        public void downloadService(String bookid){
            //开始下载
            download(bookid);
        }
    }
}
