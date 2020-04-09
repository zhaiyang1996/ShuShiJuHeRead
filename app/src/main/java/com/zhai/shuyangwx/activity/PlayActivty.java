package com.zhai.shuyangwx.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsVideo;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.adapter.GridViewAdapter;
import com.zhai.shuyangwx.adapter.VideoListEngineAdapter;
import com.zhai.shuyangwx.bean.VideoList;
import com.zhai.shuyangwx.bean.VideoUrl;
import com.zhai.shuyangwx.constants.Constants;
import com.zhai.shuyangwx.http.VideoDataManager;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.X5WebView;
import com.zhai.shuyangwx.view.MyGridView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

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
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    @BindView(R.id.webView)
    ViewGroup mViewParent;
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
    private X5WebView mWebView;


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

    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initToolBar() {

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
        mHomeUrl = Constants.CIRCUIT[0];
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
        //根据来源默认选择不同的引擎
        if(vod_type){
            if(yPath.contains("m3u8")){
                play_circuitcuit.setText("播放器一");
                player = 0;
            }else{
                play_circuitcuit.setText("播放器二");
                player = 1;
            }
        }
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
                case R.id.play_circuitcuit:
                    showCircuitDialog();
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
        mWebView = new X5WebView(this, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        if(mWebView.getX5WebViewExtension()!=null){
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", true);
            //true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);
            //false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);
            //1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            mWebView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }


        //拦截超链接挑战
        mWebView.setWebViewClient(new WebViewClient(){
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                         toast("跳转无效!");
                                         return true;
                                     }
                                 });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsConfirm(WebView arg0, String arg1, String arg2,
                                       JsResult arg3) {
                return super.onJsConfirm(arg0, arg1, arg2, arg3);
            }

            View myVideoView;
            View myNormalView;
            CustomViewCallback callback;

            // /////////////////////////////////////////////////////////
            //
            /**
             * 全屏播放配置
             */
            @Override
            public void onShowCustomView(View view,
                                         CustomViewCallback customViewCallback) {
                ViewGroup.LayoutParams layoutParams = mViewParent.getLayoutParams();
                int height = Tool.dip2px(PlayActivty.this,200);
                toast("点击了全屏");
                if(layoutParams.height==height){
                    layoutParams.height=getZmKuan();
                }else{
                    layoutParams.height=height;
                }
                mViewParent.setLayoutParams(layoutParams);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
                getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
                FrameLayout normalView = mWebView;
                ViewGroup viewGroup = (ViewGroup) normalView.getParent();
                viewGroup.removeView(normalView);
                viewGroup.addView(view);
                myVideoView = view;
                myNormalView = normalView;
                callback = customViewCallback;
                Bundle bundle = new Bundle();
                bundle.putInt("screenMode", 102);

            }

            @Override
            public void onHideCustomView() {
                ViewGroup.LayoutParams layoutParams = mViewParent.getLayoutParams();
                int height = Tool.dip2px(PlayActivty.this,200);
//                toast("点击了缩小");
                if(layoutParams.height==height){
                    layoutParams.height=getZmGao();
                }else{
                    layoutParams.height=height;
                }
                mViewParent.setLayoutParams(layoutParams);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);//取消全屏
                if (callback != null) {
                    callback.onCustomViewHidden();
                    callback = null;
                }
                if (myVideoView != null) {
                    ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                    viewGroup.removeView(myVideoView);
                    viewGroup.addView(myNormalView);
                }
            }

            @Override
            public boolean onJsAlert(WebView arg0, String arg1, String arg2,
                                     JsResult arg3) {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
                new AlertDialog.Builder(PlayActivty.this)
                        .setTitle("allow to download？")
                        .setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Toast.makeText(
                                                PlayActivty.this,
                                                "fake message: i'll download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("no",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PlayActivty.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setOnCancelListener(
                                new DialogInterface.OnCancelListener() {

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(
                                                PlayActivty.this,
                                                "fake message: refuse download...",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //采用PC游览器标识
        webSetting.setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        long time = System.currentTimeMillis();
        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != uploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        uploadFile.onReceiveValue(result);
                        uploadFile = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile.onReceiveValue(null);
                uploadFile = null;
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent == null || mWebView == null || intent.getData() == null)
            return;
        mWebView.loadUrl(intent.getData().toString());
    }

    @Override
    protected void onDestroy() {
        if (mTestHandler != null)
            mTestHandler.removeCallbacksAndMessages(null);
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
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
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

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
                                mWebView.loadUrl(videoUrl.getUrl());
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
    public void showCircuitDialog(){
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        ListView listView = new ListView(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line, Constants.CIRCUIT_NAME);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==3){
                    toast("不要着急嘛~");
                    return;
                }
//                if(position==2){
//                    circuit_id = 1;
//                    mHomeUrl="";
//                    play_circuitcuit.setText(Constants.CIRCUIT_NAME[position]);
//                    playVideo(collect-1);
//                    dialog.dismiss();
//                    return;
//                }
                circuit_id = 0;
                player = position;
                play_circuitcuit.setText(Constants.CIRCUIT_NAME[position]);
                playVideo(collect-1,false);
                dialog.dismiss();
            }
        });
        customizeDialog.setTitle("选择播放线路：");
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
            if (pay_type){
                if(yPath.contains("m3u8")){
                    mHomeUrl = Constants.CIRCUIT[0];
                }else{
                    mHomeUrl = Constants.CIRCUIT[1];
                }
            }else{
                mHomeUrl = Constants.CIRCUIT[player];
            }
            path = mHomeUrl+playlist[position];
        }else{
            yPath = playlist[position+1].substring(0,playlist[position+1].length());
            if (pay_type){
                if(yPath.contains("m3u8")){
                    mHomeUrl = Constants.CIRCUIT[0];
                }else{
                    mHomeUrl = Constants.CIRCUIT[1];
                }
            }else{
                mHomeUrl = Constants.CIRCUIT[player];
            }
            if(playlist.length>=0){
                path = mHomeUrl+playlist[position+1].substring(0,playlist[position+1].length());
            }else if(playlist.length>=9){
                path = mHomeUrl+playlist[position+1].substring(0,playlist[position+1].length());
            }else if(playlist.length>=99){
                path = mHomeUrl+playlist[position+1].substring(0,playlist[position+1].length());
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
        mWebView.loadUrl(path);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
                return super.shouldInterceptRequest(webView, s);
                //做广告拦截，ADFIlterTool 为广告拦截工具类

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //去除标题栏
                String js= "javascript:function callJS(){\n" +
                        "      document.getElementsByTagName(\"title\")[0].innerText = '正在播放：" +videoList.getName()+
                        "';" +
                        "   }";
//                    //创建方法
                view.loadUrl(js);
                //加载方法
                view.loadUrl("javascript:callJS()");
            }
        });

        if(!TbsVideo.canUseTbsPlayer(PlayActivty.this)){
            toast("TBS播放器不可用，调用H5播放器");
        }
    }

}
