package com.shushijuhe.shushijuheread.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PageItemClickListener;
import me.crosswall.lib.coverflow.core.PagerContainer;

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
    PagerContainer pagerContainer;
    @BindView(R.id.book_store_overlap_pager)
    ViewPager viewPager;
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
                }
                initBanner();
            }
        }, getContext(), "搜索中..."), bean.male.get(i)._id);
    }

    private void initBanner() {
        viewPager.setAdapter(new PageAdapter());
        // 设置数据
        new CoverFlow.Builder()
                .with(viewPager)
                .pagerMargin(0f)
                .scale(0.3f)
                .spaceSize(0f)
                .rotationY(0f)
                .build();
        pagerContainer.setPageItemClickListener(new PageItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(context,"position:" + position,Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    class PageAdapter extends PagerAdapter {
        ImageView imageView;
        @Override
        public int getCount() {
            toast(imageUrls.size()+"");
            return imageUrls!=null&&imageUrls.size()>0?imageUrls.size():0;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = View.inflate(getActivity(),R.layout.item_bookstore,null);
            imageView = view.findViewById(R.id.item_bookstore_img);
            Glide.with(getActivity())
                    .load(imageUrls.get(position))
                    .into(imageView);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }
    }
}
