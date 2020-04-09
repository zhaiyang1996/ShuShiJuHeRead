package com.zhai.shuyangwx.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.adapter.RankDetailsAdapter;
import com.zhai.shuyangwx.bean.RankBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.utils.SpacesItemDecoration;

import butterknife.BindView;

/**
 * 唐鹏 2018/7/16.
 * 排行详情页面
 */
public class RankDetailsFragment extends BaseFragment {

    @BindView(R.id.rankdetails_rv_details)
    RecyclerView rvDetails;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rankdetails;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        String id = null;
        if (bundle != null) {
            id = bundle.getString("id");
        }
        DataManager.getInstance().getRank(new ProgressSubscriber<RankBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                RankBean bean = (RankBean) o;
                initRv(bean);
            }
        }, getContext(), "搜索中..."), id);
    }

    private void initRv(RankBean bean) {
        RankDetailsAdapter adapter = new RankDetailsAdapter(getContext(), bean);
        rvDetails.setAdapter(adapter);
        rvDetails.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvDetails.addItemDecoration(new SpacesItemDecoration(getContext()));
    }

    @Override
    public void initEvent() {

    }
}
