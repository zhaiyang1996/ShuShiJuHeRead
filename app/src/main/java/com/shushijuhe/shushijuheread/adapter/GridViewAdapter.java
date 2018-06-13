package com.shushijuhe.shushijuheread.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;


/**
 * Created by zhaiyang on 2018/6/8.
 */

public class GridViewAdapter extends BaseAdapter{
    private int count;
    private int collect;
    private Context context;
    public void setData(Context context,int count,int collect){
        this.count = count;
        this.collect = collect;
        this.context = context;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_play_grid,null);
        TextView textView = view.findViewById(R.id.item_playgrid_text);
        textView.setText((position+1)+"");
        if ((position+1) == collect){
            textView.setTextColor(Color.BLUE);
            textView.setBackgroundResource(R.drawable.grid_blue);
        }
        return view;
    }
}
