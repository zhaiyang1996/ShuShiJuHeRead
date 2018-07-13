package com.shushijuhe.shushijuheread.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.shushijuhe.shushijuheread.MainActivity;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

/**
 * 关于我们:翟阳 2018/7/13
 */
public class AboutUsActivity extends BaseActivity{
    TopMenuHeader topMenu;
    @Override
    public int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public void initToolBar() {
        topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "关于我们",
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

    }

    @Override
    public void initEvent() {

    }
}
