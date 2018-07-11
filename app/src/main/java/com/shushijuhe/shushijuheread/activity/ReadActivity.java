package com.shushijuhe.shushijuheread.activity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.martian.libsliding.SlidingAdapter;
import com.martian.libsliding.SlidingLayout;
import com.martian.libsliding.slider.OverlappedSlider;
import com.martian.libsliding.slider.PageSlider;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.activity.base.TxtPageBean;
import com.shushijuhe.shushijuheread.animation.Read_ainmation;
import com.shushijuhe.shushijuheread.application.app;
import com.shushijuhe.shushijuheread.bean.BookData;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookReadHistory;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.bean.ChapterRead;
import com.shushijuhe.shushijuheread.bean.ReadPatternBean;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.dao.BookDataDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookReadHistoryDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.service.DownloadService;
import com.shushijuhe.shushijuheread.utils.IOUtils;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.paging.TextViewUtils;
import com.shushijuhe.shushijuheread.view.BatteryView;
import com.shushijuhe.shushijuheread.view.ReadingTextView;
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
                bookid = bookMixAToc.mixToc._id;
            }else{
                if(bookMixATocLocalBean!=null){
                    bookid = bookMixATocLocalBean.get(0).bookid;
                }else {
                    toast("书籍发生了错误，重新添加此书到书架看看！");
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
            }

        }
    };

    @Override
    public void initEvent() {

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

    }

    TextView bookName;
    TextView bookZj;
    TextView bookTime;
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
            bookZj.setText(s);
            bookBody.setText(strings);
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
            return bookBodylist.size() > 0 ? bookBodylist.get(page) : "内容获取失败......";
        }

        @Override
        public String getNext() {
            // 获取下一个要显示的内容实例
            return bookBodylist.size() > 0 &&(page + 1)<=bookBodylist.size()-1? bookBodylist.get(page + 1) : "数据正在加载中......";
        }

        @Override
        public String getPrevious() {
            // 获取前一个要显示的内容实例
            return bookBodylist.size() > 0&&(page - 1)>=0? bookBodylist.get(page - 1) : "数据正在加载中......";
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
            huadongBeijingZhu = view.findViewById(R.id.huadong_beijing);
            huadongBeijingZhu.setBackground(drawable);
            bookBody.setTextColor(fontcor);
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
                read_Download();
                break;
        }
    }
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
        bookShelf();
        return super.onKeyDown(keyCode, event);
    }
    //退出应用执行是否加入书架方法
    public void bookShelf(){
    String id;
    if(bookMixAToc!=null){
        id = bookMixAToc.mixToc._id;
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
        bookshelfBean.setBookId(bookMixAToc.mixToc._id);
        bookshelfBean.setCover(Constants.IMG_BASE_URL+app.bookDetailBean.cover);
        bookshelfBean.setName(app.bookDetailBean.title);
        bookshelfBean.setTime(Tool.getTime());
        bookshelfBeanDaoUtils.insertBookshelfBean(bookshelfBean);
        List<BookMixATocLocalBean> bookMixATocLocalBeans = new ArrayList<>();
        for(BookMixAToc.mixToc.Chapters chapters:bookMixAToc.mixToc.chapters){
            BookMixATocLocalBean bookMixATocLocalBean = new BookMixATocLocalBean();
            bookMixATocLocalBean.setBookid(bookMixAToc.mixToc._id);
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
            id = bookMixAToc.mixToc._id;
        }else{
            id = bookMixATocLocalBean.get(0).bookid;
        }
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
        //关闭数据库
        bookshelfBeanDaoUtils.closeConnection();
        bookMixATocLocalBeanDaoUtils.closeConnection();
        bookReadHistoryDaoUtils.closeConnection();
        bookDataDaoUtils.closeConnection();
        finish();
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
