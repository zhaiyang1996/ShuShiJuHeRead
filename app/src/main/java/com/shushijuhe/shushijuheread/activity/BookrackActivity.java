package com.shushijuhe.shushijuheread.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.shushijuhe.shushijuheread.MainActivity;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookrackAdapter;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.List;

import butterknife.BindView;

/**
 * 刘鹏
 * 书架页面
 */
public class BookrackActivity extends BaseActivity {
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView mRecyclerView;//书架
    private BookrackAdapter bookrackAdapter;
    private List<BookshelfBean> list;
    private BookshelfBeanDaoUtils bookshelfBeanDaoUtils;

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
        topMenu.getTopIvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookrackActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        bookrackAdapter = new BookrackAdapter(this);
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(this);
        initData();
        callBack();
    }

    /**
     * 回调
     */
    private void callBack() {
        bookrackAdapter.setOnItemClickListener(new BookrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookshelfBean bookshelfBea, ImageView imageView) {
                if(imageView!=null){
                    Toast.makeText(mContext, "长按", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "单击", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        list = bookshelfBeanDaoUtils.queryAllBookshelfBean();
        bookshelfBeanDaoUtils.closeConnection();//关闭数据库
        bookrackAdapter.setData(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置布局管理器
        mRecyclerView.setAdapter(bookrackAdapter);//设置Adapter
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置增加或删除条目的动画
    }
}
