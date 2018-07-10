package com.shushijuhe.shushijuheread.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

/**
 * 刘鹏
 * 书架页面
 */
public class BookrackFragment extends BaseFragment {
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView mRecyclerView;//书架
    @BindView(R.id.bookrack_ll_layout)
    LinearLayout linearLayout;//背景图
    @BindView(R.id.bookrack_bt_addbook)
    Button button;//添加书籍
    @BindView(R.id.boorrack_smart)
    RefreshLayout refreshLayout;//下拉刷新
    private BookrackAdapter bookrackAdapter;
    private List<BookshelfBean> list;
    private BookshelfBeanDaoUtils bookshelfBeanDaoUtils;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookrack;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        bookrackAdapter = new BookrackAdapter(getActivity());
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(getActivity());
        initRefresh();
        callBack();
    }

    @Override
    public void initEvent() {

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
        //设置 Header 为 金典 样式
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
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
        refreshLayout.setEnableRefresh(true);
        list = bookshelfBeanDaoUtils.queryAllBookshelfBean();
        bookshelfBeanDaoUtils.closeConnection();//关闭数据库
        if (list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            bookrackAdapter.setData(list);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置布局管理器
            mRecyclerView.setAdapter(bookrackAdapter);//设置Adapter
            mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
            mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置增加或删除条目的动画
        } else {
            refreshLayout.setEnableRefresh(false);
            mRecyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
