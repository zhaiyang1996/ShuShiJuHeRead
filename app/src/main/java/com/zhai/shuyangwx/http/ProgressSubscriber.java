package com.zhai.shuyangwx.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zhai.shuyangwx.utils.DialogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mSubscriberOnNextListener;
    private DialogUtils dialogUtils;
    private Context context;
    private String message;
    public ProgressSubscriber(SubscriberOnNextListenerInstance mSubscriberOnNextListener, Context context, String message) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.message=message;
        dialogUtils=new DialogUtils(context,this);
    }
    /**
     * 订阅开始时调用
     * 根据需要显示ProgressDialog
     */
    @Override
    public void onStart() {
        if (!TextUtils.isEmpty(message)){
            dialogUtils.showProgress(message);
        }
    }
    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dialogUtils.hideProgress();
//        Toast.makeText(context, "Get Data Completed", Toast.LENGTH_SHORT).show();
        Logger.d("Get Data Completed");
    }
    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络异常，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "连接超时，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(context,  e.getMessage(), Toast.LENGTH_SHORT).show();
            mSubscriberOnNextListener.onError(e);
        }
        dialogUtils.hideProgress();
    }
    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        dialogUtils.hideProgress();
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }
}
