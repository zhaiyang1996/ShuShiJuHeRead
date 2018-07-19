package com.shushijuhe.shushijuheread.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private boolean isbatch = false;


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
        bookshelfList = new ArrayList<>();
    }

    public void setData(List<BookshelfBean> list,boolean isbatch) {
        this.isbatch = isbatch;
        if(this.bookshelfList!=null){
            this.bookshelfList.clear();
            this.bookMixATocLocalList.clear();
        }
        this.bookshelfList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bookrack_shelf, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if(bookshelfList.get(position).getIsUpdate()){
            holder.isUpdate.setVisibility(View.VISIBLE);
        }else{
            holder.isUpdate.setVisibility(View.GONE);
        }
        holder.name.setText(String.valueOf(bookshelfList.get(position).getName()));
        holder.chapter.setText(String.valueOf("最新：" + getChapter(bookshelfList.get(position).getBookId(),position)));
        holder.time.setText(String.valueOf(bookshelfList.get(position).getTime()));
        Resources res = context.getResources();
        Drawable drawable;
        if(bookshelfList.get(position).getIsEnd()){
            drawable = res.getDrawable(R.mipmap.bookrack_end);
        }else{
            drawable = res.getDrawable(R.mipmap.bookrack_serial);
        }
        holder.isEnd.setImageDrawable(drawable);
        if(bookshelfList.get(position).getIsChecked()){
            drawable = res.getDrawable(R.mipmap.batch_2);
        }else{
            drawable = res.getDrawable(R.mipmap.batch_1);
        }
        holder.isBatch.setImageDrawable(drawable);
        if(isbatch){
            holder.isBatch.setVisibility(View.VISIBLE);
        }else{
            holder.isBatch.setVisibility(View.GONE);
        }
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
        return bookshelfList==null&&bookshelfList.size()<=0?0:bookshelfList.size();
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
        RelativeLayout mlayout;//item
        @BindView(R.id.item_bookrack_dot)
        ImageView isUpdate; //章节是否更新
        @BindView(R.id.item_bookrack_update)
        ImageView isEnd; //章节是否完结
        @BindView(R.id.item_bookrack_isbatch)
        ImageView isBatch; //是否展示多选

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
