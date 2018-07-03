package com.shushijuhe.shushijuheread.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.bean.VideoSeekBean;
import com.shushijuhe.shushijuheread.http.VideoDataManager;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

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
    MyAdapter myAdapter;
    String req;
    List<VideoSeekBean> videoSeekBeen;

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
                            req = VideoDataManager.getVideBean("search", str);
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
                intent.putExtra("id", videoSeekBeen.get(i).getUrl() + "");
                startActivity(intent);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disWaitingDialog();
            String str = Tool.unicode2String(req);
            if (str != null) {
                Gson gson = new Gson();
                try {
                    JSONArray jsonObject = new JSONArray(str);
                    if (jsonObject != null) {
                        videoSeekBeen = gson.fromJson(str, new TypeToken<List<VideoSeekBean>>() {
                        }.getType());
                        myAdapter.setData(videoSeekBeen);
                    } else {
                        String Message = jsonObject.getString(1);
                        throw new RuntimeException(Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

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
        List<VideoSeekBean> seekBean;

        public void setData(List<VideoSeekBean> seekBean) {
            this.seekBean = seekBean;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return seekBean == null ? 0 : seekBean.size();
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
            TextView name = view.findViewById(R.id.item_seekvideo_name);
            TextView cont = view.findViewById(R.id.item_seekvideo_cont);
            name.setText(seekBean.get(position).getName());
            cont.setText(seekBean.get(position).getMsg());
            Glide.with(SeekVideoActivity.this)
                    .load(seekBean.get(position).getImg())
                    .into(imageView);
            return view;
        }
    }
}
