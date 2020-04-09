package com.zhai.shuyangwx.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.application.app;
import com.zhai.shuyangwx.bean.VideoSeekBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.http.VideoDataManager;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.TopMenuHeader;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhaiyang on 2018/6/7.
 * 视频搜索页
 */

public class SeekVideoActivity extends BaseActivity {
    @BindView(R.id.seekvideo_listview)
    ListView listView;
    @BindView(R.id.seekvideo_btn)
    Button button;
    @BindView(R.id.seekvideo_ed)
    EditText editText;
    @BindView(R.id.seekVideo_layout)
    LinearLayout linearLayout;
    MyAdapter myAdapter;
    String req;
    VideoSeekBean videoSeekBeen;

    private String type = "-1";
    @Override
    public int getLayoutId() {
        return R.layout.activity_seekvideo;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "视频搜索",
                "", false, false);
        //标题栏点击事件，get相应控件
        topMenu.getTopIvLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initView() {
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        type = this.getIntent().getStringExtra("type");
        if(type!=null&&!type.equals(-1)){
            linearLayout.setVisibility(View.GONE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initEvent() {
        if (!Tool.isWifiActive(this)) {
            toast("当前不是wifi网络，请注意流量");
        }
        editText.addTextChangedListener(warcher);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tool.isNetworkConnected(SeekVideoActivity.this)) {
                    toast("无网络连接");
                    return;
                }
                final String str = editText.getText().toString();
                if (str.isEmpty()) {
                    finish();
                } else {
                    showWaitingDialog("搜索中...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            req = VideoDataManager.getVideBean(str);
                            handler.sendEmptyMessage(0x123);
                        }
                    }).start();
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SeekVideoActivity.this, PlayActivty.class);
                intent.putExtra("id", videoSeekBeen.getData().get(i).getVod_id() + "");
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });
        if(type!=null&&!type.equals(-1)){
            //设置分类列表获取和适配
            DataManager.getInstance().getXXSeekJson(new ProgressSubscriber<VideoSeekBean>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onNext(Object o) {
                    videoSeekBeen = (VideoSeekBean) o;
                    myAdapter.setData(videoSeekBeen);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    toast("服务器君好像出现了问题呀！");
                }
            },SeekVideoActivity.this,"加载中..."),app.xxUrl,type,app.xxKM,Tool.getTime_miao()/1000+"");
        }
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disWaitingDialog();
            String str = req;
            Log.d("接受数据", str);
            if (str != null) {
                Gson gson = new Gson();
                videoSeekBeen = gson.fromJson(str, new TypeToken<VideoSeekBean>() {
                }.getType());
                myAdapter.setData(videoSeekBeen);
            }
        }
    };
    TextWatcher warcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String str = editText.getText().toString();
            if (!str.isEmpty()) {
                button.setText("搜索");
            } else {
                button.setText("返回");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    class MyAdapter extends BaseAdapter {
        VideoSeekBean seekBean;

        public void setData(VideoSeekBean seekBean) {
            this.seekBean = seekBean;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return seekBean == null ? 0 : seekBean.getData().size();
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
            View view = View.inflate(SeekVideoActivity.this, R.layout.item_seekvideo, null);
            ImageView imageView = view.findViewById(R.id.item_seekvideo_img);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = getZmKuan()/4;
            layoutParams.height = getZmGao()/6;
            imageView.setLayoutParams(layoutParams);
            TextView name = view.findViewById(R.id.item_seekvideo_name);
            TextView cont = view.findViewById(R.id.item_seekvideo_cont);
            name.setText(seekBean.getData().get(position).getName());
            cont.setText(seekBean.getData().get(position).getMsg());
            String img = seekBean.getData().get(position).getImg();
            if (!img.contains("http")) {
                img = app.url+img;
            }
            Log.d("图片地址", img);
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.imag)
                    .error(R.mipmap.imag)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(200, 100);
            Glide.with(SeekVideoActivity.this)
                    .load(img)
                    .apply(options)
                    .into(imageView);
            return view;
        }
    }

    /**
     * 跳转到此页面
     */
    public static void starActivity(Context context, String type){
        Intent starter = new Intent(context, SeekVideoActivity.class);
        starter.putExtra("type",type);
        context.startActivity(starter);
    }
}
