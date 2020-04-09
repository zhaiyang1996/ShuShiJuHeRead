package com.zhai.shuyangwx.activity.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zhai.shuyangwx.activity.Play_accompanyActivity;

import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by zhaiyang on 2018/6/1.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Toolbar mCommonToolbar;

    protected Context mContext;
    protected ProgressDialog waitingDialog;
    private int zmGao;
    private int zmKuan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initStar();
        setContentView(getLayoutId());
        initToolBar();
        onCJZT();
        mContext = this;
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        zmGao = point.y;
        zmKuan = point.x;
        ButterKnife.bind(this);
//        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
//        if (mCommonToolbar != null) {
//            initToolBar();
//            setSupportActionBar(mCommonToolbar);
        initView();
        initEvent();
    }
    protected int getZmGao(){
        return zmGao;
    }
    protected int getZmKuan(){
        return zmKuan;
    }
    /**
     * 等待条diaglog
     * @param msg
     */
    protected void showWaitingDialog(String msg) {
        waitingDialog=
                new ProgressDialog(this);
        waitingDialog.setMessage(msg);
        waitingDialog.show();
    }
    protected void disWaitingDialog(){
        waitingDialog.dismiss();
    }

    /**
     * 简单信息提示弹窗
     * @return
     */
    protected void showMsgDialog(String msg){
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);
        customizeDialog.setTitle("提示：");
        TextView textView = new TextView(this);
        textView.setText(msg);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        customizeDialog.setView(textView);
        customizeDialog.show();
//        customizeDialog.setPositiveButton("确定",null);
    }
    public abstract int getLayoutId();

    public abstract void initToolBar();

    public abstract void initView();

    /**
     * 对各种控件进行设置、适配、填充数据
     */
    public abstract void initEvent();

    /**
     * 在加载布局前执行的方法
     */
    public void initStar(){};

    @Override
    protected void onResume() {
        super.onResume();
        JAnalyticsInterface.onPageStart(this,this.getClass().getCanonicalName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JAnalyticsInterface.onPageEnd(this,this.getClass().getCanonicalName());
    }
    Toast toast = null;
    public void toast(String str){
        if(toast==null){
            toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    //沉浸式状态栏
    public void onCJZT() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
