package com.shushijuhe.shushijuheread;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shushijuhe.shushijuheread.activity.BookCategoryActivity;
import com.shushijuhe.shushijuheread.activity.ReadActivity;
import com.shushijuhe.shushijuheread.activity.SeekBookActivity;
import com.shushijuhe.shushijuheread.activity.SeekVideoActivity;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.application.app;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.bean.BookshelfBean;
import com.shushijuhe.shushijuheread.dao.BookMixATocLocalBeanDaoUtils;
import com.shushijuhe.shushijuheread.dao.BookshelfBeanDaoUtils;

import com.shushijuhe.shushijuheread.service.DownloadService;
import com.shushijuhe.shushijuheread.utils.Tool;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主类，碎片
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.main_btn_1)
    Button button1;
    @BindView(R.id.main_btn_2)
    Button button2;
    @BindView(R.id.main_btn_3)
    Button button3;
    @BindView(R.id.main_list)
    ListView listView;

    BookshelfBeanDaoUtils bookshelfBeanDaoUtils;
    BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils;
    List<BookshelfBean> bookshelfBeanList;
    List<BookMixATocLocalBean> bookMixATocLocalBeans;
    List<List<BookMixATocLocalBean>> lists;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(true, "",
                "书视聚合", false, false);
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
        Tool.isGrantExternalRW(this);
        //启动下载服务
        startService(new Intent(this,DownloadService.class));
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(this);
        bookMixATocLocalBeanDaoUtils = new BookMixATocLocalBeanDaoUtils(this);
        lists = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(bookMixATocLocalBeans!=null&&bookMixATocLocalBeans.size()>0){
                    ReadActivity.statrActivity(MainActivity.this,null,lists.get(i),bookshelfBeanList.get(i).getName(),0,0,false);
                }else{
                    toast("本地数据异常，删除书籍重新添加吧~");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(lists!=null){
            lists.removeAll(lists);
        }
        if(bookshelfBeanDaoUtils==null)
            return;
        bookshelfBeanList =  bookshelfBeanDaoUtils.queryAllBookshelfBean();
        if(bookshelfBeanList!=null&&bookshelfBeanList.size()>0){
            for(BookshelfBean bookshelfBean:bookshelfBeanList){
                bookMixATocLocalBeans = bookMixATocLocalBeanDaoUtils.queryBookMixATocLocalBeanByQueryBuilder(bookshelfBean.getBookId());
                lists.add(bookMixATocLocalBeans);
            }
            listView.setAdapter(new MyAdapter());
        }
        bookMixATocLocalBeanDaoUtils.closeConnection();
        bookshelfBeanDaoUtils.closeConnection();
    }

    @Override
    public void initEvent() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.main_btn_1:
                intent = new Intent(this,BookCategoryActivity.class);
                break;
            case R.id.main_btn_2:
                intent = new Intent(this,SeekVideoActivity.class);
                break;
            case R.id.main_btn_3:
                intent = new Intent(this,SeekBookActivity.class);
                break;
        }
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bookshelfBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = View.inflate(mContext,R.layout.item_seekvideo,null);
            ImageView imageView = view.findViewById(R.id.item_seekvideo_img);
            TextView textView = view.findViewById(R.id.item_seekvideo_name);
            TextView textView1 = view.findViewById(R.id.item_seekvideo_cont);
            Glide.with(mContext)
                    .load(bookshelfBeanList.get(i).getCover())
                    .into(imageView);
            textView.setText(bookshelfBeanList.get(i).getName());
            textView1.setText("最新章节："+lists.get(i).get(lists.get(i).size()-1).getTitle());
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
