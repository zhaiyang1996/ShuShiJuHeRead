package com.shushijuhe.shushijuheread.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * 翟阳：2018/7/10 碎片基类，封装
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected ProgressDialog waitingDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initToolBar();
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        this.mContext = getActivity();
        initView();
        initEvent();
        return view;
    }
    /**
     * 等待条diaglog
     * @param msg
     */
    protected void showWaitingDialog(String msg) {
        waitingDialog=
                new ProgressDialog(getActivity());
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
    public void onResume() {
        super.onResume();
        JAnalyticsInterface.onPageStart(getActivity(),this.getClass().getCanonicalName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JAnalyticsInterface.onPageEnd(getActivity(),this.getClass().getCanonicalName());
    }
    Toast toast = null;
    public void toast(String str){
        if(toast==null){
            toast = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        }else{
            toast.setText(str);
        }
        toast.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
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
}
