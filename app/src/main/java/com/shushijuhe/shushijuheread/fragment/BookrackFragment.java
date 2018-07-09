package com.shushijuhe.shushijuheread.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.ReadActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookrackAdapter;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 刘鹏
 * 书架页面
 */
public class BookrackFragment extends Fragment {
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView mRecyclerView;//书架
    @BindView(R.id.boorrack_smart)
    RefreshLayout refreshLayout;//书架
    private BookrackAdapter bookrackAdapter;
    private List<BookshelfBean> list;
    private BookshelfBeanDaoUtils bookshelfBeanDaoUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookrack, container, false);
        bookrackAdapter = new BookrackAdapter(getActivity());
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(getActivity());
        ButterKnife.bind(this, view);
        initRefresh();
        callBack();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 下拉刷新
     */
    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式
        refreshLayout.setRefreshHeader(new ClassicsHeader(Objects.requireNonNull(getActivity())));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);
            }
        });
    }

    /**
     * 回调
     */
    private void callBack() {
        bookrackAdapter.setOnItemClickListener(new BookrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookshelfBean bookshelfBea, ImageView imageView, List<BookMixATocLocalBean> bookMixATocLocalList) {
                if (imageView != null) {

                } else {
                    ReadActivity.statrActivity((BaseActivity) getActivity(), null,
                            bookMixATocLocalList, bookshelfBea.getName(), 0, 0, false);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list = bookshelfBeanDaoUtils.queryAllBookshelfBean();
        bookshelfBeanDaoUtils.closeConnection();//关闭数据库
        bookrackAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);//设置布局管理器
        mRecyclerView.setAdapter(bookrackAdapter);//设置Adapter
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置增加或删除条目的动画
    }

}
