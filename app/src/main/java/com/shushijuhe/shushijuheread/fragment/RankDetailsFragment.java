package com.shushijuhe.shushijuheread.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.adapter.RankDetailsAdapter;
import com.shushijuhe.shushijuheread.bean.RankBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.SpacesItemDecoration;

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
