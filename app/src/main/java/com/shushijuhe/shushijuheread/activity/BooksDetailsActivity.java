package com.shushijuhe.shushijuheread.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import butterknife.BindView;

/**
 * 书籍 详情
 */
public class BooksDetailsActivity extends BaseActivity {
    @BindView(R.id.booksdetails_text_read)
    TextView read;//开始阅读
    @BindView(R.id.booksdetails_text_chase)
    TextView chase;//加入书架
    @BindView(R.id.booksdetails_text_title)
    TextView title;//书名
    @BindView(R.id.booksdetails_text_genre)
    TextView genre;//类型
    @BindView(R.id.booksdetails_text_writer)
    TextView writer;//作者
    @BindView(R.id.booksdetails_text_words)
    TextView words;//字数
    @BindView(R.id.booksdetails_text_subscription)
    TextView subscription;//订阅
    @BindView(R.id.booksdetails_text_status)
    TextView status;//是否连载
    @BindView(R.id.booksdetails_text_synopsis)
    TextView synopsis;//内容简介
    @BindView(R.id.booksdetails_iv_cover)
    ImageView cover;//内容简介

    @Override
    public int getLayoutId() {
        return R.layout.activity_booksdetails;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "",
                "书籍详情", R.mipmap.title_seek, R.mipmap.title_menu);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "开始阅读", Toast.LENGTH_SHORT).show();
            }
        });
        chase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "加入书架", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
