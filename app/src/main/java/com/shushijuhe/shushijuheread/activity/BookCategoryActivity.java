package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookCategoryAdapter;
import com.shushijuhe.shushijuheread.bean.CategoriesBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import butterknife.BindView;

/**
 * 唐鹏
 * 书籍分类界面
 */
public class BookCategoryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.bookcategory_tv_show_male)
    TextView tv_male;
    @BindView(R.id.bookcategory_tv_show_female)
    TextView tv_female;
    @BindView(R.id.bookcategory_rv_show_male)
    RecyclerView rv_male;
    @BindView(R.id.bookcategory_rv_show_female)
    RecyclerView rv_female;
    private BookCategoryAdapter adapter_male, adapter_female;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookcategory;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "分类详情",
                "", false, false);
        //标题栏点击事件，get相应控件
        topMenu.getTopIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initView() {
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        adapter_male = new BookCategoryAdapter(this);
        rv_male.setAdapter(adapter_male);
        rv_male.setLayoutManager(new GridLayoutManager(this, 3));
        adapter_female = new BookCategoryAdapter(this);
        rv_female.setAdapter(adapter_female);
        rv_female.setLayoutManager(new GridLayoutManager(this, 3));
        setTvBg(tv_male);
        setData();
    }

    @Override
    public void initEvent() {

    }

    private void setData() {
        DataManager.getInstance().getCategories(new ProgressSubscriber<CategoriesBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                adapter_female.setList((CategoriesBean) o, 0);
                adapter_male.setList((CategoriesBean) o, 1);
            }
        }, this, "获取分类信息中......"));
    }

    @Override
    public void onClick(View v) {
        if (v == tv_female) {
            setTvBg(tv_female);
            rv_male.setVisibility(View.GONE);
            rv_female.setVisibility(View.VISIBLE);
        } else if (v == tv_male) {
            setTvBg(tv_male);
            rv_female.setVisibility(View.GONE);
            rv_male.setVisibility(View.VISIBLE);
        }
    }

    private void setTvBg(TextView tv) {
        tv_female.setBackgroundResource(R.drawable.text_bg);
        tv_male.setBackgroundResource(R.drawable.text_bg);
        tv.setBackgroundResource(R.color.itemBg);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, BookCategoryActivity.class);
        context.startActivity(starter);
    }
}
