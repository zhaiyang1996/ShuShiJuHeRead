package com.shushijuhe.shushijuheread.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;
import com.shushijuhe.shushijuheread.activity.BookRankActivity;
import com.shushijuhe.shushijuheread.bean.RankBean;
import com.shushijuhe.shushijuheread.bean.Rank_categoryBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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
    @BindView(R.id.book_store_banner_recommend)
    Banner banner;
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
                for (int i1 = 0; i1 < bean.ranking.books.size(); i1++) {
                    imageUrls.add(bean.ranking.books.get(i1).cover);
                    titleStr.add(bean.ranking.books.get(i1).title);
                    initBanner();
                }
            }
        }, getContext(), "搜索中..."), bean.male.get(i)._id);
    }

    private void initBanner() {
//       设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(imageUrls);
        //设置图片单页显示时间
        banner.setDelayTime(2000);
//        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);//	不显示指示器和标题
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);    //显示圆形指示器
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);    //显示数字指示器
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);    //显示数字指示器和标题
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);    //显示圆形指示器和标题（垂直显示）
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);    //显示圆形指示器和标题（水平显示）
        banner.setBannerTitles(titleStr);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        //设置条目点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

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
}
