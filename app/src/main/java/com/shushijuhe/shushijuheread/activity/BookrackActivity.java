package com.shushijuhe.shushijuheread.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookrackAdapter;
import com.shushijuhe.shushijuheread.bean.Book_infoBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import butterknife.BindView;

/**
 * 书架
 */
public class BookrackActivity extends BaseActivity {
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView shelf;//书架
    private BookrackAdapter bookrackAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookrack;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "",
                "书架", R.mipmap.title_seek, R.mipmap.title_menu);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        bookrackAdapter = new BookrackAdapter(this);
    }
}
