package com.zhai.shuyangwx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.bean.Rank_categoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 唐鹏
 * 书籍排行展示适配器
 */
public class BookRankAdapter extends RecyclerView.Adapter<BookRankAdapter.MyViewHolder> {

    private Context context;
    private List<Rank_categoryBean.MaleBean> list;
    private MyOnClickListener myOnClickListener;

    public BookRankAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<Rank_categoryBean.MaleBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_book_rank, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_title.setText(list.get(position).title);
        if (list.get(position).custom_cover!=-1) {
            holder.iv_cover.setBackgroundResource(list.get(position).custom_cover);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.iv_cover.getLayoutParams();
            layoutParams.height = 0;
            layoutParams.width = 5;
            holder.iv_cover.setLayoutParams(layoutParams);
        }
        holder.ll_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClickListener.myOnClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setMyOnClickListener(MyOnClickListener myOnClickListener) {
        this.myOnClickListener = myOnClickListener;
    }

    public interface MyOnClickListener {
        void myOnClick(Rank_categoryBean.MaleBean maleBean);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_cover;
        TextView tv_title;
        LinearLayout ll_click;

        MyViewHolder(View itemView) {
            super(itemView);

            iv_cover = itemView.findViewById(R.id.item_book_rank_cover);
            tv_title = itemView.findViewById(R.id.item_book_rank_title);
            ll_click = itemView.findViewById(R.id.item_book_rank_click);
        }
    }
}
