package com.zhai.shuyangwx.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.controller.ControlWrapper;
import com.dueeeke.videoplayer.controller.GestureVideoController;
import com.dueeeke.videoplayer.controller.IControlComponent;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.PlayerUtils;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.Play_accompanyActivity;
import com.zhai.shuyangwx.utils.Tool;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义播放控制器
 */
public class MyVideoController extends GestureVideoController implements View.OnClickListener {
    protected ImageView mLockButton;

    protected ProgressBar mLoadingProgress;
    private Play_accompanyActivity activity;

    public MyVideoController(@NonNull Context context) {
        this(context, null);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dkplayer_layout_standard_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        mLockButton = findViewById(R.id.lock);
        mLockButton.setOnClickListener(this);
        mLoadingProgress = findViewById(R.id.loading);
    }
    public void setActivity(Play_accompanyActivity activity){
        this.activity = activity;
    }
    /**
     * 快速添加各个组件
     * @param title  标题
     * @param isLive 是否为直播
     */
    public void addDefaultControlComponent(String title, boolean isLive) {
        CompleteView completeView = new CompleteView(getContext());
        ErrorView errorView = new ErrorView(getContext());
        PrepareView prepareView = new PrepareView(getContext());
        prepareView.setClickStart();
        TitleView titleView = new TitleView(getContext());
        titleView.setTitle(title);
        addControlComponent(completeView, errorView, prepareView, titleView);
        if (isLive) {
            addControlComponent(new LiveControlView(getContext()));
        } else {
            VodControlView vodControlView =new VodControlView(getContext());
            vodControlView.showBottomProgress(false);
            addControlComponent(vodControlView);
        }
        addControlComponent(new GestureView(getContext()));
        setCanChangePosition(!isLive);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lock) {
            mControlWrapper.toggleLockState();
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            mLockButton.setSelected(true);
            Toast.makeText(getContext(), R.string.dkplayer_locked, Toast.LENGTH_SHORT).show();
        } else {
            mLockButton.setSelected(false);
            Toast.makeText(getContext(), R.string.dkplayer_unlocked, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (mLockButton.getVisibility() == GONE) {
                    mLockButton.setVisibility(VISIBLE);
                    if (anim != null) {
                        mLockButton.startAnimation(anim);
                    }
                }
            } else {
                mLockButton.setVisibility(GONE);
                if (anim != null) {
                    mLockButton.startAnimation(anim);
                }
            }
        }
    }

    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                mLockButton.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                if (isShowing()) {
                    mLockButton.setVisibility(VISIBLE);
                } else {
                    mLockButton.setVisibility(GONE);
                }
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                FrameLayout.LayoutParams lblp = (LayoutParams) mLockButton.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }

    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case VideoView.STATE_IDLE:
                mLockButton.setSelected(false);
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
            case VideoView.STATE_PAUSED:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_BUFFERED:
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_BUFFERING:
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                mLoadingProgress.setVisibility(GONE);
                mLockButton.setVisibility(GONE);
                mLockButton.setSelected(false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            Toast.makeText(getContext(), R.string.dkplayer_lock_tip, Toast.LENGTH_SHORT).show();
            return true;
        }
        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }
        return super.onBackPressed();
    }
    class LiveControlView extends FrameLayout implements IControlComponent, View.OnClickListener {

        private ControlWrapper mControlWrapper;

        private ImageView mFullScreen;
        private LinearLayout mBottomContainer;
        private ImageView mPlayButton;
        private RelativeLayout relativeLayout; //弹幕输入框的父控件
        private EditText editText; //弹幕输入框
        private Dialog dialog;

        public LiveControlView(@NonNull Context context) {
            super(context);
        }

        public LiveControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public LiveControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        {
            setVisibility(GONE);
            Button button; //弹出弹幕输入框
            View view = View.inflate(this.getContext(),R.layout.item_danmu_btn,null);
            button = view.findViewById(R.id.item_danmu_btn_btn);
            relativeLayout = new RelativeLayout(this.getContext());
            relativeLayout.setGravity(Gravity.CENTER|Gravity.BOTTOM);
            relativeLayout.setPadding(0,0,0,25);
            relativeLayout.addView(button);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出弹幕框并弹出键盘
                    AlertDialog.Builder customizeDialog = new AlertDialog.Builder(activity);
                    View danMuView = View.inflate(activity,R.layout.item_danmu,null);
                    Button btn = danMuView.findViewById(R.id.item_danmu_btn);
                    //添加弹幕输入控件
                    editText= danMuView.findViewById(R.id.item_danmu_ed);
                    editText.setHint("在这里弹幕和指令");
                    editText.setHintTextColor(Color.GRAY);
                    customizeDialog.setView(danMuView);
                    btn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //执行发送
                            String content = editText.getText().toString();
                            activity.sendOut(content);
                            dialog.dismiss();
                        }
                    });
                    dialog = customizeDialog.create();
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.TOP);
                    dialog.show();
                    //延迟400毫秒打开键盘
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                    {
                        public void run()
                        {
                            Tool.showKeybord(editText,activity);
                        }
                    },400);

                }
            });
            addView(relativeLayout);
            LayoutInflater.from(getContext()).inflate(com.dueeeke.videocontroller.R.layout.dkplayer_layout_live_control_view, this, true);
            mFullScreen = findViewById(com.dueeeke.videocontroller.R.id.fullscreen);
            mFullScreen.setOnClickListener(this);
            mBottomContainer = findViewById(com.dueeeke.videocontroller.R.id.bottom_container);
            mPlayButton = findViewById(com.dueeeke.videocontroller.R.id.iv_play);
            mPlayButton.setOnClickListener(this);
            ImageView refresh = findViewById(com.dueeeke.videocontroller.R.id.iv_refresh);
            refresh.setOnClickListener(this);
        }

        @Override
        public void attach(@NonNull ControlWrapper controlWrapper) {
            mControlWrapper = controlWrapper;
        }

        @Override
        public View getView() {
            return this;
        }

        @Override
        public void onVisibilityChanged(boolean isVisible, Animation anim) {
            if (isVisible) {
                if (getVisibility() == GONE) {
                    setVisibility(VISIBLE);
                    if (anim != null) {
                        startAnimation(anim);
                    }
                }
            } else {
                if (getVisibility() == VISIBLE) {
                    setVisibility(GONE);
                    if (anim != null) {
                        startAnimation(anim);
                    }
                }
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                case VideoView.STATE_START_ABORT:
                case VideoView.STATE_PREPARING:
                case VideoView.STATE_PREPARED:
                case VideoView.STATE_ERROR:
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    setVisibility(GONE);
                    break;
                case VideoView.STATE_PLAYING:
                    mPlayButton.setSelected(true);
                    break;
                case VideoView.STATE_PAUSED:
                    mPlayButton.setSelected(false);
                    break;
                case VideoView.STATE_BUFFERING:
                case VideoView.STATE_BUFFERED:
                    mPlayButton.setSelected(mControlWrapper.isPlaying());
                    break;
            }
        }

        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL:
                    mFullScreen.setSelected(false);
                    relativeLayout.setVisibility(GONE);
                    break;
                case VideoView.PLAYER_FULL_SCREEN:
                    mFullScreen.setSelected(true);
                    relativeLayout.setVisibility(VISIBLE);
                    break;
            }

            Activity activity = PlayerUtils.scanForActivity(getContext());
            if (activity != null && mControlWrapper.hasCutout()) {
                int orientation = activity.getRequestedOrientation();
                int cutoutHeight = mControlWrapper.getCutoutHeight();
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    mBottomContainer.setPadding(0, 0, 0, 0);
                } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    mBottomContainer.setPadding(cutoutHeight, 0, 0, 0);
                } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    mBottomContainer.setPadding(0, 0, cutoutHeight, 0);
                }
            }
        }

        @Override
        public void setProgress(int duration, int position) {

        }

        @Override
        public void onLockStateChanged(boolean isLocked) {
            onVisibilityChanged(!isLocked, null);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == com.dueeeke.videocontroller.R.id.fullscreen) {
                toggleFullScreen();
            } else if (id == com.dueeeke.videocontroller.R.id.iv_play) {
                mControlWrapper.togglePlay();
            } else if (id == com.dueeeke.videocontroller.R.id.iv_refresh) {
//                mControlWrapper.replay(true);
                activity.upData(true);
            }
        }
        /**
         * 横竖屏切换
         */
        private void toggleFullScreen() {
            Activity activity = PlayerUtils.scanForActivity(getContext());
            mControlWrapper.toggleFullScreen(activity);
            Tool.closeKeybord(activity);
        }
    }
}
