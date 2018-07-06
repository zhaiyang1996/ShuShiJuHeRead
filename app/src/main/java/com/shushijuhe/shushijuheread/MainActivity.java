package com.shushijuhe.shushijuheread;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shushijuhe.shushijuheread.activity.SeekBookActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.ViewPagerAdapter;
import com.shushijuhe.shushijuheread.fragment.BookrackFragment;
import com.shushijuhe.shushijuheread.fragment.BookstoreFragment;
import com.shushijuhe.shushijuheread.fragment.FindFragment;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主类，碎片
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_vp_fragment)
    ViewPager vpFragment;
    @BindView(R.id.main_tl_title)
    TabLayout tlTitle;
    List<Fragment> fragments;
    ViewPagerAdapter adapter;
    TopMenuHeader topMenu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "",
                "书视聚合", false, true);
        //标题栏点击事件，get相应控件
        topMenu.getTopIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        topMenu.getTopIvRight().setImageResource(R.mipmap.title_seek);
        topMenu.getTopIvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SeekBookActivity.class));
            }
        });
    }

    @Override
    public void initView() {
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        //动态权限获取
        Tool.isGrantExternalRW(this);
        fragments = new ArrayList<>();
        setVpFragment();
        setTabLayout();
    }

    private void setTabLayout() {
        tlTitle.addTab(tlTitle.newTab());
        tlTitle.addTab(tlTitle.newTab());
        tlTitle.addTab(tlTitle.newTab());
        // 使用 TabLayout 和 ViewPager 相关联
        tlTitle.setupWithViewPager(vpFragment);
        // TabLayout指示器添加文本
        tlTitle.getTabAt(0).setText("书架");
        tlTitle.getTabAt(1).setText("书城");
        tlTitle.getTabAt(2).setText("发现");
    }

    public void setVpFragment() {
        fragments.add(new BookrackFragment());
        fragments.add(new BookstoreFragment());
        fragments.add(new FindFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        vpFragment.setAdapter(adapter);
        vpFragment.setCurrentItem(0);
    }

    @Override
    public void initEvent() {
    }
}
