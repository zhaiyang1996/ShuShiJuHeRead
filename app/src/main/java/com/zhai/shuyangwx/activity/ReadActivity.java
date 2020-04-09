package com.zhai.shuyangwx.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingmouren.colorpicker.ColorPickerDialog;
import com.dingmouren.colorpicker.OnColorPickerListener;
import com.martian.libsliding.SlidingAdapter;
import com.martian.libsliding.SlidingLayout;
import com.martian.libsliding.slider.OverlappedSlider;
import com.martian.libsliding.slider.PageSlider;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.animation.Read_ainmation;
import com.zhai.shuyangwx.application.app;
import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookMixAToc;
import com.zhai.shuyangwx.bean.BookMixATocLocalBean;
import com.zhai.shuyangwx.bean.BookReadHistory;
import com.zhai.shuyangwx.bean.BookshelfBean;
import com.zhai.shuyangwx.bean.ChapterRead;
import com.zhai.shuyangwx.bean.ReadPatternBean;
import com.zhai.shuyangwx.bean.ThecustomBJ;
import com.zhai.shuyangwx.constants.Constants;
import com.zhai.shuyangwx.dao.BookDataDaoUtils;
import com.zhai.shuyangwx.dao.BookMixATocLocalBeanDaoUtils;
import com.zhai.shuyangwx.dao.BookReadHistoryDaoUtils;
import com.zhai.shuyangwx.dao.BookshelfBeanDaoUtils;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.service.DownloadService;
import com.zhai.shuyangwx.utils.GetImagePath;
import com.zhai.shuyangwx.utils.IOUtils;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.paging.TextViewUtils;
import com.zhai.shuyangwx.view.BatteryView;
import com.zhai.shuyangwx.view.ReadingTextView;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


public class ReadActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.sliding_container)
    SlidingLayout slidingContainer;
    @BindView(R.id.btn_up)
    Button btnUp;
    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.read_button)
    Button button;
    @BindView(R.id.book_zitisizex)
    TextView bookZitisizex;
    @BindView(R.id.book_caidanweix)
    LinearLayout bookCaidanweix;
    @BindView(R.id.book_caidan_toux)
    RelativeLayout bookCaidanToux;
    @BindView(R.id.book_quxiaocaidanx)
    Button bookQuxiaocaidanx;
    @BindView(R.id.txthuoqux)
    LinearLayout txthuoqux;
    @BindView(R.id.ziti_toux)
    RelativeLayout zitiToux;
    @BindView(R.id.zi_yangshi_listx)
    ListView ziYangshiListx;
    @BindView(R.id.meiyouzitix)
    TextView meiyouzitix;
    @BindView(R.id.zi_yangshi_layoutx)
    RelativeLayout ziYangshiLayoutx;
    @BindView(R.id.ziti_tou1x)
    RelativeLayout zitiTou1x;
    @BindView(R.id.ziti_shangcheng_listx)
    ListView zitiShangchengListx;
    @BindView(R.id.ziti_shangcheng_meiyoux)
    TextView zitiShangchengMeiyoux;
    @BindView(R.id.ziti_shangchengx)
    RelativeLayout zitiShangchengx;
    @BindView(R.id.qwe)
    RelativeLayout qwe;
    @BindView(R.id.read_book_x)
    ReadingTextView read_book_x;
    @BindView(R.id.huadong_beijing_zhu)
    RelativeLayout ZhuBeiJing;
    @BindView(R.id.read_back)
    Button btn_back;
    @BindView(R.id.readmix)
    Button btn_mix;
    @BindView(R.id.read_download)
    Button download;


    @BindView(R.id.read_zongse)
    Button readZongse;
    @BindView(R.id.read_niupix)
    Button readNiupix;
    @BindView(R.id.read_huoyan)
    Button readHuoyan;
    @BindView(R.id.read_huyanbeijing)
    Button readHuyanbeijing;
    @BindView(R.id.read_lanse)
    Button readLanse;
    @BindView(R.id.read_luse)
    Button readLuse;
    @BindView(R.id.read_yinghua)
    Button readYinghua;
    @BindView(R.id.read_yueliang)
    Button readYueliang;
    @BindView(R.id.read_changguimo)
    Button readChangguimo;
    @BindView(R.id.read_rijianbeijing)
    Button readRijianbeijing;
    @BindView(R.id.read_yejianbeijing)
    Button readYejianbeijing;
    @BindView(R.id.read_text_a)
    Button readTextA;
    @BindView(R.id.read_text_b)
    Button readTextB;
    @BindView(R.id.read_rel_web)
    RelativeLayout read_rel_web;
    @BindView(R.id.read_rel_text_web)
    TextView read_rel_text_web;
    @BindView(R.id.read_thecustom_background)
    Button read_thecustom_background;
    @BindView(R.id.read_thecustom_text)
    Button read_thecustom_text;

    private TestSlidingAdapter myslid;
    private OverlappedSlider myover;
    private BookMixAToc bookMixAToc;
    private List<BookMixATocLocalBean> bookMixATocLocalBean;
    private String book; //书籍详细类容
    private String bookx; //书籍详细类容备份
    private List<String>  bookBodylist = new ArrayList<>();;
    int mixAtoc = 1;//章节
    private boolean mPagerMode = true;
    private ArrayList<Integer>  offsetArrayList = new ArrayList<>();;
    private int statusBarHeight = 0;
    private boolean jianqu = true;
    private boolean bookCurrent = false;
    private int page = 0;//页码
    private boolean no_1 = true; //是否是第一次加载
    private BatteryView mBattery;//电池控件
    private int cell = 100; //电量
    private ReadPatternBean yd;
    private ThecustomBJ thecustomBJ;
    private int ziticr = 0;
    private int buju = 0;
    private int zitidaxiao = 20;//字体大小
    private Drawable drawable;
    private String fontsrc = "1";
    private int fontcor;
    private Typeface typeface;
    private String bookName_str = "获取失败";
    public static String READBOOKMIX = "READBOOKMIX";//书籍目录
    public static String BOOKNAME = "BOOKNAME";//书籍名
    public static String BOOKMIXATOC = "BOOKMIXATOC";//阅读章节
    public static String BOOKPAGE = "BOOKPAGE";//阅读章节页码
    public static String BOOKISONLINE = "BOOKISONLINE"; //是否为在线阅读
    public static String ISMIX = "ISMIX";
    public boolean isOnline = true;
    boolean isRefresh = false; //是否需要刷新
    boolean isMix = false;
    boolean isError = false; //获取书籍是否异常
    private boolean isTTS = true;//TTS是否开启
    private boolean isReadText = false;//自定义选择是否为字体颜色
    BookshelfBeanDaoUtils bookshelfBeanDaoUtils; //书架数据库操作类
    BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils; //书籍章节数据库操作类
    BookReadHistoryDaoUtils bookReadHistoryDaoUtils; //书籍阅读记录数据库操作类
    BookDataDaoUtils bookDataDaoUtils; //书籍内容数据库操作类
    List<BookData> bookData;

    DownloadService.MyBind myBind;
    String bookid;
    String formattedDate = "";
    //定义一个ServiceConnection对象
    private ServiceConnection connn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //当Activity与Service连接成功时回调该方法
            myBind = (DownloadService.MyBind) service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //当Activity与Service断开连接时回调该方法
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    public void initToolBar() {
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initView() {
        // 读取用户保存设置
        yd = Tool.getBuJu(this);
        if (yd != null) {
            ziticr = yd.getFontcr();
            buju = yd.getBuju();
            zitidaxiao = yd.getSize();
            getMoShi();
        }
        //读取用户自定义阅读模式
        thecustomBJ = Tool.getThecustomBJ(this);
        ZhuBeiJing.setBackground(drawable);
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        button.setOnClickListener(this);
        bookQuxiaocaidanx.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_mix.setOnClickListener(this);
        download.setOnClickListener(this);
        //设置阅读背景
        readZongse.setOnClickListener(setPattern);
        readNiupix.setOnClickListener(setPattern);
        readHuoyan.setOnClickListener(setPattern);
        readHuyanbeijing.setOnClickListener(setPattern);
        readLanse.setOnClickListener(setPattern);
        readLuse.setOnClickListener(setPattern);
        readYinghua.setOnClickListener(setPattern);
        readYueliang.setOnClickListener(setPattern);
        readChangguimo.setOnClickListener(setPattern);
        readRijianbeijing.setOnClickListener(setPattern);
        readYejianbeijing.setOnClickListener(setPattern);
        readTextA.setOnClickListener(setPattern);
        readTextB.setOnClickListener(setPattern);

        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        height = display.heightPixels;
        width = display.widthPixels;
        //初始化数据库
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(this);
        bookMixATocLocalBeanDaoUtils = new BookMixATocLocalBeanDaoUtils(this);
        bookReadHistoryDaoUtils = new BookReadHistoryDaoUtils(this);
        bookDataDaoUtils = new BookDataDaoUtils(this);
        //绑定服务
        bindService(new Intent(this,DownloadService.class),connn, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        if(intent!=null){
            bookMixAToc = app.bookMixAToc;
            bookMixATocLocalBean = app.bookMixATocLocalBean;
            mixAtoc = getIntent().getIntExtra(BOOKMIXATOC,0);
            bookName_str = getIntent().getStringExtra(BOOKNAME);
            page = getIntent().getIntExtra(BOOKPAGE,0);
            isMix = getIntent().getBooleanExtra(ISMIX,false);
            //查询历史记录
            if(bookMixAToc!=null){
                bookid = bookMixAToc.mixToc.book;
            }else{
                if(bookMixATocLocalBean!=null){
                    bookid = bookMixATocLocalBean.get(0).bookid;
                }else {
                    toast("书籍发生了错误，重新打开看看！");
                    return;
                }
            }
            //加入书籍历史记录
            if(!isMix){
                List<BookReadHistory> bookshelfBeans = bookReadHistoryDaoUtils.queryBookReadHistoryQueryBuilder(bookid);
                if(bookshelfBeans!=null&&bookshelfBeans.size()>0){
                    mixAtoc = bookshelfBeans.get(0).getMix();
                    page = bookshelfBeans.get(0).getPaga();
                    isRefresh = true;
                }
            }
            setBooksData();
        }
    }

    private void switchSlidingMode() {
        if (mPagerMode) {
            myslid = new TestSlidingAdapter();
            myover = new OverlappedSlider();
            slidingContainer.setAdapter(myslid);
            slidingContainer.setSlider(myover);
        } else {
            slidingContainer.setAdapter(new TestSlidingAdapter());
            slidingContainer.setSlider(new PageSlider());
        }
//        mPagerMode = !mPagerMode;
    }

    /**
     * 设置书籍详细类容
     */
    public void setBooksData() {
        isError = false;
        read_book_x.setTextSize(zitidaxiao);
        bookZitisizex.setText(zitidaxiao + "");
        if (bookBodylist.size() > 0)
            bookBodylist.removeAll(bookBodylist);

        showWaitingDialog("数据加载中...");
        //判断是否为在线或本地以便于加载书籍数据
        boolean isOnline;
        String link;
        String titble;
        if(bookMixAToc!=null){
            if(mixAtoc>=bookMixAToc.mixToc.chapters.size())
                mixAtoc=bookMixAToc.mixToc.chapters.size()-1;
            isOnline = bookMixAToc.mixToc.chapters.get(mixAtoc).isOnline;
            link =  bookMixAToc.mixToc.chapters.get(mixAtoc).link;
            titble = bookMixAToc.mixToc.chapters.get(mixAtoc).title;
        }else{
            if(mixAtoc>=bookMixATocLocalBean.size())
            mixAtoc=bookMixATocLocalBean.size()-1;
            isOnline = bookMixATocLocalBean.get(mixAtoc).isOnline;
            link = bookMixATocLocalBean.get(mixAtoc).link;
            titble = bookMixATocLocalBean.get(mixAtoc).title;
        }
        if(isOnline){
            read_rel_text_web.setText(link);
            //没有离线则获取网络数据
            DataManager.getInstance().getBookChapter(new ProgressSubscriber<ChapterRead>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    ChapterRead chapterRead = (ChapterRead) o;
                    if (chapterRead.isOk()){
                        if(bookMixAToc!=null){
                            bookBodylist.add(bookMixAToc.mixToc.chapters.get(mixAtoc).title);
                        }else{
                            bookBodylist.add(bookMixATocLocalBean.get(mixAtoc).title);
                        }
                        book = "\u3000\u3000"+chapterRead.getChapter().getBody().replace("\n","\n\u3000\u3000");
                        bookx = book;
                        //加载书籍文章
                        read_book_x.setText(book);
                        bookBodylist.add(book);
                        handler.sendEmptyMessage(0x223);
                    }else{
                        isBookData();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    isBookData();
                }
            }, this, null),link);
        }else{
            read_rel_text_web.setText("此章节已离线，不支持网页查看！");
            //若离线则加载本地数据
            bookData = bookDataDaoUtils.queryBookDataDaoByQueryBuilder(bookid,titble);
            if(bookData!=null&&bookData.size()>0&&bookData.size()<2){
                bookBodylist.add(bookData.get(0).getTitle());
                if(IOUtils.fileIsExists(bookData.get(0).getBody())){
                    book = IOUtils.getText(bookData.get(0).getBody());
                }else {
                    book = "离线书籍加载失败啦！原因以及解决方案如下：\n1.资源文件被删除\n2.退出重进刷新（将以在线方式加载文件）\n3.防止后面章节缺失，可打开目录，点击检查资源完整性来判断离线章节文件是否存在！\n4.加入官方QQ群：215636017";
                    BookMixATocLocalBean bean = bookMixATocLocalBean.get(mixAtoc);
                    bean.setIsOnline(true);
                    bookMixATocLocalBeanDaoUtils.updateBookMixATocLocalBean(bean);
                }
                bookx = book;
                //加载书籍文章
                read_book_x.setText(book);
                bookBodylist.add(book);
                handler.sendEmptyMessage(0x223);
            }else{
                isBookData();
            }
        }

    }
    public void isBookData(){
        isError = true;
        page = 0;
        book = "重新打开页面进行刷新，如一直无法刷新原因可能如下：\n1.网络不稳定，检查你的网络。\n2.可能资源文件被加密。\n3.数据缺失\n4.加入官方群反馈：215636017";
        bookx = book;
        //加载书籍文章
        String title = "";
        if(bookMixAToc!=null){
            title = "章节《"+bookMixAToc.mixToc.chapters.get(mixAtoc).title+"》获取失败：\n";
        }else{
            title = "章节《"+bookMixATocLocalBean.get(mixAtoc).title+"》获取失败：\n";
        }
        title = title+book;
        read_book_x.setText(title);
        bookx = title;
        bookBodylist.add(title);
        handler.sendEmptyMessage(0x223);
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disWaitingDialog();
            switch (msg.what) {
                case 0x123:
                    int i =1;
                    for(String p :bookBodylist){
                        if(i<bookBodylist.size()){
                            String s;
                            if(bookBodylist.get(i).length()>15){
                                s = bookBodylist.get(i).substring(0,15);
                            }else{
                                if(bookBodylist.get(i).length()>0){
                                    s = bookBodylist.get(i).substring(0,bookBodylist.get(i).length()-1);
                                }else{
                                    s = "";
                                }
                            }
                            int i1 = p.indexOf(s);
                            if(i1!=-1){
                                s = p.substring(0,i1);
//                                Log.d("Read", "\n所在位置："+i1+"\n裁剪后的字符："+s);
                                bookBodylist.set(i-1,s);
                            }
                        }
                        i++;

                    }
                    if(!isRefresh){
                        if (bookCurrent) {
                            page = bookBodylist.size() - 1;
                        } else {
                            page = 0;
                        }
                    }
                    setBookData();
                    break;
                case 0x223:
                    while (true) {
                        int offset = TextViewUtils.getOffsetForPosition(read_book_x, width - read_book_x.getPaddingRight(),
                                height - getStatusBarHeight() - read_book_x.getPaddingBottom() - 25);
//                        System.out.println("原字符长度：" + book.length() + "截取后的长度：" + offset);
                        if (offset != -1 && offset < book.length()) {
                            book = book.substring(offset + 1);
                            boolean o = false;
                            for (String p : bookBodylist) {
                                if (!p.equals(book)) {
                                    o = true;
                                }
                            }
                            if (o) {
                                bookBodylist.add(book);
                            }
                            offsetArrayList.add(offset);
                            jianqu = true;
                        } else {
                            jianqu = false;
                        }
                        read_book_x.setText(book);
                        if (!jianqu) {
                            handler.sendEmptyMessage(0x123);
                            break;
                        }
                    }
                    break;
                case 0:
                    //播放完成后的回调
                    toast("播放完成");
                    break;
            }
        }
    };

    @Override
    public void initEvent() {
        read_rel_web.setOnClickListener(this);
        read_thecustom_background.setOnClickListener(this);
        read_thecustom_text.setOnClickListener(this);
    }

    /**
     * 填充文章
     */
    public void setBookData() {
        slidingContainer.setOnTapListener(new SlidingLayout.OnTapListener() {
            @Override
            public void onSingleTap(MotionEvent event) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int x = (int) event.getX();
                if (x > screenWidth / 2) {
                    slidingContainer.slideNext();
                } else if (x <= screenWidth / 2) {
                    slidingContainer.slidePrevious();
                }
            }
        });
        slidingContainer.setVisibility(View.VISIBLE);
        // 默认为左右平移模式
        switchSlidingMode();
//        if(isTTS){
//         //执行语音朗读
//            baiDuTTS.speak(bookBodylist.get(page));
//        }
    }

    TextView bookName;
    TextView bookZj;
    TextView bookTime;
    TextView bookpage;
    ReadingTextView bookBody;
    RelativeLayout huadongBeijingZhu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }

    int size;
    class TestSlidingAdapter extends SlidingAdapter<String> {
        @Override
        public View getView(View contentView, String strings) {
            if (contentView == null)
                contentView = getLayoutInflater().inflate(R.layout.sliding_content, null);
            init(contentView);
            bookName.setText(bookName_str);
            String s ;
            if(bookMixAToc!=null){
                s = bookMixAToc.mixToc.chapters.get(mixAtoc).title;
            }else{
                s =bookMixATocLocalBean.get(mixAtoc).title;
            }
            String[] strings1 = strings.split("/////");
            bookZj.setText(s);
            bookBody.setText(strings1[0]);
            if(strings1.length>1){
                s = strings1[1]+"/"+(bookBodylist.size()-1);
            }else{
                s = 1+"/"+(bookBodylist.size()-1);
            }
            bookpage.setText(s);
            if(page >= bookBodylist.size()-2){
                bookBody.setGravity(Gravity.TOP);
            }else{
                bookBody.setGravity(Gravity.CENTER_VERTICAL);
            }
            return contentView;
        }

        @Override
        public String getCurrent() {
            if (page == 0) {
                btnUp.setVisibility(View.VISIBLE);
                btnDown.setVisibility(View.INVISIBLE);
                if(mixAtoc <= 0){
                    btnUp.setText("第一章，禁止向前翻页");
                    btnUp.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_translucent));
                }else{
                    btnUp.setText("");
                    btnUp.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                }
            }else{
                if(page >=bookBodylist.size()-1){
                    btnDown.setVisibility(View.VISIBLE);
                    if(mixAtoc >= size-1){
                        btnDown.setText("最后一章，禁止向后翻页");
                        btnDown.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_translucent));
                    }else{
                        btnDown.setText("");
                        btnDown.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                    }
                }
                btnUp.setVisibility(View.INVISIBLE);
            }

            // 获取当前要显示的内容实例
            return bookBodylist.size() > 0 ? bookBodylist.get(page) +"/////"+page: "内容获取失败......";
        }

        @Override
        public String getNext() {
            // 获取下一个要显示的内容实例
            return bookBodylist.size() > 0 &&(page + 1)<=bookBodylist.size()-1? bookBodylist.get(page + 1) +"/////"+(page + 1): "数据正在加载中......";
        }

        @Override
        public String getPrevious() {
            // 获取前一个要显示的内容实例
            return bookBodylist.size() > 0&&(page - 1)>=0? bookBodylist.get(page - 1) +"/////"+(page - 1): "数据正在加载中......";
        }

        @Override
        public boolean hasNext() {
            // 判断当前是否还有下一个内容实例
            if(mixAtoc==size-1){
                if(bookBodylist.size()>1){
                    return page < bookBodylist.size()-1;
                }else{
                    return page < bookBodylist.size();
                }
            }else{
                return page < bookBodylist.size();
            }
        }

        @Override
        public boolean hasPrevious() {
            // 判断当前是否还有前一个内容实例
            if(mixAtoc == 0){
                return page > 0;
            }else{
                return page > -1;
            }
        }

        @Override
        protected void computeNext() {
            System.out.println("NEXT："+page);
            // 实现如何从当前的实例移动到下一个实例
            int i = 0x00000000;
            if(btnDown.getVisibility()==i){
                if (mixAtoc < size - 1) {
                    isRefresh = false;
                    ++mixAtoc;
                    bookCurrent = false;
                    setBooksData();
                } else {
                    toast("已经是最后一章啦~");
                }
                return;
            }
            btnUp.setVisibility(View.INVISIBLE);
            if (page == bookBodylist.size() - 2) {
                btnDown.setVisibility(View.VISIBLE);
                if(mixAtoc >= size-1){
                    btnDown.setText("最后一章，禁止向后翻页");
                    btnDown.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_translucent));
                }else{
                    btnDown.setText("");
                    btnDown.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                }
            } else {
                btnDown.setVisibility(View.INVISIBLE);
            }
            ++page;
        }

        @Override
        protected void computePrevious() {
            System.out.println("Previous："+page);
            int i = 0x00000000;
            if(btnUp.getVisibility()==i){
                if (mixAtoc > 0) {
                    isRefresh = false;
                    --mixAtoc;
                    bookCurrent = true;
                    setBooksData();
                } else {
                    toast("已经是第一章啦~");
                }
                return;
            }
            // 实现如何从当前的实例移动到前一个实例
            if (page + 1 == 0) {
                btnUp.setVisibility(View.VISIBLE);
                if(mixAtoc <= 0){
                    btnUp.setText("第一章，禁止向前翻页");
                    btnUp.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_translucent));
                }else{
                    btnUp.setText("");
                    btnUp.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
                }
            } else {
                btnUp.setVisibility(View.INVISIBLE);
            }
            btnDown.setVisibility(View.INVISIBLE);
            --page;
        }

        //初始化控件
        public void init(View view) {
            bookName = view.findViewById(R.id.zt_bookname_x);
            bookZj = view.findViewById(R.id.zt_bookzj_x);
            mBattery = view.findViewById(R.id.mybattxxx);
            bookTime = view.findViewById(R.id.zt_time_x);
            bookBody = view.findViewById(R.id.book_x);
            bookpage = view.findViewById(R.id.read_bookpage);
            huadongBeijingZhu = view.findViewById(R.id.huadong_beijing);
            if(thecustomBJ!=null&&thecustomBJ.getIs()!=-1){
                if(!thecustomBJ.getBjColor().equals("-1")){
                    if(thecustomBJ.getIsImg()==0){
                        //根据路径设置图片
                        Bitmap bitm  = BitmapFactory.decodeFile(thecustomBJ.getBjColor());
                        Drawable drawable = new BitmapDrawable(bitm);
                        huadongBeijingZhu.setBackground(drawable);
                    }else{
                        int i = Integer.parseInt(thecustomBJ.getBjColor());
                        huadongBeijingZhu.setBackgroundColor(i);
                    }
                }else{
                    huadongBeijingZhu.setBackground(drawable);
                }
                if(!(thecustomBJ.getTextColor()==-1)){
                    bookBody.setTextColor(thecustomBJ.getTextColor());
                }else{
                    bookBody.setTextColor(fontcor);
                }

            }else{
                huadongBeijingZhu.setBackground(drawable);
                bookBody.setTextColor(fontcor);
            }

            bookBody.setTextSize(zitidaxiao);
            if(page>=bookBodylist.size()){
                page = bookBodylist.size()-1;
            }
            if (!"1".equals(fontsrc)) {
                typeface = Typeface.createFromFile(fontsrc);
                bookBody.setTypeface(typeface);
            }
            if(bookMixAToc!=null){
                size =  bookMixAToc.mixToc.chapters.size();
            }else{
                size = bookMixATocLocalBean.size();
            }
            //开启时间监控
            getTime();
            //开启电池监控
            getDian();
        }
    }

    //更新时间
    public void getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        bookTime.setText(formattedDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开启电池监控广播
        register();
    }

    private void register() {
        registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    // 接受广播,接听电池电量
    private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int power = level * 100 / scale;
                System.out.println("进入电池监控：" + power);
                cell = power;
            }
        }
    };

    public void getDian() {
        mBattery.setPower(cell);
    }

    int height;
    int width;

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
    Dialog dialog;
    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        isRefresh = false;
        switch (v.getId()) {
            case R.id.btn_up:
                break;
            case R.id.btn_down:

                break;
            case R.id.read_button:
                //调用平移动画，打开菜单
                Read_ainmation.showMenuAinm(this, bookCaidanweix, bookCaidanToux, bookQuxiaocaidanx);
                break;
            case R.id.book_quxiaocaidanx:
                //取消菜单
                Read_ainmation.disMenuAinm(this, bookCaidanweix, bookCaidanToux, bookQuxiaocaidanx);
                break;
            case R.id.read_back:
                bookShelf();
                break;
            case R.id.readmix:
                BookMixATocActivity.statrActivity(this,bookMixAToc,bookMixATocLocalBean,mixAtoc,bookName_str);
                break;
            case R.id.read_download:
                //执行下载方法
                if(Tool.isGrantExternalRW(ReadActivity.this)){
                    read_Download();
                }else{
                 toast("没有权限无法下载哦~");
                }
                break;
            case R.id.read_rel_web:
                //打开游览器跳转
                if(!read_rel_text_web.getText().toString().equals("此章节已离线，不支持网页查看！")){
                    Uri uri = Uri.parse(read_rel_text_web.getText().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.read_thecustom_background:
                //自定义阅读背景选择
                String[] strings = {"图片","纯色"};
                AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
                ListView listView = new ListView(this);
                ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,strings);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            //导入图片，从相册选择
                            //使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
                            if(Tool.isGrantExternalRW(ReadActivity.this)){
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 1);
                            }else{
                                toast("没有文件获取权限哦~");
                            }
                        }else{
                            //打开颜色选择器
                            isReadText = false;
                            openColorChoose();
                        }
                        dialog.dismiss();
                    }
                });
                customizeDialog.setTitle("选择背景方案：");
                customizeDialog.setView(listView);
                dialog = customizeDialog.create();
                dialog.show();
                break;
            case R.id.read_thecustom_text:
                //自定义阅读字体颜色选择
                isReadText = true;
                openColorChoose();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO 自动生成的方法存根
        System.out.println(requestCode+"");
        if(requestCode==1)
        {
            //获得图片的uri
            Uri uri = data.getData();
            try
            {
                String path =GetImagePath.getRealPathFromUri(this,uri);
                thecustomBJ.setIs(0);
                thecustomBJ.setIsImg(0);
                thecustomBJ.setBjColor(path);
                Tool.setThecustomBJ(ReadActivity.this,thecustomBJ.getIs(),thecustomBJ.getIsImg(),thecustomBJ.getBjColor(),thecustomBJ.getTextColor());
                //刷新背景
                refreshRead();
                toast("图片背景保存成功");
            }
            catch (Exception e)
            {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                toast("设置失败，图片不支持");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //打开颜色选择器
    public void openColorChoose(){
        /*
         * 创建支持透明度的取色器
         * @param context 宿主Activity
         * @param defauleColor 默认的颜色
         * @param isSupportAlpha 颜色是否支持透明度
         * @param listener 取色器的监听器
         */
        ColorPickerDialog mColorPickerDialog = new ColorPickerDialog(
                this,
                getResources().getColor(R.color.colorBackground),
                true,
                mOnColorPickerListener
        ).show();
    }
    //取色器的监听器
    private OnColorPickerListener mOnColorPickerListener = new OnColorPickerListener() {
        @Override
        public void onColorCancel(ColorPickerDialog dialog) {//取消选择的颜色
            toast("临时模式，不保存！");
        }

        @Override
        public void onColorChange(ColorPickerDialog dialog, int color) {//实时监听颜色变化
            thecustomBJ.setIs(0);
            if(isReadText){
                thecustomBJ.setTextColor(color);
            }else{
                thecustomBJ.setIsImg(1);
                thecustomBJ.setBjColor(""+color);
            }
            refreshRead();
        }

        @Override
        public void onColorConfirm(ColorPickerDialog dialog, int color) {//确定的颜色
            Tool.setThecustomBJ(ReadActivity.this,thecustomBJ.getIs(),thecustomBJ.getIsImg(),thecustomBJ.getBjColor(),thecustomBJ.getTextColor());
            toast("已保存自定义模式");
        }
    };
    public void read_Download(){
        if(Tool.isWifiActive(mContext)){
            if(myBind.downloadIs(bookid)){
                myBind.downloadService(bookid,bookName_str);
            }else{
                //书架中若是没有书籍，应先添加书籍到书架
                toast("书架中没有此书，已自动为你加入书架");
                setShelf();
                myBind.downloadService(bookid,bookName_str);
            }
        }else{
            //打开提示框是否将此书加入书架
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示")
                    .setMessage("当前不是WIFI网络！是否使用流量下载？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(myBind.downloadIs(bookid)){
                                myBind.downloadService(bookid,bookName_str);
                            }else{
                                //书架中若是没有书籍，应先添加书籍到书架
                                toast("书架中没有此书，已自动为你加入书架");
                                setShelf();
                                myBind.downloadService(bookid,bookName_str);
                            }
                        }
                    })
                    .setNegativeButton("取消",null);
            builder.show();
        }

    }
    public void getMoShi() {
        isRefresh = false;
        bookZitisizex.setText(zitidaxiao + "");
        Resources res = getResources();
        switch (yd.getBuju()) {
            case 1:
                drawable = res.getDrawable(R.drawable.hubeijin);
                break;
            case 2:
                drawable = res.getDrawable(R.mipmap.baibei);
                break;
            case 3:
                drawable = res.getDrawable(R.mipmap.bookbai);
                break;
            case 4:
                drawable = res.getDrawable(R.drawable.yebeijin);
                break;
            case 5:
                drawable = res.getDrawable(R.mipmap.niupizhi);
                break;
            case 6:
                drawable = res.getDrawable(R.drawable.changgui);
                break;
            case 7:
                drawable = res.getDrawable(R.mipmap.lansezhi);
                break;
            case 8:
                drawable = res.getDrawable(R.mipmap.lusezhi);
                break;
            case 9:
                drawable = res.getDrawable(R.mipmap.yueliangzhi);
                break;
            case 10:
                drawable = res.getDrawable(R.mipmap.yinghuazhi);
                break;
            case 11:
                drawable = res.getDrawable(R.mipmap.huoyanzhi);
                break;
            default:
                drawable = res.getDrawable(R.mipmap.bookbai);
                break;
        }
        switch (yd.getFontcr()) {
            case 1:
                fontcor = Color.BLACK;
                break;
            case 2:
                fontcor = Color.parseColor("#81556c");
                break;
            case 3:
                fontcor = Color.BLACK;
                break;
            case 4:
                fontcor = Color.parseColor("#525453");
                break;
            case 5:
                fontcor = Color.parseColor("#5c4325");
                break;
            case 6:
                fontcor = Color.BLACK;
                break;
            case 7:
                fontcor = Color.parseColor("#4b6285");
                break;
            case 8:
                fontcor = Color.parseColor("#4b733c");
                break;
            case 9:
                fontcor = Color.parseColor("#4e5a66");
                break;
            case 10:
                fontcor = Color.parseColor("#b7828d");
                break;
            case 11:
                fontcor = Color.parseColor("#614e42");
                break;
            default:
                fontcor = Color.BLACK;
                break;
        }
        if (!"1".equals(yd.getFont())) {
            typeface = Typeface.createFromFile(yd.getFont());
            fontsrc = yd.getFont();
            // 读取用户所使用的字体
            ziYangshiListx.setVisibility(View.INVISIBLE);
        }
    }

    long prelongTim = 0;//定义上一次单击的时间
    View.OnClickListener setPattern = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Resources res = getResources();
            switch (v.getId()){
                case R.id.read_zongse:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.bookbai);
                    fontcor = android.graphics.Color.BLACK;
                    buju = 3;
                    ziticr = 3;
                    break;
                case R.id.read_niupix:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.niupizhi);
                    buju = 5;
                    ziticr = 5;
                    fontcor=Color.parseColor("#5c4325");
                    break;
                case R.id.read_huoyan:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.huoyanzhi);
                    buju = 11;
                    ziticr = 11;
                    fontcor=Color.parseColor("#614e42");
                    break;
                case R.id.read_huyanbeijing:
                    isRefresh = false;
                    drawable = res.getDrawable(R.drawable.hubeijin);
                    fontcor = android.graphics.Color.BLACK;
                    buju = 1;
                    ziticr = 1;
                    break;
                case R.id.read_lanse:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.lansezhi);
                    buju = 7;
                    ziticr = 7;
                    fontcor=Color.parseColor("#4b6285");
                    break;
                case R.id.read_luse:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.lusezhi);
                    buju = 8;
                    ziticr = 8;
                    fontcor=Color.parseColor("#4b733c");
                    break;
                case R.id.read_yinghua:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.yinghuazhi);
                    buju = 10;
                    ziticr = 10;
                    fontcor=Color.parseColor("#b7828d");
                    break;
                case R.id.read_yueliang:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.yueliangzhi);
                    buju = 9;
                    ziticr = 9;
                    fontcor=Color.parseColor("#4e5a66");
                    break;
                case R.id.read_changguimo:
                    isRefresh = false;
                    drawable = res.getDrawable(R.drawable.changgui);
                    buju = 6;
                    ziticr = 6;
                    fontcor=android.graphics.Color.BLACK;
                    break;
                case R.id.read_rijianbeijing:
                    isRefresh = false;
                    drawable = res.getDrawable(R.mipmap.baibei);
                    fontcor =Color.parseColor("#81556c");
                    buju = 2;
                    ziticr = 2;
                    break;
                case R.id.read_yejianbeijing:
                    isRefresh = false;
                    drawable = res.getDrawable(R.drawable.yebeijin);
                    buju = 4;
                    ziticr = 4;
                    fontcor = Color.parseColor("#525453");
                    break;
                case R.id.read_text_a:
                    isRefresh = true;
                    if(zitidaxiao>=13){
                        zitidaxiao--;
                    }else{
                        return;
                    }
                    break;
                case R.id.read_text_b:
                    isRefresh = true;
                    if(zitidaxiao<=31){
                        zitidaxiao++;
                    }else{
                        return;
                    }
                    break;
            }
            // 执行字体布局保存方法
            Tool.setBuJu(mContext, zitidaxiao, fontsrc, buju, ziticr);
            //删除自定义模式
            thecustomBJ.setIs(-1);
            Tool.delThecustomBJ(mContext);
            refreshRead();
        }
    };
    //判断两次时间点击
public void isTiemx(){
    if(prelongTim==0){//第一次单击，初始化为本次单击的时间
        prelongTim = (new Date()).getTime();
    }else{
        long curTime = (new Date()).getTime();//本地单击的时间
        if(!((curTime-prelongTim)>2000)){
            toast("你点击的太快了啊");
            return;
        }
        prelongTim = 0;//当前单击事件变为上次时间
    }
}
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                 bookShelf();
                 break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                slidingContainer.slideNext();
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                slidingContainer.slidePrevious();
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        String id;
        if(bookMixAToc!=null){
            id = bookMixAToc.mixToc.book;
        }else{
            id = bookMixATocLocalBean.get(0).bookid;
        }
        setReadHistory(id);
    }

    //退出应用执行是否加入书架方法
    public void bookShelf(){
    String id;
    if(bookMixAToc!=null){
        id = bookMixAToc.mixToc.book;
    }else{
        id = bookMixATocLocalBean.get(0).bookid;
    }
        List<BookshelfBean> bookshelfBeans = bookshelfBeanDaoUtils.queryBookshelfBeanByQueryBuilder(id);
        if(bookshelfBeans!=null&&bookshelfBeans.size()>0){
            setReadHistory();
        }else{
            //打开提示框是否将此书加入书架
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("提示")
                    .setMessage("书架中还没有此书哦，是否加入书架？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setShelf();
                            setReadHistory();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setReadHistory();
                        }
                    });
            builder.show();
        }
    }

    /**
     * 设置书籍到书架
     */
    public void setShelf(){
        BookshelfBean bookshelfBean = new BookshelfBean();
        bookshelfBean.setBookId(bookMixAToc.mixToc.book);
        bookshelfBean.setCover(Constants.IMG_BASE_URL+app.bookDetailBean.cover);
        bookshelfBean.setName(app.bookDetailBean.title);
        bookshelfBean.setTime(Tool.getTime());
        bookshelfBean.setTimeMillis(System.currentTimeMillis());
        bookshelfBean.setIsEnd(!app.bookDetailBean.isSerial);
        bookshelfBeanDaoUtils.insertBookshelfBean(bookshelfBean);
        List<BookMixATocLocalBean> bookMixATocLocalBeans = new ArrayList<>();
        int i=0;
        for(BookMixAToc.mixToc.Chapters chapters:bookMixAToc.mixToc.chapters){
            BookMixATocLocalBean bookMixATocLocalBean = new BookMixATocLocalBean();
            bookMixATocLocalBean.setBookid(bookMixAToc.mixToc.book);
            bookMixATocLocalBean.setIsOnline(true);
            bookMixATocLocalBean.setLink(chapters.link);
            bookMixATocLocalBean.setTitle(chapters.title);
            bookMixATocLocalBeans.add(bookMixATocLocalBean);
        }
        bookMixATocLocalBeanDaoUtils.insertMultBookMixATocLocalBean(bookMixATocLocalBeans);
    }
    public void setReadHistory(){
    if(isError)
        finish();
        String id;
        if(bookMixAToc!=null){
            id = bookMixAToc.mixToc.book;
        }else{
            id = bookMixATocLocalBean.get(0).bookid;
        }
        setReadHistory(id);
        //关闭数据库
        bookshelfBeanDaoUtils.closeConnection();
        bookMixATocLocalBeanDaoUtils.closeConnection();
        bookReadHistoryDaoUtils.closeConnection();
        bookDataDaoUtils.closeConnection();
        finish();
    }
    public void setReadHistory(String id){
        //加入书籍历史记录
        List<BookReadHistory> bookshelfBeans = bookReadHistoryDaoUtils.queryBookReadHistoryQueryBuilder(id);
        if(bookshelfBeans!=null&&bookshelfBeans.size()>0){
            //存在则更新
            BookReadHistory bookReadHistory = new BookReadHistory();
            bookReadHistory.setId(bookshelfBeans.get(0).getId());
            bookReadHistory.setBookid(id);
            bookReadHistory.setMix(mixAtoc);
            bookReadHistory.setPaga(page);
            bookReadHistoryDaoUtils.updateBookReadHistory(bookReadHistory);
        }else{
            //不存在则添加
            BookReadHistory bookReadHistory = new BookReadHistory();
            bookReadHistory.setBookid(id);
            bookReadHistory.setMix(mixAtoc);
            bookReadHistory.setPaga(page);
            bookReadHistoryDaoUtils.insertBookReadHistory(bookReadHistory);
        }
    }
    public void refreshRead(){
        //判断是否需要重新排版数据
        if(isRefresh){
            isTiemx();
            //重新排版
//                setBooksData();
            read_book_x.setTextSize(zitidaxiao);
            bookZitisizex.setText(zitidaxiao + "");
            if (bookBodylist.size() > 0)
                bookBodylist.removeAll(bookBodylist);
            String s;
            if(bookMixAToc!=null){
                s = bookMixAToc.mixToc.chapters.get(mixAtoc).title;
            }else{
                s = bookMixATocLocalBean .get(mixAtoc).title;
            }
            bookBodylist.add(s);
            book = bookx;
            read_book_x.setText(book);
            bookBodylist.add(book);
            handler.sendEmptyMessage(0x223);
        }else{
            // 默认为左右平移模式
            switchSlidingMode();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connn);
    }

    /**
     * 跳转至阅读界面
     * @param context 上下文
     * @param bookMixAToc 阅读目录
     * @param bookMixATocLocalBean 阅读目录（本地）
     * @param bookName 书名
     * @param bookMixatoc 阅读章节
     * @param bookPage 章节页码
     * @param isMix 是否为目录跳转：用于判断是否加载历史记录
     */
    public static void statrActivity(BaseActivity context, BookMixAToc bookMixAToc,List<BookMixATocLocalBean> bookMixATocLocalBean, String bookName, int bookMixatoc, int bookPage,boolean isMix){
        if(bookMixAToc == null){
            app.bookMixAToc = null;
            app.bookMixATocLocalBean = bookMixATocLocalBean;
        }else{
            app.bookMixAToc = bookMixAToc;
            app.bookMixATocLocalBean = null;
        }
        Intent init = new Intent(context,ReadActivity.class);
        init.putExtra(BOOKNAME,bookName);
        init.putExtra(BOOKMIXATOC,bookMixatoc);
        init.putExtra(BOOKPAGE,bookPage);
        init.putExtra(ISMIX,isMix);
        init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //同名activity只允许一个存活
        context.startActivity(init);
    }
}
