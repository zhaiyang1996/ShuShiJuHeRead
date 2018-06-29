package com.shushijuhe.shushijuheread.activity;


import android.view.View;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

public class BookCategoryActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "书籍分类",
                "", -1, -1);
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
