package com.shushijuhe.shushijuheread.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.bean.Categories_infoBean;
import com.shushijuhe.shushijuheread.constants.Constants;
import com.shushijuhe.shushijuheread.utils.BookDialog;
import com.shushijuhe.shushijuheread.utils.Tool;

import java.text.DecimalFormat;

/**
 * Created by Boy on 2018/6/8.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyViewHolder> {

    private Categories_infoBean bean;
    private Context context;

    public DetailsAdapter(Context context) {
        this.context = context;
    }

    public void setBean(Categories_infoBean bean) {
        this.bean = bean;
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
        if (position==0){
            holder.iv_cover.setBackgroundResource(R.mipmap.ic_launcher);
            int[] wh = Tool.getWHDP(context);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_cover.getLayoutParams();
            layoutParams.width = (wh[0] / 2 - 20);
            layoutParams.height = (wh[0] / 4 * 3 - 20)/2;
            return;
        }
        final String title = bean.books.get(position-1).title;
        final String author = bean.books.get(position-1).author;
        final String lastChapter = bean.books.get(position-1).lastChapter;
        float i_latelyFollower = (float) bean.books.get(position-1).latelyFollower;
        String latelyFollower;
        DecimalFormat fnum = new DecimalFormat("##0.0");
        if (i_latelyFollower >= 10000) {
            i_latelyFollower = (i_latelyFollower / 10000f);
            latelyFollower = fnum.format(i_latelyFollower) + "万";
        } else {
            latelyFollower = (int) i_latelyFollower + "";
        }
        final String shortIntro = bean.books.get(position-1).shortIntro;
        final String cover = Constants.IMG_BASE_URL + bean.books.get(position-1).cover;
        final String retentionRatio = bean.books.get(position-1).retentionRatio + "%";
        final String tags = bean.books.get(position-1).majorCate;
        Glide.with(context)
                .load(Constants.IMG_BASE_URL + bean.books.get(position-1).cover)
                .into(holder.iv_cover);
        int[] wh = Tool.getWHDP(context);
//        int i = (int) (Math.random()*70+1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.iv_cover.getLayoutParams();
        layoutParams.width = (wh[0] / 2 - 20);
        layoutParams.height = (wh[0] / 4 * 3 - 20);
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
    }

    @Override
    public int getItemCount() {
        return bean == null ? 0 : bean.books.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cover;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.books_cover);
        }
    }
}
