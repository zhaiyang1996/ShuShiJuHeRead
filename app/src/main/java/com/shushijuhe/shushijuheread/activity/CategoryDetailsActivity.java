package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.DetailsAdapter;
import com.shushijuhe.shushijuheread.bean.Categories_infoBean;
import com.shushijuhe.shushijuheread.bean.Sub_CategoriesBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.SpacesItemDecoration;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Boy on 2018/6/8.
 */

public class CategoryDetailsActivity extends BaseActivity {

    private List<Categories_infoBean.BooksBean> bean;
    private MinorAdapter minorAdapter;
    private DetailsAdapter detailsAdapter;
    private Map<String, List<String>> map;
    private String minor = "";
    private String type;
    private int itemPosition = 0;
    public static String name;
    public static String gender;
    private int start = 0;
    @BindView(R.id.details_srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.details_rv_minor)
    RecyclerView rv_minor;
    @BindView(R.id.details_rv_book)
    RecyclerView rv_book;
    @BindView(R.id.details_tv_hot)
    TextView tvHot;
    @BindView(R.id.details_tv_new)
    TextView tvNew;
    @BindView(R.id.details_tv_reputation)
    TextView tvReputation;
    @BindView(R.id.details_tv_over)
    TextView tvOver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "分类详情",
                "", -1, -1);
        //标题栏点击事件，get相应控件
        topMenu.getTopIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initView() {
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        minorAdapter = new MinorAdapter();
        detailsAdapter = new DetailsAdapter(this, 1);
        bean = new ArrayList<>();
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                setData(type, null, "正在刷新中...",false);
                refreshLayout.finishRefresh();
            }
        });

        srlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                addItem();
                refreshLayout.finishLoadMore();
            }
        });
        map = new HashMap<>();
        DataManager.getInstance().getSub_CategoriesBean(new ProgressSubscriber<Sub_CategoriesBean>(
                new SubscriberOnNextListenerInstance() {
                    @Override
                    public void onNext(Object o) {
                        Sub_CategoriesBean sub = (Sub_CategoriesBean) o;
                        if (gender.equals("male")) {
                            for (int i = 0; i < sub.male.size(); i++) {
                                map.put(sub.male.get(i).major, sub.male.get(i).mins);
                            }
                        } else {
                            for (int i = 0; i < sub.female.size(); i++) {
                                map.put(sub.female.get(i).major, sub.female.get(i).mins);
                            }
                        }
                        setRv();
                    }
                }, this, "数据加载中"));
    }

    private void setRv() {
        rv_minor.setAdapter(minorAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_minor.setLayoutManager(linearLayoutManager);
        minorAdapter.setList(map.get(name));
        if (map.get(name).size() == 0) {
            rv_minor.setVisibility(View.GONE);
        }

        rv_book.setAdapter(detailsAdapter);
        rv_book.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv_book.addItemDecoration(new SpacesItemDecoration(this));
        setData("hot", tvHot, "正在搜索中...",false);
    }

    @Override
    public void initEvent() {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CategoryDetailsActivity.class);
        context.startActivity(starter);
    }

    @OnClick({R.id.details_tv_hot, R.id.details_tv_new, R.id.details_tv_reputation, R.id.details_tv_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.details_tv_hot:
                setData("hot", tvHot, "正在搜索中...",false);
                break;
            case R.id.details_tv_new:
                setData("new", tvNew, "正在搜索中...",false);
                break;
            case R.id.details_tv_reputation:
                setData("reputation", tvReputation, "正在搜索中...",false);
                break;
            case R.id.details_tv_over:
                setData("over", tvOver, "正在搜索中...",false);
                break;
        }
    }

    private void addItem() {
        start += 20;
        setData(type, null, null,true);
    }

    private void setData(final String type, TextView tv, String texts, final boolean isAdd) {
        this.type = type;
        DataManager.getInstance().getCategories_info(
                new ProgressSubscriber<Categories_infoBean>(
                        new SubscriberOnNextListenerInstance() {
                            @Override
                            public void onNext(Object o) {
                                Categories_infoBean categories_infoBean = (Categories_infoBean) o;
                                if(isAdd){
                                    bean.addAll(categories_infoBean.books);
                                }else{
                                    bean.removeAll(bean);
                                    bean.addAll(categories_infoBean.books);
                                }

                                detailsAdapter.setBean(bean);
                            }
                        }, this, texts), gender,
                type, name, minor, start + "", "20");

        if (tv != null) {
            tvNew.setTextColor(Color.parseColor("#747474"));
            tvHot.setTextColor(Color.parseColor("#747474"));
            tvOver.setTextColor(Color.parseColor("#747474"));
            tvReputation.setTextColor(Color.parseColor("#747474"));
            tv.setTextColor(Color.parseColor("#5500ffff"));
        }
    }

    class MinorAdapter extends RecyclerView.Adapter<MinorAdapter.MyViewHolder> {

        List<String> list;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(CategoryDetailsActivity.this).inflate(R.layout.item_minor, parent, false);
            return new MyViewHolder(v);
        }

        MinorAdapter() {
            list = new ArrayList<>();
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            setTextColor(position, holder);
            holder.tv_minor.setText(list.get(position));
            holder.tv_minor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minor = list.get(position);
                    itemPosition = position;
                    setData(type, null, "正在搜索中...",false);
                    notifyDataSetChanged();
                }
            });
        }

        private void setTextColor(int position, MyViewHolder holder) {
            if (position == itemPosition) {
                holder.tv_minor.setTextColor(Color.parseColor("#5500ffff"));
            } else {
                holder.tv_minor.setTextColor(Color.parseColor("#747474"));
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public void setList(List<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_minor;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_minor = itemView.findViewById(R.id.minor_tv_minor_type);
            }
        }
    }
}
