package com.zhai.shuyangwx.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.activity.base.VodClassBean;
import com.zhai.shuyangwx.adapter.BookCategoryAdapter;
import com.zhai.shuyangwx.application.app;
import com.zhai.shuyangwx.bean.CategoriesBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.utils.TopMenuHeader;

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

    int isVod_book = 0; //0为书籍，1为视频，2为搞黄色
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
        isVod_book = this.getIntent().getIntExtra("type",0);
        tv_female.setOnClickListener(this);
        tv_male.setOnClickListener(this);
        adapter_male = new BookCategoryAdapter(this);
        rv_male.setAdapter(adapter_male);
        rv_male.setLayoutManager(new GridLayoutManager(this, 3));
        adapter_female = new BookCategoryAdapter(this);
        rv_female.setAdapter(adapter_female);
        rv_female.setLayoutManager(new GridLayoutManager(this, 3));
        if(!(isVod_book==0)){
            tv_male.setVisibility(View.GONE);
            tv_female.setVisibility(View.GONE);
        }
        setTvBg(tv_male);
        setData();
    }

    @Override
    public void initEvent() {

    }

    private void setData() {
        if(!(isVod_book==0)){
            DataManager.getInstance().getXXClassJson(new ProgressSubscriber<VodClassBean>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    VodClassBean vodClassBean = (VodClassBean) o;
                    adapter_male = new BookCategoryAdapter(BookCategoryActivity.this);
                    rv_male.setAdapter(adapter_male);
                    adapter_male.setXXlist(vodClassBean, 2);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    toast("服务器君出问题啦!!!");
                }
            },this,"获取分类中..."), app.xxUrl);
        }else{
            DataManager.getInstance().getCategories(new ProgressSubscriber<CategoriesBean>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    adapter_female.setList((CategoriesBean) o, 0);
                    adapter_male.setList((CategoriesBean) o, 1);
                }
            }, this, "获取分类信息中......"));
        }

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

    public static void start(Context context,int i) {
        Intent starter = new Intent(context, BookCategoryActivity.class);
        starter.putExtra("type",i);
        context.startActivity(starter);
    }
}
