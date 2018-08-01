package com.shushijuhe.shushijuheread.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;
import com.shushijuhe.shushijuheread.activity.BookRankActivity;
import com.shushijuhe.shushijuheread.bean.RankBean;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * 唐鹏
 * 书城页面
 */
public class BookstoreFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.book_store_tv_category)
    TextView tvCategory;
    @BindView(R.id.book_store_tv_rank)
    TextView tvRank;
    @BindView(R.id.book_store_mzbanner_recommend)
    MZBannerView mzbannerRecommend;
    private Context context;
    private List<String> imageUrls;
    private List<String> titleStr;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bookstore;
    }

    @Override
    public void initToolBar() {

    }

    public void initView() {
        tvCategory.setOnClickListener(this);
        tvRank.setOnClickListener(this);
        initBannerData();
    }

    private void initBannerData() {
        imageUrls = new ArrayList<>();
        titleStr = new ArrayList<>();
        DataManager.getInstance().getRank_category(new ProgressSubscriber<Rank_categoryBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                Rank_categoryBean bean = (Rank_categoryBean) o;
                setRandomData(bean);
            }
        }, context, "正在查找数据......"));
    }

    private void setRandomData(Rank_categoryBean bean) {
        Random rand = new Random();
        int i = rand.nextInt(bean.male.size());
        DataManager.getInstance().getRank(new ProgressSubscriber<RankBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                RankBean bean = (RankBean) o;
                int a = 10;
                if (a >= bean.ranking.books.size()) {
                    a = bean.ranking.books.size();
                }
                for (int i = 0; i < a; i++) {
                    imageUrls.add(Constants.IMG_BASE_URL + bean.ranking.books.get(i).cover);
                    titleStr.add(bean.ranking.books.get(i).title);
                    initBanner();
                }
            }
        }, getContext(), "搜索中..."), bean.male.get(i)._id);
    }

    private void initBanner() {
        //https://github.com/pinguo-zhouwei/MZBannerView
        // 设置数据
        mzbannerRecommend.setPages(imageUrls, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onClick(View v) {
        if (v == tvCategory) {
            BookCategoryActivity.start(context);
        } else if (v == tvRank) {
            BookRankActivity.start(context);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        mzbannerRecommend.start();//开始轮播
    }

    @Override
    public void onPause() {
        super.onPause();
        mzbannerRecommend.pause();//暂停轮播
    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            // 数据绑定
            Glide.with(context)
                    .load(data)
                    .into(mImageView);
        }
    }
}
