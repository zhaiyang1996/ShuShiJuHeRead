package com.shushijuhe.shushijuheread.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.ReadActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookrackAdapter;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * 刘鹏
 * 书架页面
 */
public class BookrackFragment extends BaseFragment {
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView mRecyclerView;//书架
    @BindView(R.id.bookrack_ll_layout)
    LinearLayout linearLayout;//背景图
    @BindView(R.id.bookrack_bt_addbook)
    Button button;//添加书籍
    @BindView(R.id.boorrack_smart)
    RefreshLayout refreshLayout;//下拉刷新
    private BookrackAdapter bookrackAdapter;
    private List<BookshelfBean> list;
    private BookshelfBeanDaoUtils bookshelfBeanDaoUtils;
    private BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils;
    private List<BookMixATocLocalBean> bookMixATocLocalBeans;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookrack;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        bookrackAdapter = new BookrackAdapter(getActivity());
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(getActivity());
        bookMixATocLocalBeanDaoUtils = new BookMixATocLocalBeanDaoUtils(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置布局管理器
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置增加或删除条目的动画
        mRecyclerView.setAdapter(bookrackAdapter);//设置Adapter
        initRefresh();
        callBack();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 下拉刷新
     */
    private void initRefresh() {
        //设置 Header 为 金典 样式
        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
        refreshLayout.setRefreshHeader(new ClassicsHeader(Objects.requireNonNull(getActivity())));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                upDate();
            }
        });
    }
    int page = 0;
    private void upDate(){
        page = 0;
        //创建循环重复获取书籍目录
        for(final BookshelfBean bean:list){
            //获取目录数据
            DataManager.getInstance().getBookMixAToc(new ProgressSubscriber<BookMixAToc>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    BookMixAToc bookMixAToc = (BookMixAToc) o;
                    String id = bean.getBookId();
                    if (bookMixAToc != null) {
                        bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(id);
                        if(bookMixATocLocalBeans.size()<bookMixAToc.mixToc.chapters.size()){
                            int size = bookMixAToc.mixToc.chapters.size() - bookMixATocLocalBeans.size();
                            //在线章节大于本地章节则更新本地章节并且展示小红点
                            List<BookMixATocLocalBean> bookMixATocLocalBeanss = new ArrayList<>();
                            for(int i=0;i<size;i++){
                                BookMixATocLocalBean bookMixATocLocalBean = new BookMixATocLocalBean();
                                bookMixATocLocalBean.setBookid(bookMixAToc.mixToc.book);
                                bookMixATocLocalBean.setIsOnline(true);
                                bookMixATocLocalBean.setLink(bookMixAToc.mixToc.chapters.get(bookMixATocLocalBeans.size()+i).link);
                                bookMixATocLocalBean.setTitle(bookMixAToc.mixToc.chapters.get(bookMixATocLocalBeans.size()+i).title);
                                bookMixATocLocalBeanss.add(bookMixATocLocalBean);
                            }
                            bean.setIsUpdate(true);
                            bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                            bookMixATocLocalBeanDaoUtils.insertMultBookMixATocLocalBean(bookMixATocLocalBeanss);
                        }
                    } else {
                        toast("目录获取异常");
                    }
                    if(page >= list.size()-1){
                        initData();
                        refreshLayout.finishRefresh(2000/*,false*/);
                        //关闭数据库
                        bookshelfBeanDaoUtils.closeConnection();
                        bookMixATocLocalBeanDaoUtils.closeConnection();
                    }
                    page++;
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if(page >= list.size()-1){
                        initData();
                        toast("网络异常，请检查你的网络哟~");
                        refreshLayout.finishRefresh(2000/*,false*/);
                        //关闭数据库
                        bookshelfBeanDaoUtils.closeConnection();
                        bookMixATocLocalBeanDaoUtils.closeConnection();
                    }
                    page++;
                }
            }, getActivity(), null), bean.getBookId(), "chapters");
        }
    }
    /**
     * 回调
     */
    private void callBack() {
        bookrackAdapter.setOnItemClickListener(new BookrackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BookshelfBean bookshelfBea, ImageView imageView, List<BookMixATocLocalBean> bookMixATocLocalList) {
                if (imageView != null) {

                } else {
                    //更新点击状态
                    BookshelfBean bean = bookshelfBea;
                    bean.setIsUpdate(false);
                    bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                    ReadActivity.statrActivity((BaseActivity) getActivity(), null,
                            bookMixATocLocalList, bookshelfBea.getName(), 0, 0, false);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        refreshLayout.setEnableRefresh(true);
        list = bookshelfBeanDaoUtils.queryAllBookshelfBean();
        bookshelfBeanDaoUtils.closeConnection();//关闭数据库
        if (list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            bookrackAdapter.setData(list);
        } else {
            refreshLayout.setEnableRefresh(false);
            mRecyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "敬请期待！", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}
