package com.zhai.shuyangwx.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zhai.shuyangwx.MainActivity;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.TopMenuHeader;

import butterknife.BindView;

/**
 * 关于我们:翟阳 2018/7/13
 */
public class AboutUsActivity extends BaseActivity{
    TopMenuHeader topMenu;
    @BindView(R.id.aboutus_version)
    TextView textView;
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
        textView.setText(Tool.getVerName(this));
    }

    @Override
    public void initEvent() {

    }
}
