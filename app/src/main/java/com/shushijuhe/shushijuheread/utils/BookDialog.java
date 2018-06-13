package com.shushijuhe.shushijuheread.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.dialog_long_bookinfo_cover)
    ImageView bookinfo_cover;
    @BindView(R.id.dialog_long_bookinfo_title)
    TextView bookinfo_title;
    @BindView(R.id.dialog_long_bookinfo_author)
    TextView bookinfo_author;
    @BindView(R.id.dialog_long_bookinfo_lastChapter)
    TextView bookinfo_lastChapter;//最新章节
    @BindView(R.id.dialog_long_bookinfo_latelyFollower)
    TextView bookinfo_latelyFollower;//人气
    @BindView(R.id.dialog_long_bookinfo_shortIntro)
    TextView bookinfo_shortIntro;//书籍简介
    @BindView(R.id.dialog_long_bookinfo_negative)
    ImageView negative;//关闭按钮
    @BindView(R.id.dialog_long_bookinfo_retentionRatio)
    TextView bookinfo_retentionRatio;
    private Context context;
    private OnCloseListener listener;
    private String title;
    private String author;
    private String lastChapter;
    private String latelyFollower;
    private String shortIntro;
    private String coverUrl;
    private String retentionRatio;
    private String tags;

    public BookDialog(Context context) {
        super(context);
        this.context = context;
    }

    public BookDialog(Context context, int themeResId, OnCloseListener listener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
    }

    public BookDialog(Context context, int themeResId, OnCloseListener listener,
                      String title, String author, String lastChapter, String latelyFollower,
                      String shortIntro, String cover_url, String retentionRatio, String tags) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
        this.title = title;
        this.author = author;
        this.lastChapter = lastChapter;
        this.latelyFollower = latelyFollower;
        this.shortIntro = shortIntro;
        this.coverUrl = cover_url;
        this.retentionRatio = retentionRatio;
        this.tags = tags;
    }

    protected BookDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public BookDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public BookDialog setAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookDialog setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
        return this;
    }

    public BookDialog setLatelyFollower(String latelyFollower) {
        this.latelyFollower = latelyFollower;
        return this;
    }

    public BookDialog setShortIntro(String shortIntro) {
        this.shortIntro = shortIntro;
        return this;
    }

    public BookDialog setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_long_bookinfo);
        ButterKnife.bind(this);
//        setCancelable(false);//dialog弹出后会点击屏幕或物理返回键，dialog不消失
//        setCanceledOnTouchOutside(false);//dialog弹出后点击屏幕，dialog不消失；点击物理返回键dialog消失
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        negative.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            bookinfo_title.setText(title);
        }

        if (!TextUtils.isEmpty(author) && !TextUtils.isEmpty(tags)) {
            bookinfo_author.setText(author + "      |      " + tags);
        }

        if (!TextUtils.isEmpty(lastChapter)) {
            bookinfo_lastChapter.setText(lastChapter);
        }

        if (!TextUtils.isEmpty(latelyFollower)) {
            bookinfo_latelyFollower.setText("人气：" + latelyFollower);
        }

        if (!TextUtils.isEmpty(shortIntro)) {
            bookinfo_shortIntro.setText(shortIntro);
        }

        if (!TextUtils.isEmpty(retentionRatio)) {
            bookinfo_retentionRatio.setText("留存率：" + retentionRatio);
        }

        if (!TextUtils.isEmpty(coverUrl)) {
            Glide.with(context)
                    .load(coverUrl)
                    .into(bookinfo_cover);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClickCancel(this, false);
        }
        this.dismiss();
    }

    public interface OnCloseListener {
        void onClickCancel(Dialog dialog, boolean confirm);
    }
}