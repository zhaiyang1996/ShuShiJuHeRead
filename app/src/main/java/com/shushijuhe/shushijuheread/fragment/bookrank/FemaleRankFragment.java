package com.shushijuhe.shushijuheread.fragment.bookrank;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.adapter.BookRankAdapter;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.fragment.BaseFragment;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 唐鹏
 * 女声排行页面
 */
public class FemaleRankFragment extends BaseFragment {


    @BindView(R.id.femalerank_rv_rank)
    RecyclerView rvRank;
    Context context;
    private BookRankAdapter adapter;
    private Rank_categoryBean bean;
    private List<Rank_categoryBean.MaleBean> list;
    private boolean isAll = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_femalerank;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        adapter = new BookRankAdapter(context);
        list = new ArrayList<>();
        adapter.setMyOnClickListener(new BookRankAdapter.MyOnClickListener() {
            @Override
            public void myOnClick(Rank_categoryBean.MaleBean maleBean) {
                if (maleBean.title.equals("更多排行")) {
                    setData(isAll);
                    isAll = !isAll;
                }
            }
        });
        rvRank.setAdapter(adapter);
        rvRank.setLayoutManager(new GridLayoutManager(context, 1));
        getHttp(false);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void setData(boolean mIsAll) {
        list.clear();
        for (int i = 0; i < 8; i++) {
            list.add(bean.female.get(i));
        }
        Rank_categoryBean.MaleBean bean_more = new Rank_categoryBean.MaleBean();
        bean_more.title = "更多排行";
        list.add(bean_more);
        if (mIsAll) {
            for (int i = 8; i < bean.female.size(); i++) {
                list.add(bean.female.get(i));
            }
        }
        adapter.setList(list);
    }

    public void getHttp(final boolean mIsAll) {
        DataManager.getInstance().getRank_category(new ProgressSubscriber<Rank_categoryBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                bean = (Rank_categoryBean) o;
                setData(mIsAll);
            }
        }, context, "正在查找数据......"));
    }
}
