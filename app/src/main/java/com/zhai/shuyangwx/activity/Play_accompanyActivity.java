package com.zhai.shuyangwx.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dueeeke.videoplayer.player.VideoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.adapter.GridViewAdapter;
import com.zhai.shuyangwx.adapter.MyVideoController;
import com.zhai.shuyangwx.bean.DBTBean;
import com.zhai.shuyangwx.bean.StateBean;
import com.zhai.shuyangwx.bean.UserChatBean;
import com.zhai.shuyangwx.bean.VideoList;
import com.zhai.shuyangwx.bean.VideoSeekBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.http.VideoDataManager;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.view.MyDanmakuView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 点播及陪看放映大厅
 */
public class Play_accompanyActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.play_accompaby_videoview)
    VideoView videoView;
    @BindView(R.id.play_acc_text)
    TextView play_acc_text;
    @BindView(R.id.play_accompaby_text)
    TextView play_accompaby_text;
    @BindView(R.id.play_acc_ed)
    EditText editText;
    @BindView(R.id.play_accrscrol)
    ScrollView scrollView;
    @BindView(R.id.play_acc_text_all)
    TextView play_acc_text_all;
    @BindView(R.id.play_acc_dbd)
    TextView play_acc_dbd;

    private String vod_time;//视频总时长
    private DBTBean dbtBean; //点播单
    private String room_id = "1";//当前房间_默认1号房间
    private String mServerIp = "106.14.82.67";	// 服务器地址192.168.1.103 106.14.82.67
    private int mServerHost = 8894;
    private OutputStream os; //输出流
    private Socket mSocket = null;
    private String name = "萌大人";
    private String sex = "男";
    private String vod_Url = "-1"; //当前播放的链条
    private boolean isDbd = true; //是否显示点播单，默认关闭
    private MyDanmakuView myDanmakuView; //弹幕
    private boolean isDanMu = true; //是否显示弹幕，默认开启
    private MyVideoController controller;//视频播放控制器
    private boolean isXP = false;//是否开启画中画，默认关闭
    private boolean isServer =false; //聊天服务器是否上线，默认下线
    @Override
    public int getLayoutId() {
        return R.layout.activity_play_accompaby;
    }

    @Override
    public void initToolBar() {

    }
    Timer timer;
    @Override
    public void initView() {
        name = Tool.getUser(mContext).getName();
        sex = Tool.getUser(mContext).getSex();
        upData(false);
//        starTimer();
    }

    @Override
    public void initEvent() {
        Tool.closeKeybord(Play_accompanyActivity.this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    final String content = editText.getText().toString();
                    //软键盘回车按钮
                    sendOut(content);
                }
                return false;
            }
        });
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
                        if(!dbtBean.getData().isEmpty())
                        videoView.seekTo(Long.parseLong(dbtBean.getData().get(0).getVod_dq_time()));
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
    public  void upData(final boolean isrefresh){
        DataManager.getInstance().getDBT(new ProgressSubscriber<DBTBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                dbtBean = (DBTBean) o;
//                toast(videoView.getCurrentPlayState()+"");//播放器当前状态
                //判断是否更改了播放地址
                if(!vod_Url.equals("-1")){
                    if(dbtBean!=null&&dbtBean.getData().size()>0&&!vod_Url.equals(dbtBean.getData().get(0).getVod_url())){
                        videoView.release();
                        vod_Url = dbtBean.getData().get(0).getVod_url();
                    }
                }else{
                    if(dbtBean!=null&&dbtBean.getData().size()>0)
                    vod_Url = dbtBean.getData().get(0).getVod_url();
                }
                if(videoView.getCurrentPlayState()==0){
                    if(dbtBean!=null&&dbtBean.getData().size()>0){
                        videoView.release();
                        play_accompaby_text.setVisibility(View.GONE);
                        videoView.setUrl(vod_Url); //设置视频地址
                        myDanmakuView = new MyDanmakuView(Play_accompanyActivity.this);
                        controller = new MyVideoController(Play_accompanyActivity.this);
                        controller.addControlComponent(myDanmakuView);
                        controller.setActivity(Play_accompanyActivity.this,0);
                        controller.addDefaultControlComponent("正在播放："+dbtBean.getData().get(0).getVod_name()+" | 点播人："+dbtBean.getData().get(0).getName(), true);
                        controller.setCanChangePosition(false);
                        controller.setEnableInNormal(true);
                        videoView.setVideoController(controller); //设置控制器
                        videoView.start(); //开始播放，不调用则不自动播放
                        //更新点播单
                        StringBuffer stringBuffer = new StringBuffer();
                        int i = 1;
                        for(DBTBean.DataBean dataBean:dbtBean.getData()){
                            stringBuffer.append(i+"、 "+dataBean.getVod_name()+" — "+dataBean.getName()+"\n");
                            ++i;
                        }
                        play_acc_dbd.setText(stringBuffer.toString());
                    }else{
                        play_accompaby_text.setVisibility(View.VISIBLE);
                        play_accompaby_text.setText("当前无点播影片，发送#+影片名进行点播");
                        play_acc_dbd.setText("当前无点播影片，发送#+影片名进行点播");
                    }
                }else if(videoView.getCurrentPlayState()==3){
                    if(isrefresh){
                        videoView.release();
                        upData(false);
                    }else{
                        if(!(dbtBean!=null&&dbtBean.getData().size()>0)){
                            if(videoView.isFullScreen()){
                                videoView.stopFullScreen();
                                Play_accompanyActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                            videoView.release();
                            //延迟400毫秒打开键盘
                            play_accompaby_text.setVisibility(View.VISIBLE);
                            play_accompaby_text.setText("当前无点播影片，发送#+影片名进行点播");
                            play_acc_dbd.setText("当前无点播影片，发送#+影片名进行点播");
                        }
                    }
                }else if(videoView.getCurrentPlayState()==4){
                    if(isrefresh){
                        videoView.release();
                        upData(false);
                    }else{
                        videoView.start();
                    }
                    //更新点播单
                    StringBuffer stringBuffer = new StringBuffer();
                    int i = 1;
                    for(DBTBean.DataBean dataBean:dbtBean.getData()){
                        stringBuffer.append(i+"、 "+dataBean.getVod_name()+" — "+dataBean.getName()+"\n");
                        ++i;
                    }
                    play_acc_dbd.setText(stringBuffer.toString());
                }else{
                    if(!(dbtBean!=null&&dbtBean.getData().size()>0)){
                        //判断是否为全屏，全屏则退出并旋转为竖屏
                        if(videoView.isFullScreen()){
                            videoView.stopFullScreen();
                            Play_accompanyActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        }
                        videoView.release();
                        play_accompaby_text.setVisibility(View.VISIBLE);
                        play_accompaby_text.setText("当前无点播影片，发送#+影片名进行点播");
                        play_acc_dbd.setText("当前无点播影片，发送#+影片名进行点播");
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d("放映厅错误", e.toString());
                toast("放映厅出错拉~");
            }
        },this,null),room_id);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        isOnline(name,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
        starSockte();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.release();
        if(timer!=null)
        timer.cancel();
    }


    @Override
    public void onBackPressed() {
        if (!videoView.onBackPressed()) {
            super.onBackPressed();
        }
    }

/**
 * Socket通信保持聊天室和播放进度的同步
 */
    private Handler handler;
    @SuppressLint("HandlerLeak")
    public void starSockte(){
        handler = new Handler() {
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自子线程
                switch (msg.what){
                    case 0x000:
                        toast("服务器连接失败");
                        break;
                    case 0x001://服务器连接成功发送用户信息
                        //1为在线，0为离线
                        isOnline(name,true);
                        break;
                    case 0x002:
                        break;
                    case 0x003:
                        toast("消息发送失败");
                        break;
                    case 0x004:
                        editText.setText("");
                        break;
                    case 0x234:
                        // 将读取的内容追加显示在文本框中
                        if(msg.obj!=null&&!msg.obj.toString().isEmpty()){
//                            play_acc_text.append("\n" + msg.obj.toString());
                            String str =msg.obj.toString();
                            Log.d("接受到的信息", str);
                            UserChatBean userChatBean;
                            StringBuffer stringBuffer = new StringBuffer();
                            //解析聊天数据
                            if (str!=null){
                                Gson gson = new Gson();
                                try {
                                    JSONObject jsonObject = new JSONObject(str);
                                    if (jsonObject!=null) {
                                        userChatBean = gson.fromJson(str, UserChatBean.class);
                                        for(UserChatBean.DataBean dataBean:userChatBean.getData()){
                                            stringBuffer.insert(0,"["+dataBean.getName()+"] "+dataBean.getMsg()+"\n");
                                        }
                                        //判断最新信息是不是服务器发送
                                        if(userChatBean.getData().get(userChatBean.getData().size()-1).getIsServer().equals("0")){
                                            //为服务器发送根据服务器信息做动作
                                            if(userChatBean.getData().get(userChatBean.getData().size()-1).getServerMsg().equals("刷新视频")){
//                                                toast(userChatBean.getData().get(userChatBean.getData().size()-1).getServerMsg());
                                                upData(false);
                                            }else if(userChatBean.getData().get(userChatBean.getData().size()-1).getServerMsg().equals("无点播")){
                                                upData(false);
                                            }else if (userChatBean.getData().get(userChatBean.getData().size()-1).getServerMsg().equals("-1")){
                                                //判断是不是当前用户
                                                if(userChatBean.getData().get(userChatBean.getData().size()-1).getMsg().equals(name)) {
                                                    toast("你没有可撤销的影片");
                                                }
                                                break;
                                            }else if(userChatBean.getData().get(userChatBean.getData().size()-1).getServerMsg().equals("撤销")){
                                                upData(false);
                                            }
                                        }
                                        play_acc_text.setText(stringBuffer.toString());
                                        play_acc_text_all.setText("在线人数："+userChatBean.getData().get(userChatBean.getData().size()-1).getOnLineS()+" 人");
                                        String danmu = "["+userChatBean.getData().get(userChatBean.getData().size()-1).getName()+"] "+userChatBean.getData().get(userChatBean.getData().size()-1).getMsg();
                                        if(myDanmakuView!=null){
                                            danMu(userChatBean,danmu);
                                        }
                                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                                        // 输入框失去焦点如果输入框有内容强势获得焦点
                                        if(!editText.getText().toString().isEmpty()){
                                            editText.requestFocus();
                                        }
                                    } else {
                                        String mssg = jsonObject.getString("Message");
                                        throw  new RuntimeException(mssg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 初始化该Socket对应的输入流
                try {
                    mSocket  = new Socket(mServerIp, mServerHost);
                    new Thread(new ClientThread(mSocket,handler)).start();
                    os = mSocket.getOutputStream();
                    handler.sendEmptyMessage(0x001);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x000);
                }
            }
        }).start();
    }
     class ClientThread implements Runnable {
        //流
        BufferedReader br = null;
        Handler handler;
        private Socket socket=null;
        public ClientThread(Socket socket,Handler handler) throws IOException{
            this.handler = handler;
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    "utf-8"));
        }
        @Override
        public void run() {
            try {
                String content = null;
                // 采用循环不断从Socket中读取客户端发送过来的数据
                while ((content = br.readLine()) != null) {
                    Message msg = new Message();
                    msg.what=0x234;
                    msg.obj = content;
                    handler.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //输入框发送消息给服务器
    private void setMsg(final UserChatBean.DataBean userChatBean){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append( userChatBean.getIsOnline()+"###"+userChatBean.getMsg()+"###"+userChatBean.getName()+"###"+userChatBean.getTime()+"###"+userChatBean.getSex());
                    String string = Tool.replaceBlank(stringBuffer.toString())+"\n";
                    Log.d("发送数据", string);
                    if(os==null)
                        return;
                    os.write(string.getBytes());
                    os.flush();
                    if(!isServer){
                        mSocket.shutdownInput();
                        os.close();
                        mSocket.close();
                    }
                    handler.sendEmptyMessage(0x004);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x003);
                }
            }
        }).start();

    }
    //判断用户是否加入直播间
    public void isOnline(String name,boolean is){
        UserChatBean.DataBean userBean = new UserChatBean.DataBean();
        if(is){
            //1为在线，0为离线
            userBean.setIsOnline("1");
            userBean.setMsg(name+"加入聊天室");
            isServer = true;
        }else{
            userBean.setIsOnline("0");
            userBean.setMsg(name+"离开了聊天室");
            isServer = false;
        }
        userBean.setName("系统");
        userBean.setTime(Tool.getTime());
        userBean.setSex(sex);
        setMsg(userBean);
    }

    /**
     * 点播功能，搜索服务器视频数据
     */
    private String req;
    private Handler vodHandler;
    private VideoSeekBean videoSeekBean;
    private Dialog dialog;
    private VideoList videoList;//视频详情（包含播放列表）
    private String[] listurl; //视频引擎链条
    private String[] playlist; //视频详细播放信息
    private String path; //视频解析后的播放地址
    private String yPath; //视频原始地址
    private String[] engines; //视频播放引擎数组
    @SuppressLint("HandlerLeak")
    public void getVodJson(final String vod_name){
        showWaitingDialog("视频搜索中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                req = VideoDataManager.getVideBean(vod_name);
                vodHandler.sendEmptyMessage(0x123);
            }
        }).start();
        vodHandler = new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //关闭加载
                disWaitingDialog();
                switch (msg.what){
                    case 0x123:
                        String str = req;
                        if (str != null) {
                            Gson gson = new Gson();
                            videoSeekBean = gson.fromJson(str, new TypeToken<VideoSeekBean>() {
                            }.getType());
                            ListView listView = new ListView(Play_accompanyActivity.this);
                            final List<String> vod_list_name = new ArrayList<>();
                            for(VideoSeekBean.DataBean bean:videoSeekBean.getData()){
                                vod_list_name.add(bean.getName());
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(Play_accompanyActivity.this,android.R.layout.simple_dropdown_item_1line, vod_list_name);
                            listView.setAdapter(arrayAdapter);
                            AlertDialog.Builder customizeDialog = new AlertDialog.Builder(Play_accompanyActivity.this);
                            customizeDialog.setTitle("选择影片：");
                            customizeDialog.setView(listView);
                            dialog = customizeDialog.create();
                            if(!videoSeekBean.getData().isEmpty()){
                                dialog.show();
                            }else{
                                showMsgDialog("没有搜索到相关影片哟~");
                            }

                            //设置listview点击事件
                            setListOnItem(listView);
                        }
                        break;
                }
            }
        };
    }
    private VideoView videoViewServer;
    private String videoUrl;
    private String vod_name;
    public void setListOnItem(ListView listView){
        videoViewServer = new VideoView(this);
        videoViewServer.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                //获取实际播放地址（存在m3u8直链，不存在则提示无资源，试试其他选项）
                Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(VideoDataManager.getVideList(videoSeekBean.getData().get(i).getVod_id(),"-1"));
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
                Subscriber<String> mySubscriber = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        toast("服务器异常");
                    }

                    @Override
                    public void onNext(String str) {
                        Gson gson = new Gson();
                        videoList = gson.fromJson(str, VideoList.class);
                        listurl = videoList.getPlaylist().split("\\$\\$\\$");
                        engines = videoList.getPlaycode().split("\\$\\$\\$");

                        boolean isM3u8 = false;
                        int m3u8 = 0;
                        for(String zUrl:listurl){
                            playlist = listurl[m3u8].split("\\$");
                            for(String url:playlist){
                                if(url.contains("m3u8")){
                                    Log.d("视频全部地址：", zUrl);
                                    isM3u8 = true;
                                    break;
                                }else {
                                    isM3u8 = false;
                                }
                            }
                            m3u8++;
                        }
                        if(isM3u8){
                            dialog.dismiss();
                            //打开集数弹窗
                            GridViewAdapter gridViewAdapter = new GridViewAdapter();
                            GridView gridView = new GridView(Play_accompanyActivity.this);
                            gridView.setAdapter(gridViewAdapter);
                            gridView.setNumColumns(3);
                            gridViewAdapter.setData(Play_accompanyActivity.this,playlist.length-1,1);
                            AlertDialog.Builder customizeDialog = new AlertDialog.Builder(Play_accompanyActivity.this);
                            customizeDialog.setTitle("选择集数：");
                            customizeDialog.setView(gridView);
                            dialog = customizeDialog.create();
                            dialog.show();
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                                        toast("视频地址："+playlist[i+1]);
//                                                        Log.d("视频地址：", playlist[i+1]);
                                    dialog.dismiss();
                                    if(playlist.length>1){
                                        vod_name= videoList.getName()+" 第"+(i+1)+"集";
                                    }else{
                                        vod_name= videoList.getName();
                                    }
                                    videoUrl = playlist[i+1];
                                   //判断用户是否已有点播影片
                                    boolean is = false;
                                    if(dbtBean!=null&&!dbtBean.getData().isEmpty()){
                                        for(DBTBean.DataBean dataBean:dbtBean.getData()){
                                            if(dataBean.getName().equals(name)){
                                                is = true;
                                                break;
                                            }else{
                                                is = false;
                                            }
                                        }
                                    }
                                    if(!is){
                                        //后台播放视频获得视频总时长
                                        videoViewServer.setUrl(videoUrl); //设置视频地址
                                        videoViewServer.start();
                                        showWaitingDialog("加载中...");
                                    }else{
                                        showMsgDialog("你已经有影片在播单了哟，发送cut撤销后在点播呀！");
                                    }

                                }
                            });
                        }else{
                            toast("没有直链资源，试试其他的~");
                        }
                    }
                };
                myObservable.subscribe(mySubscriber);

            }
        });
        videoViewServer.setOnStateChangeListener(new VideoView.OnStateChangeListener() {
            @Override
            public void onPlayerStateChanged(int playerState) {

            }

            @Override
            public void onPlayStateChanged(int playState) {
                switch (playState) {
                    case 2:
                        //获得视频地址，添加点播单
                        DataManager.getInstance().setDBT(new ProgressSubscriber<StateBean>(new SubscriberOnNextListenerInstance() {
                            @Override
                            public void onNext(Object o) {
                                StateBean s = (StateBean) o;
                                disWaitingDialog();
                                if(s.getState()==0){
                                    toast("点播成功");

                                }else{
                                    toast("点播失败");
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                disWaitingDialog();
                                toast("点播失败:服务器异常");
                            }
                        },Play_accompanyActivity.this,null),room_id,name,videoUrl,vod_name,videoViewServer.getDuration()+"");
                        videoViewServer.release();//关闭播放器
                        break;
                }
            }
        });
    }
    /**
     * 聊天框发送事件
     */
    public void sendOut(String content){
        if(content.isEmpty()){
            toast("聊天框不能为空！");
        }else{
            //转义特殊符号
            //判断是不是点播指令
            if(content.substring(0,1).equals("#")){
                Tool.closeKeybord(Play_accompanyActivity.this);
                //第一个元素为#打开点播单
                getVodJson(content.substring(1,content.length()));
                handler.sendEmptyMessage(0x004);
            }else if(content.length()==4&&content.equals("help")){
                editText.setText("");
                //判断用户是否打开点播单
                String help = "目前已有指令：\n" +
                        "#：后面加电影名可点播（可模糊搜索）\n" +
                        "cut：撤销自己点播的电影\n" +
                        "dbd：打开或关闭点播单\n" +
                        "help：查看指令帮助\n"+
                        "danmu：打开或者关闭弹幕\n"+
                        "star：设置或取消聊天室为启动页";
                showMsgDialog(help);
                handler.sendEmptyMessage(0x004);
            }else if(content.length()==3&&content.equals("dbd")){
                //打开点播或者关闭单
                if(isDbd){
                    play_acc_dbd.setVisibility(View.VISIBLE);
                }else{
                    play_acc_dbd.setVisibility(View.GONE);
                }
                isDbd = !isDbd;
                handler.sendEmptyMessage(0x004);
            }else if(content.length()==5&&content.equals("danmu")){
                //打开或者关闭弹幕
                if(isDanMu){
                    myDanmakuView.hide();
                    toast("弹幕已关闭");
                }else{
                    myDanmakuView.show();
                    toast("弹幕已打开");
                }
                isDanMu = !isDanMu;
                handler.sendEmptyMessage(0x004);
            }else if(content.length()==2&&content.equals("xp")){
                //打开或悬浮窗
                startFloatWindow(isXP);
                isXP = !isXP;
            }else if(content.length()==4&&content.equals("star")){
                //设置或取消默认启动页
                if(Tool.getUser(mContext).getIsQd().equals("-1")){
                    Tool.setUser(mContext,name,sex,"0");
                    toast("设置聊天室为启动页");
                }else{
                    Tool.setUser(mContext,name,sex,"-1");
                    toast("取消聊天室为启动页");
                }
                handler.sendEmptyMessage(0x004);
            }else{
                if (mSocket!=null && mSocket.isConnected() && !mSocket.isOutputShutdown()) {
                    // 将用户在文本框内输入的内容写入网络
                    //1为在线，0为离线
                    UserChatBean.DataBean userChatBean = new UserChatBean.DataBean();
                    userChatBean.setIsOnline("1");
                    userChatBean.setMsg(Tool.changeFH(content));
                    userChatBean.setName(name);
                    userChatBean.setTime(Tool.getTime());
                    userChatBean.setSex(sex);
                    setMsg(userChatBean);
                    Tool.closeKeybord(Play_accompanyActivity.this);
                }
            }
        }
    }
    public void danMu(UserChatBean userChatBean,String danmu){
        if(userChatBean.getData().get(userChatBean.getData().size()-1).getName().equals(name)){
            myDanmakuView.addDanmaku(danmu,true,false);
        }else{
            //判断是否为服务器的弹幕
            if(userChatBean.getData().get(userChatBean.getData().size()-1).getName().equals("系统")){
                myDanmakuView.addDanmaku(danmu,false,true);
            }else{
                myDanmakuView.addDanmaku(danmu,false,false);
            }
        }
    }
    /**
     * 打开画中画窗口
     */
    public void startFloatWindow(final boolean is) {
        if(Tool.isGrantExternalRW(this)){
            if(is){

            }else{

            }
        }else{
            toast("没有悬浮窗权限");
            isXP = !isXP;
        }
    }
}
