package com.shushijuhe.shushijuheread.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shushijuhe.shushijuheread.R;
import com.shushijuhe.shushijuheread.activity.AboutUsActivity;
import com.shushijuhe.shushijuheread.activity.ReadActivity;
import com.shushijuhe.shushijuheread.utils.GetImagePath;
import com.shushijuhe.shushijuheread.utils.Tool;

import butterknife.BindView;

public class DrawerFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.drawer_me)
    TextView textView;
    @BindView(R.id.drawer_iqy)
    TextView iqi;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_drawer;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        textView.setOnClickListener(this);
        iqi.setOnClickListener(this);
    }
    Dialog dialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawer_me:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.drawer_iqy:
                transitionDiglog();
                break;
        }
    }
    public void transitionDiglog(){
        String[] strings = {"设置自定义启动页","还原启动页"};
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(getActivity());
        ListView listView = new ListView(getActivity());
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,strings);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //打开图库
                    if(Tool.isGrantExternalRW(getActivity())){
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    }else{
                        toast("没有文件获取权限哦~");
                    }
                }else{
                    Tool.delTransition(getActivity());
                    toast("还原成功，下次启动时生效！");
                }
                dialog.dismiss();
            }
        });
        customizeDialog.setTitle("选择功能：");
        customizeDialog.setView(listView);
        dialog = customizeDialog.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            //获得图片的uri
            Uri uri = data.getData();
            try
            {
                String path = GetImagePath.getRealPathFromUri(getActivity(),uri);
                Tool.setTransition(getActivity(),path);
                toast("设置成功，下次启动时生效！");
            }
            catch (Exception e)
            {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
                toast("设置失败，图片不支持");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
