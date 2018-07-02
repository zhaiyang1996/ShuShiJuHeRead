package com.shushijuhe.shushijuheread.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.BooksDetailsActivity;
import com.shushijuhe.shushijuheread.bean.Book_infoBean;
import com.shushijuhe.shushijuheread.bean.Categories_infoBean;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.utils.BookDialog;
import com.shushijuhe.shushijuheread.utils.Tool;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Boy on 2018/6/8.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private List<Categories_infoBean.BooksBean> cbean;
    private int type;
    private Book_infoBean bbean;
    private Context context;

    public DetailsAdapter(Activity context, int type) {
        this.context = context;
        this.type = type;
    }

    public void setBean(Object bean) {
        if (type == 1) {
            this.cbean = (List<Categories_infoBean.BooksBean>) bean;
        } else {
            this.bbean = (Book_infoBean) bean;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_books, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        int[] wh = Tool.getWHDP(context);
        if (position == 0) {
            holder.tv_tip.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tv_tip.getLayoutParams();
            layoutParams.width = (wh[0] / 2 - 20);
            layoutParams.height = 300;
            holder.tv_tip.setLayoutParams(layoutParams);
        } else {
            holder.tv_tip.setVisibility(View.GONE);
        }
        if (type == 1) {//书籍详情
            final String title = cbean.get(position).title;
            final String author = cbean.get(position).author;
            final String lastChapter = cbean.get(position).lastChapter;
            float i_latelyFollower = (float) cbean.get(position).latelyFollower;
            final String bookid = cbean.get(position)._id;
            final String bookmsg = cbean.get(position).shortIntro;
            String latelyFollower;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            if (i_latelyFollower >= 10000) {
                i_latelyFollower = (i_latelyFollower / 10000f);
                latelyFollower = fnum.format(i_latelyFollower) + "万";
            } else {
                latelyFollower = (int) i_latelyFollower + "";
            }
            final String shortIntro = cbean.get(position).shortIntro;
            final String cover = Constants.IMG_BASE_URL + cbean.get(position).cover;
            final String retentionRatio = cbean.get(position).retentionRatio + "%";
            final String tags = cbean.get(position).majorCate;
            Glide.with(context)
                    .load(Constants.IMG_BASE_URL + cbean.get(position).cover)
                    .into(holder.iv_cover);
            //图片大小
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_cover.getLayoutParams();
            layoutParams.width = ((wh[0] >> 1) - 20);//>>1为向右移一位，即除以2
            layoutParams.height = ((wh[0] >> 2) * 3 - 20);
            holder.iv_cover.setLayoutParams(layoutParams);

            final String finalLatelyFollower = latelyFollower;
            holder.iv_cover.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new BookDialog(context, R.style.dialog, new BookDialog.OnCloseListener() {
                        @Override
                        public void onClickCancel(Dialog dialog, boolean confirm) {

                        }
                    }, title, author, lastChapter, finalLatelyFollower, shortIntro, cover, retentionRatio, tags).show();
                    return false;
                }
            });
            //设置单击事件
            holder.iv_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BooksDetailsActivity.starActivity(context,bookid,bookmsg);
                }
            });


            //文字大小
            RelativeLayout.LayoutParams layoutParams_tv = (RelativeLayout.LayoutParams) holder.tv_name.getLayoutParams();
            layoutParams_tv.width = ((wh[0] >> 1) - 20);//>>1为向右移一位，即除以2
            layoutParams_tv.height = 80;
            holder.tv_name.setLayoutParams(layoutParams_tv);
            holder.tv_name.setText(title);
        } else {//搜索结果

            final String title = bbean.books.get(position).title;
            final String author = bbean.books.get(position).author;
            final String lastChapter = bbean.books.get(position).lastChapter;
            float i_latelyFollower = (float) bbean.books.get(position).latelyFollower;
            String latelyFollower;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            if (i_latelyFollower >= 10000) {
                i_latelyFollower = (i_latelyFollower / 10000f);
                latelyFollower = fnum.format(i_latelyFollower) + "万";
            } else {
                latelyFollower = (int) i_latelyFollower + "";
            }
            final String shortIntro = bbean.books.get(position).shortIntro;
            final String cover = Constants.IMG_BASE_URL + bbean.books.get(position).cover;
            final String retentionRatio = bbean.books.get(position).retentionRatio + "%";
            final String tags = bbean.books.get(position).cat;
            Glide.with(context)
                    .load(Constants.IMG_BASE_URL + bbean.books.get(position).cover)
                    .into(holder.iv_cover);
            //图片大小
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_cover.getLayoutParams();
            layoutParams.width = ((wh[0] >> 1) - 20);//>>1为向右移一位，即除以2
            layoutParams.height = ((wh[0] >> 2) * 3 - 20);
            holder.iv_cover.setLayoutParams(layoutParams);

            final String finalLatelyFollower = latelyFollower;
            holder.iv_cover.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new BookDialog(context, R.style.dialog, new BookDialog.OnCloseListener() {
                        @Override
                        public void onClickCancel(Dialog dialog, boolean confirm) {

                        }
                    }, title, author, lastChapter, finalLatelyFollower, shortIntro, cover, retentionRatio, tags).show();
                    return false;
                }
            });

            holder.iv_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BooksDetailsActivity.starActivity(context,
                            bbean.books.get(position)._id,
                            bbean.books.get(position).shortIntro);
                }
            });


            //文字大小
            RelativeLayout.LayoutParams layoutParams_tv = (RelativeLayout.LayoutParams) holder.tv_name.getLayoutParams();
            layoutParams_tv.width = ((wh[0] >> 1) - 20);//>>1为向右移一位，即除以2
            layoutParams_tv.height = 80;
            holder.tv_name.setLayoutParams(layoutParams_tv);
            holder.tv_name.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if (type == 1) {
            count = cbean == null ? 0 : cbean.size();
        } else {
            count = bbean == null ? 0 : bbean.books.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_cover;
        private TextView tv_name, tv_tip;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.books_cover);
            tv_name = itemView.findViewById(R.id.books_name);
            tv_tip = itemView.findViewById(R.id.books_tip);
        }
    }
}
