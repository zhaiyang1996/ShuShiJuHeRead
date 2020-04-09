package com.zhai.shuyangwx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.CategoryDetailsActivity;
import com.zhai.shuyangwx.activity.SeekVideoActivity;
import com.zhai.shuyangwx.activity.base.VodClassBean;
import com.zhai.shuyangwx.bean.CategoriesBean;

/**
 * 唐鹏
 * 分类详情适配器
 */
public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.MyViewHolder> {

    private Context context;
    private CategoriesBean bean;
    private VodClassBean vodClassBean;
    private int flag = -1;

    public BookCategoryAdapter(Context context) {
        this.context = context;
        bean = new CategoriesBean();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_one, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (flag == 0) {//代表女生分类
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_name.setText(bean.female.get(position).name);
            holder.tv_count.setText(bean.female.get(position).bookCount + "本");
            holder.rl_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryDetailsActivity.start(context);
                    CategoryDetailsActivity.name = bean.female.get(position).name;
                    CategoryDetailsActivity.gender = "female";
                }
            });
        } else if (flag == 1) {//代表男生分类
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_name.setText(bean.male.get(position).name);
            holder.tv_count.setText(bean.male.get(position).bookCount + "本");
            holder.rl_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryDetailsActivity.start(context);
                    CategoryDetailsActivity.name = bean.male.get(position).name;
                    CategoryDetailsActivity.gender = "male";
                }
            });
        }else if(flag == 2){//代表视频
            holder.tv_name.setText(vodClassBean.getData().get(position).getType_name());
            holder.tv_count.setVisibility(View.GONE);
            holder.rl_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeekVideoActivity.starActivity(context,vodClassBean.getData().get(position).getType_id());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (flag == 0) {
            return bean.female != null ? bean.female.size() : 0;
        } else if (flag == 1) {
            return bean.male != null ? bean.male.size() : 0;
        } else if(flag == 2){
            return vodClassBean.getData() != null ? vodClassBean.getData().size() : 0;
        }else{
            return 0;
        }
    }

    public void setList(CategoriesBean bean, int flag) {
        this.bean = bean;
        this.flag = flag;
        //重新刷新数据RecyclerView
        notifyDataSetChanged();
    }
    public void setXXlist(VodClassBean bean,int flag){
        this.vodClassBean = bean;
        this.flag = flag;
        //重新刷新数据RecyclerView
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_bg;
        TextView tv_name, tv_count;

        MyViewHolder(View itemView) {
            super(itemView);
            rl_bg = itemView.findViewById(R.id.item_category_one_rl_bg);
            tv_count = itemView.findViewById(R.id.item_category_one_tv_count);
            tv_name = itemView.findViewById(R.id.item_category_one_tv_name);
        }
    }
}
