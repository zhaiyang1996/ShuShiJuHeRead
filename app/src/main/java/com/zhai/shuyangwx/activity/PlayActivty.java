package com.zhai.shuyangwx.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoView;
import com.google.gson.Gson;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.adapter.GridViewAdapter;
import com.zhai.shuyangwx.adapter.MyVideoController;
import com.zhai.shuyangwx.adapter.VideoListEngineAdapter;
import com.zhai.shuyangwx.bean.DBTBean;
import com.zhai.shuyangwx.bean.VideoList;
import com.zhai.shuyangwx.bean.VideoUrl;
import com.zhai.shuyangwx.http.VideoDataManager;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.TopMenuHeader;
import com.zhai.shuyangwx.view.MyDanmakuView;
import com.zhai.shuyangwx.view.MyGridView;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhaiyang on 2018/6/6.
 */

public class PlayActivty extends BaseActivity {
    @BindView(R.id.webView)
    VideoView videoView;
    @BindView(R.id.play_name)
    TextView play_name;
    @BindView(R.id.play_year)
    TextView play_year;
    @BindView(R.id.play_grade)
    TextView play_grade;
    @BindView(R.id.play_type)
    TextView play_type;
    @BindView(R.id.play_img)
    ImageView play_img;
    @BindView(R.id.paly_introduce)
    TextView play_introduce;
    @BindView(R.id.play_introduce_btn)
    Button play_introduce_btn;
    @BindView(R.id.play_gtid)
    MyGridView gridView;
    @BindView(R.id.play_engine)
    TextView engineView;
    @BindView(R.id.play_circuitcuit)
    Button play_circuitcuit;
    @BindView(R.id.textView2)
    TextView textView2;
//    private X5WebView mWebView;


    private static  String mHomeUrl = "";
    private static final String TAG = "SdkDemo";
    private boolean mNeedTestPage = true;


    private ValueCallback<Uri> uploadFile;

    private URL mIntentUrl;
    private String id = "";
    private VideoList videoList;//视频详情（包含播放列表）
    private String[] listurl; //视频引擎链条
    private String[] playlist; //视频详细播放信息
    private String path; //视频解析后的播放地址
    private String yPath; //视频原始地址
    private String[] engines; //视频播放引擎数组
    private String engine; //视频播放引擎
    private int player = 0;//播放器
    private int collect = 1;
    private GridViewAdapter gridViewAdapter;
    private VideoListEngineAdapter videoListEngineAdapter;
    private int circuit_id = 0;
    private VideoUrl videoUrl; //终极线路
    String videoUrlStr;
    private boolean pay_type = true;
    private String type ="-1";
    private TopMenuHeader topMenu;
    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initToolBar() {
        topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "视频播放",
                "", false, false);
        //标题栏点击事件，get相应控件
        topMenu.getTopIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //使主控件透明
//        topMenu.getLinearLayout().setBackgroundResource(0);
    }
    //设置APP横屏不刷新
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void initStar() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        Intent intent = getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            } catch (Exception e) {
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }

        /*
         * getWindow().addFlags(
         * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
         */
    }

    //初始化B站播放四
    @Override
    public void initView() {
        play_circuitcuit.setVisibility(View.GONE); //取消播放器选择
        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
        gridViewAdapter = new GridViewAdapter();
        gridView.setAdapter(gridViewAdapter);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
    }
    @SuppressLint("SetTextI18n")
    public void setData(boolean vod_type){
        play_name.setText(videoList.getName());
        play_grade.setText(videoList.getScore());
        play_type.setText(videoList.getType().replaceAll("00b7","·"));
        play_year.setText(videoList.getMsg()+"年");
        play_introduce.setText(videoList.getInfo());
        setEngine();
        play_introduce_btn.setOnClickListener(onClick);
        play_img.setOnClickListener(onClick);
        engineView.setOnClickListener(onClick);
        play_circuitcuit.setOnClickListener(onClick);
        gridViewAdapter.setData(this,playlist.length-1,collect);
        gridView.setOnItemClickListener(gridItemOnClick);
    }
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play_introduce_btn:
                    if(play_introduce.getVisibility()==View.GONE){
                        play_introduce.setVisibility(View.VISIBLE);
                    }else{
                        play_introduce.setVisibility(View.GONE);
                    }
                    break;
                case R.id.play_img:
                    showDialog();
                    break;
                case R.id.play_engine:
                    showDialog();
                    break;
            }
        }
    };
    public void setEngine(){
        String str = "（点击切换来源）当前视频共："+(playlist.length-1)+" 集";
        if(engine.equals("qq")){
            play_img.setImageResource(R.mipmap.ic_logo_qq);
            engineView.setText("腾讯视频"+str);
        }else if(engine.equals("qiyi")){
            play_img.setImageResource(R.mipmap.ic_logo_iqiyi);
            engineView.setText("爱奇艺"+str);
        }else if(engine.equals("mgtv")){
            play_img.setImageResource(R.mipmap.ic_logo_mgtv);
            engineView.setText("芒果TV"+str);
        }else if(engine.equals("youku")){
            play_img.setImageResource(R.mipmap.ic_logo_youku);
            engineView.setText("优酷"+str);
        }else if(engine.equals("tudou")){
            play_img.setImageResource(R.mipmap.ic_logo_tudou);
            engineView.setText("土豆"+str);
        }else if(engine.equals("letv")){
            play_img.setImageResource(R.mipmap.ic_logo_letv);
            engineView.setText("乐视TV"+str);
        }else if(engine.equals("pptv")){
            play_img.setImageResource(R.mipmap.ic_logo_pp);
            engineView.setText("PPTV"+str);
        }else if(engine.equals("sohu")){
            play_img.setImageResource(R.mipmap.ic_logo_sohu);
            engineView.setText("搜狐视频"+str);
        }else if(engine.equals("tm")){
            play_img.setImageResource(R.mipmap.ic_logo_tm);
            engineView.setText("TM视频"+str);
        }else if(engine.equals("fengxing")){
            play_img.setImageResource(R.mipmap.ic_logo_fengxing);
            engineView.setText("风行视频"+str);
        }else if(engine.equals("cntv")){
            play_img.setImageResource(R.mipmap.ic_logo_cntv);
            engineView.setText("央视影音"+str);
        }else {
            play_img.setImageResource(R.mipmap.meng);
            engineView.setText("来源："+engine+str);
        }
    }
    AdapterView.OnItemClickListener gridItemOnClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            playVideo(position,false);
        }
    };
    @Override
    public void initEvent() {
        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(VideoDataManager.getVideList(id,type));
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String str) {
//                Log.d("视频播放数据:", str);
                if(str.equals("-1")){
                    initEvent();
                    return;
                }
                Gson gson = new Gson();
                videoList = gson.fromJson(str, VideoList.class);
                Log.d("视频名：", videoList.getName());
                listurl = videoList.getPlaylist().split("\\$\\$\\$");
                playlist = listurl[0].split("\\$");
                engines = videoList.getPlaycode().split("\\$\\$\\$");
                engine = engines[0];
                    playVideo(circuit_id,true);
                if (str != null) {

                }else{
                    toast("网络异常");
                }
            }
        };
        myObservable.subscribe(mySubscriber);
    }


    private void init() {

    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        videoView.release();
    }

    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    public static final int VIDEOURL = 2;
    private final int mUrlStartNum = 0;
    private int mCurrentUrl = mUrlStartNum;
    @SuppressLint("HandlerLeak")
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    init();
                    break;
                case VIDEOURL:
                    String str = Tool.unicode2String(videoUrlStr);
                    if (str!=null){
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            if (jsonObject!=null){
                                videoUrl = gson.fromJson(str, VideoUrl.class);
//                                mWebView.loadUrl(videoUrl.getUrl()); //开始播放
                                upVideo(path);
                            }else {
                                String mssg = jsonObject.getString("Message");
                                throw  new RuntimeException(mssg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    Dialog dialog;
    public void showDialog(){
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        ListView listView = new ListView(this);
        videoListEngineAdapter = new VideoListEngineAdapter(this);
        listView.setAdapter(videoListEngineAdapter);
        videoListEngineAdapter.setData(engines);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                engine = engines[position];
                playlist = listurl[position].split("\\$");
                playVideo(collect-1,true);
                dialog.dismiss();
            }
        });
        customizeDialog.setTitle("选择播放来源：");
        customizeDialog.setView(listView);
        dialog = customizeDialog.create();
        dialog.show();
    }
    public void playVideo(int position,boolean type){
        String str;
        pay_type =type;
        if(circuit_id==1){
            str = "至尊线路加载中...";
        }else{
            str = "数据加载中...";
        }
//        showWaitingDialog(str);
        path = "";
        if(playlist.length==1){
//            toast("执行的1");
            yPath = playlist[position];
            //根据不同来源选择默认引擎
            path = playlist[position];
        }else{
            yPath = playlist[position+1].substring(0,playlist[position+1].length());

            if(playlist.length>=0){
                path = playlist[position+1].substring(0,playlist[position+1].length());
            }else if(playlist.length>=9){
                path = playlist[position+1].substring(0,playlist[position+1].length());
            }else if(playlist.length>=99){
                path = playlist[position+1].substring(0,playlist[position+1].length());
            }
        }
        collect = position+1;
        setData(type);
        if(circuit_id==1){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    videoUrlStr = VideoDataManager.getVideUrl(path);
                    mTestHandler.sendEmptyMessage(VIDEOURL);
                }
            }).start();
        }

        //播放器是否可以使用
        Log.d("播放地址", path);
//        mWebView.loadUrl(path); //开始播放
        upVideo(path);
    }
 /**
  * 开始初始化播放器
  */
  public void initVideo(){
      videoView.setOnStateChangeListener(new VideoView.OnStateChangeListener() {
          @Override
          public void onPlayerStateChanged(int playerState) {

          }

          @Override
          public void onPlayStateChanged(int playState) {
              switch (playState){
                  case 0:
//                        toast("0");
                      break;
                  case 2:
                      //预留播放进度
//                      if(!dbtBean.getData().isEmpty())
//                          videoView.seekTo(Long.parseLong(dbtBean.getData().get(0).getVod_dq_time()));
//                        toast("视频总时长："+videoView.getDuration());
                      break;
                  case 4:
//                        toast("4");
                      break;
                  case 5:
                      videoView.release();
                      break;
                  case 6:
//                        toast("6");
                      break;
                  case 7:
//                        toast("7");
                      break;
              }
          }
      });
  }
  private MyVideoController controller;//视频播放控制器
  //更新和播放
  public void upVideo(String path){
//                toast(videoView.getCurrentPlayState()+"");//播放器当前状态
      //判断是否更改了播放地址
      if(videoView.getCurrentPlayState()==0){
          if(!path.isEmpty()){
              videoView.release();
              textView2.setVisibility(View.GONE);
              videoView.setUrl(path); //设置视频地址
              controller = new MyVideoController(this);
//              controller.addControlComponent(myDanmakuView); //加入弹幕功能
              controller.setActivity(PlayActivty.this,1);
              controller.addDefaultControlComponent("正在播放："+videoList.getName(), false);
              topMenu.setTopMenuHeader(true, "正在播放："+videoList.getName(),
                      "", false, false);
//              controller.setCanChangePosition(false);
              controller.setEnableInNormal(true);
              videoView.setVideoController(controller); //设置控制器
              videoView.start(); //开始播放，不调用则不自动播放
          }else{
              textView2.setVisibility(View.VISIBLE);
              textView2.setText("当前影片播放出错拉，请刷新试试");
          }
      }else{
          //其他状态处理方案
          if(!path.isEmpty()){
              videoView.release();
              textView2.setVisibility(View.GONE);
              videoView.setUrl(path); //设置视频地址
              controller = new MyVideoController(this);
//              controller.addControlComponent(myDanmakuView); //加入弹幕功能
              controller.setActivity(PlayActivty.this,1);
              controller.addDefaultControlComponent("正在播放："+videoList.getName(), false);
              topMenu.setTopMenuHeader(true, "正在播放："+videoList.getName(),
                      "", false, false);
//              controller.setCanChangePosition(false);
              controller.setEnableInNormal(true);
              videoView.setVideoController(controller); //设置控制器
              videoView.start(); //开始播放，不调用则不自动播放
          }else{
              textView2.setVisibility(View.VISIBLE);
              textView2.setText("当前影片播放出错拉，请刷新试试");
          }
      }
  }
    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }
}
