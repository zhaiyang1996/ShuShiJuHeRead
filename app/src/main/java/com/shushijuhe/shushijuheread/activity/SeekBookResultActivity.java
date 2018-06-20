package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeekBookResultActivity extends BaseActivity {


    @BindView(R.id.book_rv_result)
    RecyclerView rvResult;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seekbook_result;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        rvResult.setAdapter(detailsAdapter);
        rvResult.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvResult.addItemDecoration(SpacesItemDecoration());
    }

    @Override
    public void initEvent() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SeekBookResultActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
