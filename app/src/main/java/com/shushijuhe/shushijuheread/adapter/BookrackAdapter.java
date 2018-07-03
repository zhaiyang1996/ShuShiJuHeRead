package com.shushijuhe.shushijuheread.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 书架适配器
 */
public class BookrackAdapter extends RecyclerView.Adapter<BookrackAdapter.MyViewHolder> {

    private Context context;
    private List<BookshelfBean> list;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(BookshelfBean bookshelfBea, ImageView imageView);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BookrackAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<BookshelfBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookrack_shelf, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(String.valueOf(list.get(position).getName()));
        holder.chapter.setText("最新章节：");
        holder.time.setText(String.valueOf(list.get(position).getTime()));
        Glide.with(context)
                .load(list.get(position).getCover())
                .into(holder.cover);
        holder.mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(list.get(position), null);
            }
        });
        holder.mlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemClick(list.get(position), holder.selected);
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_bookrack_name)
        TextView name;//书名
        @BindView(R.id.item_bookrack_chapter)
        TextView chapter;//章节
        @BindView(R.id.item_bookrack_time)
        TextView time;//时间
        @BindView(R.id.item_bookrack_cover)
        ImageView cover;//封面
        @BindView(R.id.item_bookrack_selected)
        ImageView selected;//是否选中
        @BindView(R.id.item_bookrack_linearLayout)
        LinearLayout mlayout;//item


        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
