package com.shushijuhe.shushijuheread.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;


/**
 * Created by zhaiyang on 2018/6/8.
 */

public class VideoListEngineAdapter extends BaseAdapter {
    String[] strings;
    Context context;
    public void setData(String[] strings){
        this.strings=strings;
        notifyDataSetChanged();
    }
    public VideoListEngineAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return strings==null?0:strings.length;
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
        View view = View.inflate(context, R.layout.item_video_engine,null);
        ImageView play_img = view.findViewById(R.id.item_video_engine_img);
        TextView engineView = view.findViewById(R.id.item_video_engine_text);
        String engine=strings[position];
        String str = "";
        if(engine.equals("qq")){
            play_img.setImageResource(R.mipmap.ic_logo_qq);
            engineView.setText("腾讯视频"+str);
        }else if(engine.equals("iqiyi")){
            play_img.setImageResource(R.mipmap.ic_logo_iqiyi);
            engineView.setText("爱奇艺"+str);
        }else if(engine.equals("mgtv")){
            play_img.setImageResource(R.mipmap.ic_logo_mgtv);
            engineView.setText("芒果TV"+str);
        }else if(engine.equals("youku")){
            play_img.setImageResource(R.mipmap.ic_logo_youku);
            engineView.setText("优酷"+str);
        }else if(engine.equals("tudou")){
            play_img.setImageResource(R.mipmap.ic_logo_tudou);
            engineView.setText("土豆"+str);
        }else if(engine.equals("letv")){
            play_img.setImageResource(R.mipmap.ic_logo_letv);
            engineView.setText("乐视TV"+str);
        }else if(engine.equals("pptv")){
            play_img.setImageResource(R.mipmap.ic_logo_pp);
            engineView.setText("PPTV"+str);
        }else if(engine.equals("sohu")){
            play_img.setImageResource(R.mipmap.ic_logo_sohu);
            engineView.setText("搜狐视频"+str);
        }else if(engine.equals("tm")) {
            play_img.setImageResource(R.mipmap.ic_logo_tm);
            engineView.setText("TM视频" + str);
        }else if(engine.equals("fengxing")){
            play_img.setImageResource(R.mipmap.ic_logo_fengxing);
            engineView.setText("风行视频" + str);
        }else if(engine.equals("cntv")){
            play_img.setImageResource(R.mipmap.ic_logo_cntv);
            engineView.setText("央视影音"+str);
        }
        return view;
    }
}
