package com.zhai.shuyangwx.fragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.bean.MsgBean;
import com.zhai.shuyangwx.bean.VideoSeekBean;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.http.VideoDataManager;
import com.zhai.shuyangwx.utils.Tool;


import java.util.List;

import butterknife.BindView;

/**
 * 留言板
 */
public class FindFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.find_listview)
    ListView listView;
    @BindView(R.id.item_find_addmsg)
    ImageView addmsg;

    MsgBean list;
    AlertDialog.Builder dialog;
    String is;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initToolBar() {

    }

    public void initView() {
        DataManager.getInstance().getMsg(new ProgressSubscriber<MsgBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.makeText(getActivity(),"没有获取到留言哦~",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNext(Object o) {
                list = (MsgBean) o;
                if(list.getData().size()!=0){
                    listView.setAdapter(new MsgAdapter(list));
                }
            }
        },getActivity(),"留言加载中..."));
    }
    @Override
    public void initEvent() {
        addmsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_find_addmsg:
                final EditText editText;
                final EditText editText_name;
                dialog = new AlertDialog.Builder(getActivity());
                //使得点击对话框外部不消失对话框
                dialog.setCancelable(true);
                dialog.setTitle("请输入给管理员的留言：");
                View view1 = View.inflate(getActivity(),R.layout.item_addmsg,null);
                editText = view1.findViewById(R.id.item_addmsg_ed);
                editText_name = view1.findViewById(R.id.item_addmsg_name);
                dialog.setView(view1);
                dialog.setPositiveButton("留言", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String msg = editText.getText().toString();
                        final String name = editText_name.getText().toString();
                        final String time = Tool.getTime_miao()/1000+"";

                        if(!msg.isEmpty()&&!name.isEmpty()){
                            showWaitingDialog("加载中...");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    is = VideoDataManager.setMsg(name,msg,time);
                                    handler.sendEmptyMessage(0x123);
                                }
                            }).start();
                        }else{
                            toast("留言失败，用户名或留言不能为空！");
                        }

                    }
                });
                dialog.setNegativeButton("取消",null);
                dialog.show();
                break;
        }
    }
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disWaitingDialog();
            if(is.equals("0")){
                toast("留言成功！");
                initView();
            }else{
                toast("留言失败！服务器异常！");
            }
        }
    };
    /**
     * 留言板适配器
     */
    class MsgAdapter extends BaseAdapter{
        private MsgBean list;
        private TextView name;
        private TextView msg;
        private TextView msg_admin;
        private TextView time;
        private TextView is;
        MsgAdapter(MsgBean list){
            this.list = list;
        }
        @Override
        public int getCount() {
            return list==null?0:list.getData().size();
        }

        @Override
        public Object getItem(int i) {
            return list.getData().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //加载布局为一个视图
            @SuppressLint("ViewHolder") View myview = View.inflate(getActivity(),R.layout.item_find_list,null);
            name = myview.findViewById(R.id.item_find_name);
            msg = myview.findViewById(R.id.item_find_msg);
            msg_admin = myview.findViewById(R.id.item_find_msg_admin);
            time = myview.findViewById(R.id.item_find_time);
            is = myview.findViewById(R.id.item_find_is);
            //为Item 里面的组件设置相应的数据
            name.setText(list.getData().get(i).getName()+"：");
            msg.setText(list.getData().get(i).getMsg());
            if(!list.getData().get(i).getMsg_admin().isEmpty()){
                msg_admin.setVisibility(View.VISIBLE);
                msg_admin.setText("管理员回复："+list.getData().get(i).getMsg_admin());
                is.setVisibility(View.VISIBLE);
            }else{
                msg_admin.setVisibility(View.GONE);
                is.setVisibility(View.GONE);
            }
            long l = Long.parseLong(list.getData().get(i).getTime());
            String s = Tool.getTime_x(l);
            time.setText(s);
            //返回含有数据的view
            return myview;
        }
    }

}
