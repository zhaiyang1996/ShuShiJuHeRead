package com.shushijuhe.shushijuheread.activity.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by zhaiyang on 2018/6/1.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Toolbar mCommonToolbar;

    protected Context mContext;
    protected ProgressDialog waitingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initStar();
        setContentView(getLayoutId());
        initToolBar();
        onCJZT();
        mContext = this;
        ButterKnife.bind(this);
//        mCommonToolbar = ButterKnife.findById(this, R.id.common_toolbar);
//        if (mCommonToolbar != null) {
//            initToolBar();
//            setSupportActionBar(mCommonToolbar);
        initView();
        initEvent();
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
    protected void onDestroy() {
        super.onDestroy();
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
