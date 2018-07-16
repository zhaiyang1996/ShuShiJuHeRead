package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookRankVpAdapter;
import com.shushijuhe.shushijuheread.fragment.RankDetailsFragment;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 唐鹏 2018/7/16.
 * 分类详情界面
 */
public class RankDetailsActivity extends BaseActivity {

    private static final String TAG = "RankDetailsActivity";
    private static final String TITLE = "title";
    private static final String ID = "id";
    private static final String MONTH_RANK = "monthRank";
    private static final String TOTAL_RANK = "totalRank";
    @BindView(R.id.rank_details_vp_ranklist)
    ViewPager vpRank;
    @BindView(R.id.book_rank_tl_title)
    TabLayout tlTitle;

    public static void start(Context context, String title, String id, String monthRank, String totalRank) {
        Intent starter = new Intent(context, RankDetailsActivity.class);
        starter.putExtra(TITLE, title);
        starter.putExtra(ID, id);
        starter.putExtra(MONTH_RANK, monthRank);
        starter.putExtra(TOTAL_RANK, totalRank);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rankdetails;
    }

    @Override
    public void initToolBar() {
        String title = getIntent().getStringExtra(TITLE);
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, title,
                "", false, false);
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
        List<String> titles = new ArrayList<>();
        String id = getIntent().getStringExtra(ID);
        String month_rank = getIntent().getStringExtra(MONTH_RANK);
        String total_rank = getIntent().getStringExtra(TOTAL_RANK);
        List<Fragment> fragments = new ArrayList<>();
        RankDetailsFragment fragment1 = new RankDetailsFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("id", id);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);
        titles.add("周榜");


        Log.e(TAG, "initView: "+month_rank);
        if (month_rank != null) {
            RankDetailsFragment fragment2 = new RankDetailsFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putString("id", month_rank);
            fragment2.setArguments(bundle2);
            fragments.add(fragment2);
            titles.add("月榜");
        }

        if (total_rank != null) {
            RankDetailsFragment fragment3 = new RankDetailsFragment();
            Bundle bundle3 = new Bundle();
            bundle3.putString("id", total_rank);
            fragment3.setArguments(bundle3);
            fragments.add(fragment3);
            titles.add("总榜");
        }

        BookRankVpAdapter adapter = new BookRankVpAdapter(getSupportFragmentManager(), titles, fragments);
        vpRank.setAdapter(adapter);
        tlTitle.setupWithViewPager(vpRank);
    }

    @Override
    public void initEvent() {

    }
}
