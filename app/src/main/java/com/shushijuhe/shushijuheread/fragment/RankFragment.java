package com.shushijuhe.shushijuheread.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.RankDetailsActivity;
import com.shushijuhe.shushijuheread.adapter.BookRankAdapter;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 唐鹏
 * 排行页面
 */
public class RankFragment extends BaseFragment {

    public static final String TYPE = "type";
    @BindView(R.id.femalerank_rv_rank)
    RecyclerView rvRank;
    Context context;
    private List<Integer> cover;
    private BookRankAdapter adapter;
    private Rank_categoryBean bean;
    private boolean type;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_femalerank;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            type = bundle.getBoolean(TYPE,false);
        }
        adapter = new BookRankAdapter(context);
        setCover();
        adapter.setMyOnClickListener(new BookRankAdapter.MyOnClickListener() {
            @Override
            public void myOnClick(Rank_categoryBean.MaleBean maleBean) {
                RankDetailsActivity.start(context, maleBean.title, maleBean._id, maleBean.monthRank, maleBean.totalRank);
            }
        });
        rvRank.setAdapter(adapter);
        rvRank.setLayoutManager(new GridLayoutManager(context, 1));
        getHttp();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getHttp() {
        DataManager.getInstance().getRank_category(new ProgressSubscriber<Rank_categoryBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                bean = (Rank_categoryBean) o;
                for (int i = 0; i < 8; i++) {
                    bean.female.get(i).custom_cover = cover.get(i);
                    bean.male.get(i).custom_cover = cover.get(i);
                }
                if (type){
                    adapter.setList(bean.male);
                }else {
                    adapter.setList(bean.female);
                }
            }
        }, context, "正在查找数据......"));
    }

    private void setCover() {
        cover = new ArrayList<>();
        cover.add(R.mipmap.rank_fire);
        cover.add(R.mipmap.rank_good);
        cover.add(R.mipmap.rank_rocket);
        cover.add(R.mipmap.rank_rank);
        cover.add(R.mipmap.rank_face);
        cover.add(R.mipmap.rank_paint);
        cover.add(R.mipmap.rank_vip);
        cover.add(R.mipmap.rank_trophy);
    }
}
