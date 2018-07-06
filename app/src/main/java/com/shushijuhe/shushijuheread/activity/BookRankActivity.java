package com.shushijuhe.shushijuheread.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookRankAdapter;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.List;

public class BookRankActivity extends BaseActivity {

    private static final String TAG = "BookRankActivity";
    BookRankAdapter male_ad, female_ad;
    Rank_categoryBean bean;
    RecyclerView rv_male, rv_female;
    List<Rank_categoryBean.MaleBean> male, female;
    private boolean mIsAll = true, fIsAll = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookrank;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "",
                "排行榜", false, false);
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
        male_ad = new BookRankAdapter(this);
        female_ad = new BookRankAdapter(this);
        male_ad.setMyOnClickListener(new BookRankAdapter.MyOnClickListener() {
            @Override
            public void myOnClick(Rank_categoryBean.MaleBean maleBean) {
                if (maleBean.title.equals("更多排行")) {
                    setData(mIsAll);
                    mIsAll = !mIsAll;
                }
            }
        });

        female_ad.setMyOnClickListener(new BookRankAdapter.MyOnClickListener() {
            @Override
            public void myOnClick(Rank_categoryBean.MaleBean maleBean) {
                if (maleBean.title.equals("更多排行")) {
                    setData(fIsAll);
                    fIsAll = !fIsAll;
                }
            }
        });
        rv_male = findViewById(R.id.book_rank_rv_male);
        rv_female = findViewById(R.id.book_rank_rv_female);
        male = new ArrayList<>();
        female = new ArrayList<>();
        rv_male.setAdapter(male_ad);
        rv_female.setAdapter(female_ad);
        rv_female.setLayoutManager(new GridLayoutManager(this, 1));
        rv_male.setLayoutManager(new GridLayoutManager(this, 1));
        getHttp(false);
    }

    @Override
    public void initEvent() {

    }

    public void getHttp(final boolean isAll) {
        DataManager.getInstance().getRank_category(new ProgressSubscriber<Rank_categoryBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                bean = (Rank_categoryBean) o;
                setData(isAll);
            }
        }, this, "正在查找数据......"));
    }

    private void setData(boolean isAll) {
        male.clear();
        female.clear();
        for (int i = 0; i < 8; i++) {
            male.add(bean.male.get(i));
            female.add(bean.female.get(i));
        }
        Rank_categoryBean.MaleBean bean_more = new Rank_categoryBean.MaleBean();
        bean_more.title = "更多排行";
        female.add(bean_more);
        male.add(bean_more);
        if (isAll) {
            for (int i = 8; i < bean.male.size(); i++) {
                male.add(bean.male.get(i));
            }
            for (int i = 8; i < bean.female.size(); i++) {
                female.add(bean.female.get(i));
            }
        }
        female_ad.setList(female);
        male_ad.setList(male);
    }
}
