package com.shushijuhe.shushijuheread.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.base.BaseActivity;
import com.shushijuhe.shushijuheread.bean.Book_infoBean;
import com.shushijuhe.shushijuheread.http.DataManager;
import com.shushijuhe.shushijuheread.http.ProgressSubscriber;
import com.shushijuhe.shushijuheread.http.SubscriberOnNextListenerInstance;

import butterknife.BindView;

/**
 * Created by zhaiyang on 2018/6/4.
 */

public class ReaderActivity extends BaseActivity {

    @BindView(R.id.ed)
    EditText ed;
    @BindView(R.id.but)
    Button button;
    @BindView(R.id.text)
    TextView textView;

    @Override
    public int getLayoutId() {
        return R.layout.reader_activity;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
    }

    @Override
    public void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ed.getText().toString();
                if(!str.isEmpty()){
                    DataManager.getInstance().getBook_info(new ProgressSubscriber<Book_infoBean>(new SubscriberOnNextListenerInstance() {
                        @Override
                        public void onNext(Object o) {
                            Book_infoBean book_infoBean = (Book_infoBean) o;
                            String string = "";
                            for(Book_infoBean.SearchBooks searchBooks:book_infoBean.books){
                                string = string+"书名："+ searchBooks.title+"\n";
                            }
                            textView.setText(string);
                        }
                    },ReaderActivity.this,"搜索中..."),str);
                }else{
                    toast("搜索内容不能为空");
                }
            }
        });
    }
}
