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
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 书架适配器
 */
public class BookrackAdapter extends RecyclerView.Adapter<BookrackAdapter.MyViewHolder> {

    private Context context;
    private List<BookshelfBean> bookshelfList;
    private List<List<BookMixATocLocalBean>> bookMixATocLocalList;
    private OnItemClickListener onItemClickListener;
    private BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils;


    public interface OnItemClickListener {
        void onItemClick(BookshelfBean bookshelfBea, ImageView imageView, List<BookMixATocLocalBean> bookMixATocLocalList);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BookrackAdapter(Context context) {
        this.context = context;
        bookMixATocLocalBeanDaoUtils = new BookMixATocLocalBeanDaoUtils(context);
        bookMixATocLocalList = new ArrayList<>();
    }

    public void setData(List<BookshelfBean> list) {
        this.bookshelfList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookrack_shelf, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(String.valueOf(bookshelfList.get(position).getName()));
        holder.chapter.setText(String.valueOf("最新：" + getChapter(bookshelfList.get(position).getBookId(),position)));
        holder.time.setText(String.valueOf(bookshelfList.get(position).getTime()));
        Glide.with(context)
                .load(bookshelfList.get(position).getCover())
                .into(holder.cover);
        holder.mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(bookshelfList.get(position), null,bookMixATocLocalList.get(position));
            }
        });
        holder.mlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemClick(bookshelfList.get(position), holder.selected,null);
                return true;
            }
        });

    }

    /**
     * 获取最新章节
     *
     * @param bookId
     */
    private String getChapter(String bookId,int i) {
        List<BookMixATocLocalBean> bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bookId);//根据书籍id查询最新章节
        bookMixATocLocalList.add(bookMixATocLocalBeans);
        bookMixATocLocalBeanDaoUtils.closeConnection();//关闭数据库
        return bookMixATocLocalList.get(i).get(bookMixATocLocalList.get(i).size()-1).getTitle();
    }

    @Override
    public int getItemCount() {
        return bookshelfList.size();
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
