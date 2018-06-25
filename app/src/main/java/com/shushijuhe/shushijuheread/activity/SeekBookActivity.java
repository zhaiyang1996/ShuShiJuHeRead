package com.shushijuhe.shushijuheread.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.bean.AutoComplete;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;
import com.shushijuhe.shushijuheread.utils.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhaiyang on 2018/6/5.
 */

public class SeekBookActivity extends BaseActivity {
    @BindView(R.id.seekbook_search)
    SearchView acTextView;
    @BindView(R.id.seekbook_seek_btn)
    Button button;
    @BindView(R.id.seekbook_list_seek)
    ListView listView_seek;
    @BindView(R.id.seekbook_listview)
    ListView listView;
    @BindView(R.id.seekbook_btn)
    Button button_2;
    private List<String> strings;
    private ArrayAdapter seekAdapter;
    private ListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activcity_seekbook;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        //设置极光推送界面
        setJGTJ("书籍搜索界面："+this.getClass().getCanonicalName());
        List record = Tool.getJiLu();
        adapter = new SimpleAdapter(this, record,
                R.layout.item_seek_record, new String[]{"jilu"},
                new int[]{R.id.item_seek_record});
        if (record != null) {
            Collections.reverse(record);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(listener);
        }
        strings = new ArrayList<>();
        seekAdapter = new ArrayAdapter<>(SeekBookActivity.this, android.R.layout.simple_dropdown_item_1line, strings);
        listView_seek.setAdapter(seekAdapter);
        listView_seek.setOnItemClickListener(itemClick);
    }

    /**
     * 搜索记录查询
     */
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            TextView sousuojilu = arg1.findViewById(R.id.item_seek_record);
            String name = sousuojilu.getText().toString();
            starSeekActvivty(name);
        }
    };

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            starSeekActvivty(strings.get(position));
        }
    };

    @Override
    public void initEvent() {
        acTextView.setOnQueryTextListener(watcher);
        acTextView.setIconifiedByDefault(false);
        button.setOnClickListener(onClick);
        button_2.setOnClickListener(onClick);
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seekbook_seek_btn:
                    finish();
                    break;
                case R.id.seekbook_btn:
                    Tool.qingkong();
                    listView.setAdapter(null);
                    break;
            }
        }
    };
    // 注册内容改变事件
    SearchView.OnQueryTextListener watcher = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            final String a = newText;
            if (a != null && !"".equals(a)) {
                button.setText("搜索");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // 执行搜索记录保存方法
                        starSeekActvivty(a);
                    }
                });
                DataManager.getInstance().getAutoComplete(new ProgressSubscriber<AutoComplete>(new SubscriberOnNextListenerInstance() {
                    @Override
                    public void onNext(Object o) {
                        AutoComplete autoComplete = (AutoComplete) o;
                        seekAdapter.clear();
                        seekAdapter.addAll(autoComplete.keywords);
                    }
                }, SeekBookActivity.this, null), a);
            } else {
                if (strings != null) {
                    seekAdapter.clear();
                }
                seekAdapter.notifyDataSetChanged();
                button.setText("返回");
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        SeekBookActivity.this.finish();
                    }
                });

            }
            return false;
        }
    };

    View.OnClickListener l1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // 为推荐导航设置点击事件
            Intent it = new Intent(SeekBookActivity.this, SeekBookActivity.class);
            switch (v.getId()) {
                case R.id.seekbook_btn1:
                    it.putExtra("name", "绝世唐门");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn2:
                    it.putExtra("name", "大主宰");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn3:
                    it.putExtra("name", "傲世九重天");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn4:
                    it.putExtra("name", "龙王传说");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn5:
                    it.putExtra("name", "凡人修仙传");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn6:
                    it.putExtra("name", "斗破苍穹");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn7:
                    it.putExtra("name", "人族训练场");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn8:
                    it.putExtra("name", "全职法师");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                case R.id.seekbook_btn9:
                    it.putExtra("name", "遮天");
                    startActivity(it);
                    SeekBookActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    public void starSeekActvivty(String a) {
        Tool.setJiLu(SeekBookActivity.this, a);
        SeekBookResultActivity.start(this, a);
//        SeekBookActivity.this.finish();
    }
//    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                long arg3) {
//            // TODO Auto-generated method stub
//            TextView sousuojilu = (TextView) arg1.findViewById(R.id.sousuojilu);
//            String name = sousuojilu.getText().toString();
//            Intent it = new Intent(SeekBookActivity.this,SouSuoX.class);
//            it.putExtra("name", name);
//            startActivity(it);
//            SeekBookActivity.this.finish();
//        }
//    };
}
