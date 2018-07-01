package com.shushijuhe.shushijuheread.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.application.app;
import com.shushijuhe.shushijuheread.bean.BookMixAToc;
import com.shushijuhe.shushijuheread.bean.BookMixATocLocalBean;
import com.shushijuhe.shushijuheread.utils.TopMenuHeader;

import java.util.List;

import butterknife.BindView;

/**
 * 翟阳：18/6/30
 * 目录界面
 */
public class BookMixATocActivity  extends BaseActivity{

    @BindView(R.id.book_caidan_touxmixtoc_list)
    ListView listView;
    public static String MIX = "MIX";
    public static String BOOKNAME = "BOOKNAME";
    public BookMixAToc bookMixAToc;
    public List<BookMixATocLocalBean> bookMixATocLocalBean;
    public int mix = 0;
    public String bookName;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bookmixtoc;
    }

    @Override
    public void initToolBar() {
        TopMenuHeader topMenu = new TopMenuHeader(getWindow().getDecorView(), this);
        topMenu.setTopMenuHeader(R.mipmap.title_backtrack, "目录",
                "", -1, -1);
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
        mix = getIntent().getIntExtra(MIX,0);
        bookName = getIntent().getStringExtra(BOOKNAME);
        bookMixAToc = app.bookMixAToc;
        bookMixATocLocalBean = app.bookMixATocLocalBean;
        if(bookMixAToc!=null&&bookMixAToc.mixToc.chapters.size()>0){
            listView.setAdapter(new MyAdapter());
            if(mix>0){
                listView.setSelection(mix-1);
            }else{
                listView.setSelection(mix);
            }
        }else{
            toast("目录加载异常,重新打开加载!");
        }

    }

    @Override
    public void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //打开阅读界面，默认page为1
                ReadActivity.statrActivity(BookMixATocActivity.this,bookMixAToc,bookMixATocLocalBean,bookName,i,0,true);
            }
        });
    }


    /**
     * @param context 上下文
     * @param bookMixAToc 目录
     * @param mix 具体章节
     * @param bookName 书籍名称
     */
    public static void statrActivity(Context context, BookMixAToc bookMixAToc, List<BookMixATocLocalBean> bookMixATocLocalBean, int mix, String bookName){
        if(bookMixAToc == null){
            app.bookMixAToc = null;
            app.bookMixATocLocalBean = bookMixATocLocalBean;
        }else{
            app.bookMixAToc = bookMixAToc;
            app.bookMixATocLocalBean = null;
        }
        Intent intent = new Intent(context,BookMixATocActivity.class);
        intent.putExtra(MIX,mix);
        intent.putExtra(BOOKNAME,bookName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //同名activity只允许一个存活
        context.startActivity(intent);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bookMixAToc.mixToc.chapters.size();
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
            view = View.inflate(BookMixATocActivity.this,R.layout.item_mixtoc,null);
            TextView view1 = view.findViewById(R.id.item_mixtoc_name);
            TextView view2 = view.findViewById(R.id.item_mixtoc_isonline);
            view1.setText(bookMixAToc.mixToc.chapters.get(i).title);
            String str = "";
            // 获取颜色资源文件
            int mycolor;
            if(bookMixAToc.mixToc.chapters.get(i).isOnline){
                str = "未下载";
                mycolor= getResources().getColor(R.color.red);
                view2.setTextColor(mycolor);
            }else{
                str = "已下载";
                mycolor= getResources().getColor(R.color.colorBackground);
                view2.setTextColor(mycolor);
            }
            view2.setText(str);
            return view;
        }
    }
}
