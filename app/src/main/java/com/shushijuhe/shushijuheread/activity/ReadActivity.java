package com.shushijuhe.shushijuheread.activity;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
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
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.ChapterRead;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.view.BatteryView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class ReadActivity extends BaseActivity {
    @BindView(R.id.sliding_container)
    SlidingLayout slidingContainer;
    @BindView(R.id.btn_shangx)
    Button btnShangx;
    @BindView(R.id.btn_xiax)
    Button btnXiax;
    @BindView(R.id.button)
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
    @BindView(R.id.ceshitxt)
    TextView ceshitxt;
    @BindView(R.id.huadong_beijing_zhu)
    RelativeLayout huadongBeijingZhu;

    //状态栏控件
    TextView zt_bookname;
    TextView zt_bookzj;
    TextView zt_time;
    Typeface typeface;
    TestSlidingAdapter myslid;
    OverlappedSlider myover;
    int dianchi = 100;

    Button shangx,xiax;
    BookMixAToc bookMixAToc;
    List<ChapterRead> chapterReadList; //书籍详细类容 加载三章
    int mixAtoc = 0;
    private boolean mPagerMode = true;

    private int statusBarHeight = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {

        chapterReadList = new ArrayList<>();
        showWaitingDialog("数据加载中...");
        DataManager.getInstance().getBookMixAToc(new ProgressSubscriber<BookMixAToc>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                bookMixAToc = (BookMixAToc) o;
                if(bookMixAToc!=null){
                    setBooksData();
                }else{
                    toast("目录获取异常");
                }
            }
        },this,null),"555abb2d91d0eb814e5db04f","chapters");

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
    public void setBooksData(){
        for(int i=0;i<3;i++){
            if(i==0&&mixAtoc==0){
                continue;
            }
            if(i==0){
                --mixAtoc;
            }else if(i==2){
                ++mixAtoc;
            }
            final int finalI = i;
            DataManager.getInstance().getBookChapter(new ProgressSubscriber<ChapterRead>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    ChapterRead chapterRead = (ChapterRead) o;
                    chapterReadList.add(chapterRead);
                    if(finalI==2){
                        handler.sendEmptyMessage(0x123);
                    }
                }
            },this,null),bookMixAToc.mixToc.chapters.get(mixAtoc).link);
        }
    }
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disWaitingDialog();
            switch (msg.what){
                case 0x123:
                    setBookData();
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
    public void setBookData(){
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

    class TestSlidingAdapter extends SlidingAdapter<List<ChapterRead> >{
        TextView bookName;
        TextView bookZj;
        BatteryView mBattery;//电池控件
        TextView bookTime;
        TextView bookBody;
        private int index =0;
        String book; //当前章的内容
        @Override
        public View getView(View contentView, List<ChapterRead> chapterReads) {
            contentView = getLayoutInflater().inflate(R.layout.sliding_content, null);
            init(contentView);
            if(chapterReads == null)
                return contentView;
            bookName.setText("法师");
            bookZj.setText(bookMixAToc.mixToc.chapters.get(1).title);
            bookBody.setText(chapterReads.get(1).chapter.body);
            return contentView;
        }

        @Override
        public List<ChapterRead> getCurrent() {
            // 获取当前要显示的内容实例
            return chapterReadList;
        }

        @Override
        public List<ChapterRead> getNext() {
            // 获取下一个要显示的内容实例
            return chapterReadList;
        }

        @Override
        public List<ChapterRead> getPrevious() {
            // 获取前一个要显示的内容实例
            return chapterReadList;
        }

        @Override
        public boolean hasNext() {
            // 判断当前是否还有下一个内容实例
            return false;
        }

        @Override
        public boolean hasPrevious() {
            // 判断当前是否还有前一个内容实例
            return false;
        }

        @Override
        protected void computeNext() {
            // 实现如何从当前的实例移动到下一个实例
        }

        @Override
        protected void computePrevious() {
            // 实现如何从当前的实例移动到前一个实例
        }
        //初始化恐惧
        public void init(View view){
            bookName = view.findViewById(R.id.zt_bookname_x);
            bookZj = view.findViewById(R.id.zt_bookzj_x);
            mBattery = view.findViewById(R.id.mybattxxx);
            bookTime = view.findViewById(R.id.zt_time_x);
            bookBody = view.findViewById(R.id.book_x);
        }
//        public void getBookpage(){
//            DisplayMetrics display = mContext.getResources().getDisplayMetrics();
//            int height = display.heightPixels;
//            int width = display.widthPixels;
//            int offset = TextViewUtils.getOffsetForPosition(bookBody, width - bookBody.getPaddingRight(),
//                    height - getStatusBarHeight() - bookBody.getPaddingBottom()-25);
//            System.out.println("原字符长度："+book.length()+"截取后的长度："+offset);
//            if (offset != -1 && offset < book.length()) {
//                book = book.substring(offset + 1);
//                boolean o = false;
//                for(String p:mylist){
//                    if(!p.equals(contentText)){
//                        o = true;
//                    }
//                }
//                if(o){
//                    mylist.add(contentText);
//                }
//                offsetArrayList.add(offset);
//            }else{
//                i=false;
//            }
//        }
    }

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
        int textHeight = height -getStatusBarHeight()- textView.getPaddingBottom() - textView.getPaddingTop() - 25;
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
}
