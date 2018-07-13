package com.shushijuhe.shushijuheread.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;
import com.shushijuhe.shushijuheread.activity.BookRankActivity;

import butterknife.BindView;

public class BookstoreFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.book_store_tv_category)
    TextView tvCategory;
    @BindView(R.id.book_store_tv_rank)
    TextView tvRank;
    Context context;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookstore;
    }

    @Override
    public void initToolBar() {

    }

    public void initView() {
        tvCategory.setOnClickListener(this);
        tvRank.setOnClickListener(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        if (v == tvCategory) {
            BookCategoryActivity.start(context);
        } else if (v == tvRank) {
            BookRankActivity.start(context);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
