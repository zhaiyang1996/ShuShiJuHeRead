package com.shushijuhe.shushijuheread.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shushijuhe.shushijuheread.http.ProgressCancelListener;


/**
 * Created by tangpeng on 2017/8/3.
 */

public class DialogUtils {
    public MaterialDialog dialog;

    Context context;
    ProgressCancelListener cancelListener;

    public DialogUtils(Context context, ProgressCancelListener cancelListener) {
        this.context = context;
        this.cancelListener = cancelListener;
    }

    public  void showProgress(final String message){
        hideProgress();
        dialog=new MaterialDialog.Builder(context)
                .content(message)
                .progress(true, 0)
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancelListener.onCancelProgress();
                    }
                }).build();
        dialog.show();
    }
    public void showAppUpdateProgess(final String title, final String message){
        hideProgress();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .positiveText("更新")
                .content(message)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cancelListener.onCancelProgress();
                    }
                }).build();
        dialog.show();
    }

    /**
     * 下载进度样式的dialog
     */
    public void showDownloadAppProgess(String title, String content, long progress){
        hideProgress();
        dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .progress(false, (int) progress, false)
                .build();
        dialog.show();
    }
    /**
     * 更新进度样式的dialog
     */
    public void updateDownloadAppProgess(long progress){
        dialog.setProgress((int) progress);
    }
    public  void hideProgress(){
        if (dialog!=null){
            dialog.dismiss();
            dialog.cancel();
            dialog=null;
        }
    }
}
