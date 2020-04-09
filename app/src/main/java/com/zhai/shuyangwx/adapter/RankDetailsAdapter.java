package com.zhai.shuyangwx.adapter;

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

import com.bumptech.glide.Glide;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.BooksDetailsActivity;
import com.zhai.shuyangwx.bean.RankBean;
import com.zhai.shuyangwx.constants.Constants;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.view.BookDialog;

import java.text.DecimalFormat;

/**
 * 唐鹏 2018/7/16
 * 排行详细信息适配器
 */
public class RankDetailsAdapter extends RecyclerView.Adapter<RankDetailsAdapter.MyViewHolder> {

    private Context context;
    private RankBean bean;

    public RankDetailsAdapter(Context context, RankBean bean) {
        this.context = context;
        this.bean = bean;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_books, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
        final String title = bean.ranking.books.get(position).title;
        final String author = bean.ranking.books.get(position).author;
        float i_latelyFollower = (float) bean.ranking.books.get(position).latelyFollower;
        final String tags = bean.ranking.books.get(position).majorCate;
        final String bookid = bean.ranking.books.get(position)._id;
        final String bookmsg = bean.ranking.books.get(position).shortIntro;
        String latelyFollower;
        DecimalFormat fnum = new DecimalFormat("##0.0");
        if (i_latelyFollower >= 10000) {
            i_latelyFollower = (i_latelyFollower / 10000f);
            latelyFollower = fnum.format(i_latelyFollower) + "万";
        } else {
            latelyFollower = (int) i_latelyFollower + "";
        }
        final String shortIntro = bean.ranking.books.get(position).shortIntro;
        final String cover = Constants.IMG_BASE_URL + bean.ranking.books.get(position).cover;
        final String retentionRatio = bean.ranking.books.get(position).retentionRatio + "%";
        Glide.with(context)
                .load(Constants.IMG_BASE_URL + bean.ranking.books.get(position).cover)
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
                }, title, author, "", finalLatelyFollower, shortIntro, cover, retentionRatio, tags).show();
                return false;
            }
        });
        //设置单击事件
        holder.iv_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BooksDetailsActivity.starActivity(context, bookid, bookmsg);
            }
        });


        //文字大小
        RelativeLayout.LayoutParams layoutParams_tv = (RelativeLayout.LayoutParams) holder.tv_name.getLayoutParams();
        layoutParams_tv.width = ((wh[0] >> 1) - 20);//>>1为向右移一位，即除以2
        layoutParams_tv.height = 80;
        holder.tv_name.setLayoutParams(layoutParams_tv);
        holder.tv_name.setText(title);
    }

    @Override
    public int getItemCount() {
        return bean.ranking.books.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_cover;
        private TextView tv_name, tv_tip;

        MyViewHolder(View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.books_cover);
            tv_name = itemView.findViewById(R.id.books_name);
            tv_tip = itemView.findViewById(R.id.books_tip);
        }
    }
}
