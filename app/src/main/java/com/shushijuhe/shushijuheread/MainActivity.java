package com.shushijuhe.shushijuheread;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;
import com.shushijuhe.shushijuheread.activity.SeekVideoActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.bean.CategoriesBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import butterknife.BindView;

/**
 * 主类，碎片
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.main_btn_1)
    Button button1;
    @BindView(R.id.main_btn_2)
    Button button2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "",
                "书视聚合", -1, -1);
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
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    public void getBook(){
        DataManager.getInstance().getCategories(new ProgressSubscriber<CategoriesBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                CategoriesBean categoriesBean = (CategoriesBean) o;
            }
        },this,"数据加载中..."));
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.main_btn_1:
                intent = new Intent(this,BookCategoryActivity.class);
                break;
            case R.id.main_btn_2:
                intent = new Intent(this,SeekVideoActivity.class);
                break;
        }
        startActivity(intent);
    }
}
