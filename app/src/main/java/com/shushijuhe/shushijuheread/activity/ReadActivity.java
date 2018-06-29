package com.shushijuhe.shushijuheread.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import com.orhanobut.logger.Logger;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.activity.base.TxtPageBean;
import com.shushijuhe.shushijuheread.animation.Read_ainmation;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.ChapterRead;
import com.shushijuhe.shushijuheread.bean.ReadPatternBean;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.ReadPageUtils;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.paging.TextViewUtils;
import com.shushijuhe.shushijuheread.view.BatteryView;
import com.shushijuhe.shushijuheread.view.ReadingTextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    LinearLayout bookCaidanToux;
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
    private String book; //书籍详细类容
    private String bookx; //书籍详细类容备份
    private List<String> bookBodylist;
    int mixAtoc = 1;//章节
    private boolean mPagerMode = true;
    private ArrayList<Integer> offsetArrayList;
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
    public List<TxtPageBean> txtPageBeans; //章节分页list
    public boolean isOnline = true;
    boolean isRefresh = false; //是否需要刷新
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
        offsetArrayList = new ArrayList<>();
        bookBodylist = new ArrayList<>();
        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        button.setOnClickListener(this);
        bookQuxiaocaidanx.setOnClickListener(this);
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

        Intent intent = getIntent();
//        if(intent!=null){
//            bookMixAToc = (BookMixAToc) getIntent().getSerializableExtra(READBOOKMIX);
//            mixAtoc = getIntent().getIntExtra(BOOKMIXATOC,0);
//            bookName_str = getIntent().getStringExtra(BOOKNAME);
//            page = getIntent().getIntExtra(BOOKPAGE,0);
//            isOnline = intent.getBooleanExtra(BOOKISONLINE,true);
//            setBooksData();
//        }


        DataManager.getInstance().getBookMixAToc(new ProgressSubscriber<BookMixAToc>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                bookMixAToc = (BookMixAToc) o;
                if (bookMixAToc != null) {
                    setBooksData();
                } else {
                    toast("目录获取异常");
                }
            }
        }, this, null), "555abb2d91d0eb814e5db04f", "chapters");

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
        read_book_x.setTextSize(zitidaxiao);
        bookZitisizex.setText(zitidaxiao + "");
        if (bookBodylist.size() > 0)
            bookBodylist.removeAll(bookBodylist);
        bookBodylist.add(bookMixAToc.mixToc.chapters.get(mixAtoc).title);
        showWaitingDialog("数据加载中...");
        //判断是否为在线或本地以便于加载书籍数据
        if(bookMixAToc.mixToc.chapters.get(mixAtoc).isOnline){
            //没有离线则获取网络数据
            DataManager.getInstance().getBookChapter(new ProgressSubscriber<ChapterRead>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    ChapterRead chapterRead = (ChapterRead) o;
                    book = chapterRead.chapter.body;
                    bookx = book;
                    //加载书籍文章
                    read_book_x.setText(book);
                    bookBodylist.add(book);
                    handler.sendEmptyMessage(0x223);
//                getBookPage(book);
                }
            }, this, null), bookMixAToc.mixToc.chapters.get(mixAtoc).link);
        }else{
            //若离线则加载本地数据

        }

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
                                s = bookBodylist.get(i).substring(0,bookBodylist.get(i).length()-1);
                            }

                            int i1 = p.indexOf(s);
                            if(i1!=-1){
                                s = p.substring(0,i1);
                                Log.d("Read", "\n所在位置："+i1+"\n裁剪后的字符："+s);
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
                case 0x677:
                    mBattery.setPower(cell);
                    break;
                case 0x667:
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    String formattedDate = df.format(c.getTime());
                    bookTime.setText(formattedDate);
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


    class TestSlidingAdapter extends SlidingAdapter<String> {
        @Override
        public View getView(View contentView, String strings) {
            if (contentView == null)
                contentView = getLayoutInflater().inflate(R.layout.sliding_content, null);
            init(contentView);
            bookName.setText(bookName_str);
            bookZj.setText(bookMixAToc.mixToc.chapters.get(mixAtoc).title);
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
            }else{
                if(page >=bookBodylist.size()-1){
                    btnDown.setVisibility(View.VISIBLE);
                }
                btnUp.setVisibility(View.INVISIBLE);
            }

            // 获取当前要显示的内容实例
            return bookBodylist.size() > 0 ? bookBodylist.get(page) : "内容获取失败......";
        }

        @Override
        public String getNext() {
            // 获取下一个要显示的内容实例
            return bookBodylist.size() > 0 ? bookBodylist.get(page + 1) : "内容获取失败......";
        }

        @Override
        public String getPrevious() {
            // 获取前一个要显示的内容实例
            return bookBodylist.size() > 0 ? bookBodylist.get(page - 1) : "";
        }

        @Override
        public boolean hasNext() {
            // 判断当前是否还有下一个内容实例
            return page < bookBodylist.size() - 1;
        }

        @Override
        public boolean hasPrevious() {
            // 判断当前是否还有前一个内容实例
            return page > 0;
        }

        @Override
        protected void computeNext() {
            // 实现如何从当前的实例移动到下一个实例
            btnUp.setVisibility(View.INVISIBLE);
            if (page == bookBodylist.size() - 2) {
                btnDown.setVisibility(View.VISIBLE);
            } else {
                btnDown.setVisibility(View.INVISIBLE);
            }
            ++page;
        }

        @Override
        protected void computePrevious() {
            // 实现如何从当前的实例移动到前一个实例
            if (page + 1 == 0) {
                btnUp.setVisibility(View.VISIBLE);
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
            //开启时间监控
            getTime();
            //开启电池监控
            getDian();
            if (!"1".equals(fontsrc)) {
                typeface = Typeface.createFromFile(fontsrc);
                bookBody.setTypeface(typeface);
            }
        }
    }

    //更新时间
    public void getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        bookTime.setText(formattedDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        handler.sendEmptyMessage(0x667);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        }).start();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        handler.sendEmptyMessage(0x677);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        }).start();
    }

    int height;
    int width;

    /**
     * 获取重新规划后的文字排版
     *
     * @param textView
     * @return
     */
    private int getPaddingBottom(TextView textView) {
        int bottom = 0;
        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        int height = display.heightPixels;
        int textHeight = height - getStatusBarHeight() - textView.getPaddingBottom() - textView.getPaddingTop() - 25;
        double lineFloat = textHeight * 1.0 / textView.getLineHeight();
        int line = (int) lineFloat;
        double lineRemainder = lineFloat - line;
        if (lineRemainder < 0.9) {
            lineRemainder = lineRemainder + 0.1;
        }
        bottom = textView.getPaddingBottom() + (int) (lineRemainder * textView.getLineHeight());
        return bottom;
    }

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
                if (mixAtoc > 0) {
                    --mixAtoc;
                    bookCurrent = true;
                    setBooksData();
                } else {
                    toast("已经是第一章啦~");
                }
                break;
            case R.id.btn_down:
                if (mixAtoc < bookMixAToc.mixToc.chapters.size() - 1) {
                    ++mixAtoc;
                    bookCurrent = false;
                    setBooksData();
                } else {
                    toast("已经是最后一章啦~");
                }
                break;
            case R.id.read_button:
                //调用平移动画，打开菜单
                Read_ainmation.showMenuAinm(this, bookCaidanweix, bookCaidanToux, bookQuxiaocaidanx);
                break;
            case R.id.book_quxiaocaidanx:
                //取消菜单
                Read_ainmation.disMenuAinm(this, bookCaidanweix, bookCaidanToux, bookQuxiaocaidanx);
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
                bookBodylist.add(bookMixAToc.mixToc.chapters.get(mixAtoc).title);
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

    public void getBookPage(final String str){
        final ReadPageUtils readPageUtils = new ReadPageUtils();
        readPageUtils.setUpTextParams(zitidaxiao);
        try {
            txtPageBeans = readPageUtils.loadPages(bookName_str,str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer stringBuffer;
        for(TxtPageBean txtPageBean:txtPageBeans){
            stringBuffer = new StringBuffer();
            for(int i = 0;i<=txtPageBean.titleLines;i++){
                stringBuffer.append(txtPageBean.lines.get(i));
            }
            bookBodylist.add(str.toString());
        }
        handler.sendEmptyMessage(0x123);
    }

    /**
     * 跳转至阅读界面
     * @param context 上下文
     * @param bookMixAToc 阅读目录
     * @param bookName 书名
     * @param bookMixatoc 阅读章节
     * @param bookPage 章节页码
     * @param isonline 是否所有章节都已离线
     */
    public static void statrActivity(Activity context, BookMixAToc bookMixAToc, String bookName, int bookMixatoc, int bookPage,boolean isonline){
        Intent init = new Intent(context,ReadActivity.class);
        init.putExtra(READBOOKMIX,bookMixAToc);
        init.putExtra(BOOKNAME,bookName);
        init.putExtra(BOOKMIXATOC,bookMixatoc);
        init.putExtra(BOOKPAGE,bookPage);
        init.putExtra(BOOKISONLINE,isonline);
        init.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //同名activity只允许一个存活
        context.startActivity(init);
    }
}
