package com.shushijuhe.shushijuheread;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.allenliu.versionchecklib.callback.OnCancelListener;
import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.allenliu.versionchecklib.v2.callback.RequestVersionListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shushijuhe.shushijuheread.activity.SeekBookActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.ViewPagerAdapter;
import com.shushijuhe.shushijuheread.bean.UpAppBean;
import com.shushijuhe.shushijuheread.fragment.BookrackFragment;
import com.shushijuhe.shushijuheread.fragment.BookstoreFragment;
import com.shushijuhe.shushijuheread.fragment.FindFragment;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主类，碎片
 */
public class MainActivity extends BaseActivity{

    @BindView(R.id.main_vp_fragment)
    ViewPager vpFragment;
    @BindView(R.id.main_tl_title)
    TabLayout tlTitle;
    @BindView(R.id.main_drawer)
    DrawerLayout drawerLayout;
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
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        topMenu.getTopIvRight().setImageResource(R.mipmap.title_seek);
        topMenu.getTopIvLeft().setImageResource(R.mipmap.title_menu);
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
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                toast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initEvent() {
        DownloadBuilder builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl("http://47.106.114.175/upApp.json")
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        UpAppBean upAppBean = null;
                        Type type =  new TypeToken<UpAppBean>(){}.getType();
                        upAppBean =new Gson().fromJson(result, type);
                        if(upAppBean!=null){
                            if(!Tool.getVerName(mContext).equals(upAppBean.getEdition())){
                                UIData uiData = UIData
                                        .create()
                                        .setDownloadUrl(upAppBean.getUrl())
                                        .setTitle("版本更新："+upAppBean.getSize())
                                        .setContent(upAppBean.getMsg());
                                return uiData;
                            }else{
                                return null;
                            }
                        }else{
                            return null;
                        }
                    }
                    @Override
                    public void onRequestVersionFailure(String message) {

                    }
                });
        builder.excuteMission(mContext);
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                MainActivity.this.finish();
            }
        });
    }
}
