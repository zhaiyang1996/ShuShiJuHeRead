package com.shushijuhe.shushijuheread.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.MainActivity;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.utils.Tool;

import java.io.File;
import java.util.Random;

import butterknife.BindView;

/**
 * 过渡页
 */
public class TransitionActivity extends BaseActivity {
    @BindView(R.id.transition_image)
    ImageView imageView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_transition;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        Random rand = new Random();
        int i = rand.nextInt(Constants.TRANSITIONIMAGEURL.length);
        String path = Tool.getTransition(mContext);
        if(path!=null&&!path.equals("-1")){
            File file = new File(path);
            Glide.with(mContext)
                    .load(file)
                    .into(imageView);
        }else{
            Glide.with(mContext)
                    .load(Constants.TRANSITIONIMAGEURL[i])
                    .into(imageView);
        }

    }

    @Override
    public void initEvent() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(TransitionActivity.this, MainActivity.class));
                finish();
            }
        },2500);
    }
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
