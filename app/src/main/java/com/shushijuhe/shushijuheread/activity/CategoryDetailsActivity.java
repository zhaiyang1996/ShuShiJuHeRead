package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.DetailsAdapter;
import com.shushijuhe.shushijuheread.bean.Categories_infoBean;
import com.shushijuhe.shushijuheread.bean.Sub_CategoriesBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.Tool;

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

    private MinorAdapter minorAdapter;
    private DetailsAdapter detailsAdapter;
    private Map<String, List<String>> map;
    private String minor = "";
    private String type;
    public static String name;
    public static String gender;
    private int start = 0;
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

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        minorAdapter = new MinorAdapter();
        detailsAdapter = new DetailsAdapter(this);
        map = new HashMap<>();
        DataManager.getInstance().getSub_CategoriesBean(new ProgressSubscriber<Sub_CategoriesBean>(new SubscriberOnNextListenerInstance() {
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
        }, this, "数据加载中..."));
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
        SpacesItemDecoration decoration = new SpacesItemDecoration();
        rv_book.addItemDecoration(decoration);
        setData("hot");
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
                setData("hot");
                break;
            case R.id.details_tv_new:
                setData("new");
                break;
            case R.id.details_tv_reputation:
                setData("reputation");
                break;
            case R.id.details_tv_over:
                setData("over");
                break;
        }
    }

    private void addItem() {

    }

    private void setData(final String type) {
        this.type = type;
        DataManager.getInstance().getCategories_info(
                new ProgressSubscriber<Categories_infoBean>(
                        new SubscriberOnNextListenerInstance() {
                            @Override
                            public void onNext(Object o) {
                                Categories_infoBean bean = (Categories_infoBean) o;
                                detailsAdapter.setBean(bean);
                                toast(bean.books.size()+"");
                                Toast.makeText(CategoryDetailsActivity.this, "type--->" + type + "minor--->" + minor + "请求完毕", Toast.LENGTH_SHORT).show();
                            }
                        }, this, "正在搜索中..."), gender,
                type, name, minor, start + "", "20");
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
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.tv_minor.setText(list.get(position));
            holder.tv_minor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minor = list.get(position);
                    setData(type);
                }
            });
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

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        public SpacesItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = Tool.dip2px(CategoryDetailsActivity.this,10);
            outRect.bottom = Tool.dip2px(CategoryDetailsActivity.this,10);
        }
    }
}
