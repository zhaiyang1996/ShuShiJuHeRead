package com.zhai.shuyangwx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.adapter.BookRankVpAdapter;
import com.zhai.shuyangwx.fragment.RankFragment;
import com.zhai.shuyangwx.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 唐鹏
 * 书籍排行界面
 */
public class BookRankActivity extends BaseActivity {

    public static final String TYPE = "type";
    @BindView(R.id.book_rank_tl_title)
    TabLayout tlTitle;
    @BindView(R.id.book_rank_vp_rank)
    ViewPager vpRank;
    BookRankVpAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, BookRankActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookrank;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "排行榜",
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
        List<Fragment> fragments = new ArrayList<>();

        RankFragment fragment1 = new RankFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putBoolean(TYPE, true);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        RankFragment fragment2 = new RankFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean(TYPE, false);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        List<String> titles = new ArrayList<>();
        titles.add("男生专场");
        titles.add("女生专场");
        adapter = new BookRankVpAdapter(getSupportFragmentManager(), titles, fragments);
        vpRank.setAdapter(adapter);
        tlTitle.setupWithViewPager(vpRank);
    }

    @Override
    public void initEvent() {

    }
}
