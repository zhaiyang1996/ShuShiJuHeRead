package com.zhai.shuyangwx.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhai.shuyangwx.MainActivity;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.constants.Constants;
import com.zhai.shuyangwx.utils.Tool;

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
//        Random rand = new Random();
//        int i = rand.nextInt(Constants.TRANSITIONIMAGEURL.length);
//        String path = Tool.getTransition(mContext);
//        if(path!=null&&!path.equals("-1")){
//            File file = new File(path);
//            Glide.with(mContext)
//                    .load(file)
//                    .into(imageView);
//        }else{
//            Glide.with(mContext)
//                    .load(Constants.TRANSITIONIMAGEURL[i])
//                    .into(imageView);
//        }

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
