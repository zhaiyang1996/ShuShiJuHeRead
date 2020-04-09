package com.zhai.shuyangwx.activity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.base.BaseActivity;
import com.zhai.shuyangwx.adapter.DetailsAdapter;
import com.zhai.shuyangwx.bean.AutoComplete;
import com.zhai.shuyangwx.bean.Book_infoBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.utils.SpacesItemDecoration;
import com.zhai.shuyangwx.utils.Tool;
import com.zhai.shuyangwx.utils.TopMenuHeader;

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
    @BindView(R.id.seekbook_rv_result)
    RecyclerView rvResult;



    private DetailsAdapter detailsAdapter;
    List record;
    private List<String> strings;
    private ArrayAdapter seekAdapter;
    private ListAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_seekbook;
    }

    @Override
    public void initToolBar() {
        // 顶部设置
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "搜索",
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
        record = Tool.getJiLu();
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

        detailsAdapter = new DetailsAdapter(this, 0);
        rvResult.setAdapter(detailsAdapter);
        rvResult.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvResult.addItemDecoration(new SpacesItemDecoration(this));
        View viewById = acTextView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        if (viewById != null) {
            viewById.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (rvResult.getVisibility()==View.INVISIBLE){
            rvResult.setVisibility(View.GONE);
        }else{
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 搜索记录查询
     */
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub
            TextView sousuojilu = (TextView) arg1.findViewById(R.id.item_seek_record);
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
            listView_seek.setVisibility(View.VISIBLE);
            if (a != null && !"".equals(a)) {
                button.setText("搜索");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // 执行搜索记录保存方法
                        acTextView.setFocusable(false);
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
            if (rvResult.getVisibility() == View.VISIBLE) {
                rvResult.setVisibility(View.GONE);
                listView_seek.setVisibility(View.VISIBLE);
            }
            return false;
        }
    };

    View.OnClickListener l1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.seekbook_btn1:
                    starSeekActvivty("绝世唐门");
                    break;
                case R.id.seekbook_btn2:
                    starSeekActvivty("大主宰");
                    break;
                case R.id.seekbook_btn3:
                    starSeekActvivty("傲世九重天");
                    break;
                case R.id.seekbook_btn4:
                    starSeekActvivty("龙王传说");
                    break;
                case R.id.seekbook_btn5:
                    starSeekActvivty("凡人修仙传");
                    break;
                case R.id.seekbook_btn6:
                    starSeekActvivty("斗破苍穹");
                    break;
                case R.id.seekbook_btn7:
                    starSeekActvivty("人族训练场");
                    break;
                case R.id.seekbook_btn8:
                    starSeekActvivty("全职法师");
                    break;
                case R.id.seekbook_btn9:
                    starSeekActvivty("遮天");
                    break;
                default:
                    break;
            }
        }
    };

    public void starSeekActvivty(String a) {
        Tool.setJiLu(SeekBookActivity.this, a);
        listView_seek.setVisibility(View.GONE);
        rvResult.setVisibility(View.VISIBLE);
        record = Tool.getJiLu();
        adapter = new SimpleAdapter(this, record,
                R.layout.item_seek_record, new String[]{"jilu"},
                new int[]{R.id.item_seek_record});
        if (record != null) {
            Collections.reverse(record);
            listView.setAdapter(adapter);
        }
        setData(a);
//        SeekBookActivity.this.finish();
    }

    private void setData(String seek) {
        DataManager.getInstance().getBook_info(new ProgressSubscriber<Book_infoBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                detailsAdapter.setBean(o);
            }
        }, this, "正在搜索中..."), seek);
    }
}
