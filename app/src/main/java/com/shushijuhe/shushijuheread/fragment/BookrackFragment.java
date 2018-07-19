package com.shushijuhe.shushijuheread.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.ReadActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.adapter.BookrackAdapter;
import com.shushijuhe.shushijuheread.bean.BookData;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookDataDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.IOUtils;
import com.shushijuhe.shushijuheread.utils.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 刘鹏
 * 书架页面
 */
public class BookrackFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.bookrack_rv_shelf)
    RecyclerView mRecyclerView;//书架
    @BindView(R.id.bookrack_ll_layout)
    LinearLayout linearLayout;//背景图
    @BindView(R.id.bookrack_bt_addbook)
    Button button;//添加书籍
    @BindView(R.id.boorrack_smart)
    RefreshLayout refreshLayout;//下拉刷新
    @BindView(R.id.bookrack_batch)
    RelativeLayout bookrack_batch; //批量操作
    @BindView(R.id.bookrack_text_num)
    TextView bookrack_text_num; //选中数量
    @BindView(R.id.bookrack_del)
    Button bookrack_del; //删除按钮
    @BindView(R.id.bookrack_all)
    Button bookrack_all; //是否全选

    private BookrackAdapter bookrackAdapter;
    private List<BookshelfBean> list;
    private BookshelfBeanDaoUtils bookshelfBeanDaoUtils;
    private BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils;
    private BookDataDaoUtils bookDataDaoUtils;
    private List<BookMixATocLocalBean> bookMixATocLocalBeans;
    private int num = 0;

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
        bookDataDaoUtils = new BookDataDaoUtils(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置布局管理器
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL)); //添加Android自带的分割线
        mRecyclerView.setItemAnimator(new DefaultItemAnimator()); //设置增加或删除条目的动画
        mRecyclerView.setAdapter(bookrackAdapter);//设置Adapter
        initRefresh();
        callBack();
    }

    @Override
    public void initEvent() {
        bookrack_del.setOnClickListener(this);
        bookrack_all.setOnClickListener(this);
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
                        toast("获取最新章节异常，请检查你的网络哟~");
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
                    if(bookrack_batch.getVisibility() == View.GONE){
                        bookrack_batch.setVisibility(View.VISIBLE);
                    }
                    initData();
                } else {
                    if(bookrack_batch.getVisibility() == View.GONE){
                        //更新点击状态
                        BookshelfBean bean = bookshelfBea;
                        bean.setIsUpdate(false);
                        bean.setTime(Tool.getTime());
                        bean.setTimeMillis(System.currentTimeMillis());
                        bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                        ReadActivity.statrActivity((BaseActivity) getActivity(), null,
                                bookMixATocLocalList, bookshelfBea.getName(), 0, 0, false);
                    }else {
                        //执行勾选操作
                        BookshelfBean bean = bookshelfBea;
                        bean.setIsChecked(!bean.getIsChecked());
                        bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                        initData();
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        num = 0;
        refreshLayout.setEnableRefresh(true);
        list = bookshelfBeanDaoUtils.queryBookshelfBeanByQueryBuilder_TimeA();
        bookshelfBeanDaoUtils.closeConnection();//关闭数据库
        if (list.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            boolean b;
            if(bookrack_batch.getVisibility() == View.VISIBLE){
                b = true;
            }else {
                b = false;
            }
            bookrackAdapter.setData(list,b);
            for(BookshelfBean bean:list){
                if(bean.getIsChecked())
                    num++;
            }
            bookrack_text_num.setText("已选中："+num);
            if(num>0){
                bookrack_del.setText("删除");
            }else{
                bookrack_del.setText("退出批量");
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bookrack_del:
                if(bookrack_del.getText().toString().equals("退出批量")){
                    bookrack_batch.setVisibility(View.GONE);
                    //循环取消全选
                    for(BookshelfBean bean:list){
                        bean.setIsChecked(false);
                        bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                    }
                    initData();
                }else{
                    //执行删除方法
                    showDelDiglog();
                }
                break;
            case R.id.bookrack_all:
                if(bookrack_all.getText().toString().equals("全选")){
                    //循环全选
                    for(BookshelfBean bean:list){
                        bean.setIsChecked(true);
                        bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                    }
                    initData();
                    num = list.size();
                    bookrack_all.setText("取消全选");
                }else{
                    //循环取消全选
                    for(BookshelfBean bean:list){
                        bean.setIsChecked(false);
                        bookshelfBeanDaoUtils.updateBookshelfBean(bean);
                    }
                    initData();
                    num = 0;
                    bookrack_all.setText("全选");
                }
                bookrack_text_num.setText("已选中："+num);
                if(num>0){
                    bookrack_del.setText("删除");
                }else{
                    bookrack_del.setText("退出批量");
                }
                break;
        }
    }
    public void showDelDiglog(){
        //打开提示框是否将此书加入书架
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示")
                .setMessage("删除后将不可恢复，是否确认删除这"+num+"本书籍？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //开启删除，将存在于本书中的离线文件一起删除，大数据处理，开启子线程处理任务
                        delData();
                    }
                })
                .setNegativeButton("取消",null);
        builder.show();
    }
    public void delData(){
        showWaitingDialog("书籍删除中...");
        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        //循环拿到被选中的书籍并删除相关参数
                        for(BookshelfBean bean:list)
                        bookshelfBeanDaoUtils.deleteBookshelfBean(bean);
                        sub.onNext("删除成功");
                        for(BookshelfBean bean:list){
                            if(bean.getIsChecked()){
                                //拿出被选中的书籍目录和下载目录（如果有的话）
                                List<BookMixATocLocalBean> bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bean.getBookId());
                                //判断此章节是否被下载，如果下载就删除目录下的文件
                                for(BookMixATocLocalBean bookMixATocLocalBean:bookMixATocLocalBeans){
                                    if(!bookMixATocLocalBean.isOnline){
                                        List<BookData> bookData = bookDataDaoUtils.queryBookDataDaoByQueryBuilder(bookMixATocLocalBean.getBookid(), bookMixATocLocalBean.getTitle());
                                        if(bookData!=null&&bookData.size()>0){
                                            String path = bookData.get(0).getBody();
                                            IOUtils.deleteFile(path);
                                            bookDataDaoUtils.deleteBookData(bookData.get(0));
                                        }
                                    }
                                    bookMixATocLocalBeanDaoUtils.deleteBookMixATocLocalBean(bookMixATocLocalBean);
                                }

                            }
                        }
                    }
                }
        ).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
               //删除后取消dialog，提示用户，并刷新书架
                disWaitingDialog();
                toast(s);
                bookrack_batch.setVisibility(View.GONE);
                initData();
            }

            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }
        };
        myObservable.subscribe(mySubscriber);
    }
}
