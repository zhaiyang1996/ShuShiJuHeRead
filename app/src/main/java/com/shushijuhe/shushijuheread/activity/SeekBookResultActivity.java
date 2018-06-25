package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.DetailsAdapter;
import com.shushijuhe.shushijuheread.bean.Book_infoBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.SpacesItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeekBookResultActivity extends BaseActivity {

    private static final String KEY_SEEK = "seek";// prsf String
    @BindView(R.id.book_rv_result)
    RecyclerView rvResult;
    private DetailsAdapter detailsAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_seekbook_result;
    }

    @Override
    public void initToolBar() {

    }

    public static void start(@NonNull Context context, @NonNull String text) {
        Intent starter = new Intent(context, SeekBookResultActivity.class);
        starter.putExtra(KEY_SEEK, text);
        context.startActivity(starter);
    }

    @Override
    public void initView() {
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        detailsAdapter = new DetailsAdapter(this, 0);
        rvResult.setAdapter(detailsAdapter);
        rvResult.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvResult.addItemDecoration(new SpacesItemDecoration(this));

        String seek = getIntent().getStringExtra(KEY_SEEK);
        setData(seek);
    }

    @Override
    public void initEvent() {

    }

    private void setData(String seek) {
        DataManager.getInstance().getBook_info(new ProgressSubscriber<Book_infoBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                detailsAdapter.setBean(o);
            }
        }, this, "正在搜索中..."), seek);
    }
}
