package com.zhai.shuyangwx.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhai.shuyangwx.MainActivity;
import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.activity.AboutUsActivity;
import com.zhai.shuyangwx.activity.BookCategoryActivity;
import com.zhai.shuyangwx.activity.Play_accompanyActivity;
import com.zhai.shuyangwx.activity.ReadActivity;
import com.zhai.shuyangwx.activity.SeekVideoActivity;
import com.zhai.shuyangwx.activity.TransitionActivity;
import com.zhai.shuyangwx.application.app;
import com.zhai.shuyangwx.bean.BookData;
import com.zhai.shuyangwx.bean.BookMixAToc;
import com.zhai.shuyangwx.bean.BookMixATocLocalBean;
import com.zhai.shuyangwx.bean.BookshelfBean;
import com.zhai.shuyangwx.bean.GHSBean;
import com.zhai.shuyangwx.bean.KMBean;
import com.zhai.shuyangwx.bean.MsgBean;
import com.zhai.shuyangwx.constants.Constants;
import com.zhai.shuyangwx.dao.BookDataDaoUtils;
import com.zhai.shuyangwx.dao.BookMixATocLocalBeanDaoUtils;
import com.zhai.shuyangwx.dao.BookshelfBeanDaoUtils;
import com.zhai.shuyangwx.http.DataManager;
import com.zhai.shuyangwx.http.ProgressSubscriber;
import com.zhai.shuyangwx.http.SubscriberOnNextListenerInstance;
import com.zhai.shuyangwx.service.DownloadService;
import com.zhai.shuyangwx.utils.GetImagePath;
import com.zhai.shuyangwx.utils.IOUtils;
import com.zhai.shuyangwx.utils.Tool;
import com.will.filesearcher.file_searcher.FileSearcherActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DrawerFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.drawer_me)
    Button textView;
    @BindView(R.id.drawer_iqy)
    Button iqi;
    @BindView(R.id.drawer_books)
    Button books;
    @BindView(R.id.drawer_vod)
    Button vod;
    @BindView(R.id.drawer_vod_acc)
    Button drawer_vod_acc;

    private int REQUEST_CODE = 0;
    //目录
    private List<BookMixATocLocalBean> directoryList = new ArrayList<>();
    BookshelfBeanDaoUtils bookshelfBeanDaoUtils;
    BookMixATocLocalBeanDaoUtils bookMixATocLocalBeanDaoUtils;
    BookDataDaoUtils bookDataDaoUtils;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_drawer;
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void initView() {
        bookshelfBeanDaoUtils = new BookshelfBeanDaoUtils(getActivity());
        bookMixATocLocalBeanDaoUtils= new BookMixATocLocalBeanDaoUtils(getActivity());
        bookDataDaoUtils = new BookDataDaoUtils(getActivity());
    }

    @Override
    public void initEvent() {
        textView.setOnClickListener(this);
        iqi.setOnClickListener(this);
        books.setOnClickListener(this);
        vod.setOnClickListener(this);
        drawer_vod_acc.setOnClickListener(this);
    }
    Dialog dialog;
    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final EditText editText = new EditText(getActivity());
        final EditText sexEditText = new EditText(getActivity());
        switch (v.getId()){
            case R.id.drawer_me:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.drawer_iqy:
                //设置自定义启动页
                transitionDiglog();
                break;
            case R.id.drawer_books:
                //导入扫描本地书籍
                seekBooks();
                break;
            case R.id.drawer_vod:
                //打开羞羞传送门
                dialog.setTitle("请输入你的传送票：");
                dialog.setView(editText);
                dialog.setPositiveButton("上车", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = editText.getText().toString();
                        if(!str.isEmpty()){
                            openXX(str);
                        }else{
                            toast("你的车票是空的呀！！！");
                        }
                    }
                });
                dialog.setNegativeButton("取消",null);
                dialog.show();
                break;
            case R.id.drawer_vod_acc:
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                editText.setHint("输入聊天室名称，最多6个字");
                sexEditText.setHint("输入性别(只能输入男或女，偷懒就让你们自己输啦)");
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);
                linearLayout.addView(sexEditText);

                //打开放映厅
                dialog.setView(linearLayout);
                dialog.setTitle("输一次后保存，更改得重装APP：");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = editText.getText().toString();
                        String sex = sexEditText.getText().toString();
                        if(!str.isEmpty()){
                            if(!sex.isEmpty()&&sex.equals("男")||sex.equals("女")){
                                Intent intent = new Intent(getActivity(),Play_accompanyActivity.class);
                                //缓存本地
                                Tool.setUser(getContext(),str,sex,"-1");
                                startActivity(intent);
                            }else{
                                toast("性别只能输入男或女");
                            }
                        }else{
                            toast("你的名字是空的呀！！！");
                        }
                    }
                });
                dialog.setNegativeButton("取消",null);
                if(Tool.getUser(mContext).getSex().equals("-1")){
                    dialog.show();
                }else{
                    startActivity(new Intent(getActivity(), Play_accompanyActivity.class));
                }

                break;
        }
    }
    public void seekBooks(){
        toast("自动过滤100K以下文件");
        Intent intent = new Intent(getActivity(), FileSearcherActivity.class);
        intent.putExtra("keyword",".txt");
        intent.putExtra("min",100 * 1024);
        intent.putExtra("theme",R.style.SearchTheme);//set custom theme here
        startActivityForResult(intent,REQUEST_CODE);
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
        if(requestCode == REQUEST_CODE && resultCode ==FileSearcherActivity.OK && data != null){
            ArrayList<File> list = ( ArrayList<File>) data.getSerializableExtra("data");
            showWaitingDialog("正在解析中...");
            for(int i=0;i<list.size();i++){
                getChapter(IOUtils.getText(list.get(i).getPath()),list.get(i).getName(),list.get(i).getPath());
            }
            disWaitingDialog();
        }
    }
    //获取章节
    public synchronized void getChapter(String s,String title,String id){
        try {
                long size = 0;
                String[] paragraphs = s.split("\r\n");
                List<BookshelfBean> bookshelfBeans= bookshelfBeanDaoUtils.queryBookshelfBeanByQueryBuilder(id);
                if(bookshelfBeans!=null&&bookshelfBeans.size()>0){
                    toast("添加失败：不能重复添加书籍！");
                    return;
                }
                for (String str : paragraphs) {
                    if (str.length() <= 30 && (str.matches(".*第.{1,8}章.*") || str.matches(".*第.{1,8}节.*"))) {
                        BookMixATocLocalBean bookMixATocLocalBean = new BookMixATocLocalBean();
                        bookMixATocLocalBean.setTitle(str);
                        bookMixATocLocalBean.setLink("萌大人好帅");
                        bookMixATocLocalBean.setIsOnline(false);
                        bookMixATocLocalBean.setBookid(id);
                        directoryList.add(bookMixATocLocalBean);
                    }
                    if (str.contains("\u3000\u3000")) {
                        size += str.length() + 2;
                    }else if (str.contains("\u3000")){
                        size += str.length() + 1;
                    }else {
                        size += str.length();
                    }
                }
            setShelf(id,title);
            //将书籍文件解析
            if(directoryList!=null&&directoryList.size()<1){
                BookMixATocLocalBean bookMixATocLocalBean = new BookMixATocLocalBean();
                bookMixATocLocalBean.setTitle(title);
                bookMixATocLocalBean.setLink("萌大人好帅");
                bookMixATocLocalBean.setIsOnline(false);
                bookMixATocLocalBean.setBookid(id);
                directoryList.add(bookMixATocLocalBean);
            }
            setBook(s);
            toast("添加成功！");
        }catch (Exception e){
            e.printStackTrace();
            toast("添加失败：资源文件异常！");
        }
    }
    /**
     * 书架书籍添加
     */
    public void setShelf(String id,String tible){
        BookshelfBean bookshelfBean = new BookshelfBean();
        bookshelfBean.setBookId(id);
        bookshelfBean.setCover("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2662340795,3260352472&fm=15&gp=0.jpg");
        bookshelfBean.setName(tible);
        bookshelfBean.setTime(Tool.getTime());
        bookshelfBean.setTimeMillis(System.currentTimeMillis());
        bookshelfBean.setIsEnd(true);
        bookshelfBeanDaoUtils.insertBookshelfBean(bookshelfBean);
        bookMixATocLocalBeanDaoUtils.insertMultBookMixATocLocalBean(directoryList);
    }
    /**
     * 解析书籍文件并存放于本地书籍
     */
    public void setBook(String s){
        if(directoryList!=null&&directoryList.size()<2){
            //章节解析失败
            String path = "./sdcard/书阳小说/BOOKTXT/" + directoryList.get(0).getBookid() + "/" + directoryList.get(0).getTitle() + ".txt";
            BookData bookData = new BookData();
            bookData.setBookId(directoryList.get(0).getBookid());
            bookData.setTitle(directoryList.get(0).getTitle());
            bookData.setBody(path);
            bookDataDaoUtils.insertBookData(bookData);
            IOUtils.setText_SD(getActivity(), directoryList.get(0).getBookid(), directoryList.get(0).getTitle(), s);
        }else{
            //章节解析成功
            for(int i =0;i<directoryList.size();i++) {
                int o = s.indexOf(directoryList.get(i).getTitle());
                int o1 = 0;
                String str;
                if ((i + 1) < directoryList.size()) {
                    o1 = s.indexOf(directoryList.get(i + 1).getTitle());
                } else {
                    o1 = -1;
                }
                if (o1 != -1) {
                    str = s.substring(o, o1);
                } else {
                    str = s.substring(o, s.length() - 1);
                }
                String path = "./sdcard/书阳小说/BOOKTXT/" + directoryList.get(i).getBookid() + "/" + directoryList.get(i).getTitle() + ".txt";
                BookData bookData = new BookData();
                bookData.setBookId(directoryList.get(i).getBookid());
                bookData.setTitle(directoryList.get(i).getTitle());
                bookData.setBody(path);
                bookDataDaoUtils.insertBookData(bookData);
                IOUtils.setText_SD(getActivity(), directoryList.get(i).getBookid(), directoryList.get(i).getTitle(), str);
            }
        }
    }
    public void openXX(final String str){
        if(Tool.isNetworkConnected(getActivity())){
            DataManager.getInstance().getGHS(new ProgressSubscriber<GHSBean>(new SubscriberOnNextListenerInstance() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    Toast.makeText(getActivity(),"服务器君貌似出问题了啦~",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNext(Object o) {
                    GHSBean ghsBean = (GHSBean) o;
                    if(ghsBean.getNo_off().equals("0")){
                        getKm(ghsBean.getUrl(),str);
//                        startActivity(new Intent(getActivity(), SeekVideoActivity.class));
                    }else{
                        toast("传送门暂时关闭了哟~");
                    }
                }
            },getActivity(),null));
        }else {
            toast("阁下似乎没有网络哦~");
        }
    }
    //加载获取卡密
    public void getKm(String url, final String km){
        app.xxUrl = url;
        DataManager.getInstance().getKM(new ProgressSubscriber<KMBean>(new SubscriberOnNextListenerInstance() {
            @Override
            public void onNext(Object o) {
                KMBean kmBean = (KMBean) o;
                if(kmBean.getIson().equals("on")){
                    long timeB = Tool.getTime_miao()/1000;
                    long timeX = Long.parseLong(kmBean.getTime());
                    long time = timeX - timeB;
                    Log.d("时间：", timeX+":"+timeB+":"+time);
                    if(time>0){
                        toast("传送票还有 "+ ((time/86400)+1)+" 天到期呢~");
                        BookCategoryActivity.start(getActivity(),2);
                        app.xxKM = km;
                    }else{
                        toast("这张传送票似乎到期啦！快去在搞一张啦~");
                    }
                }else{
                    toast("你的传送票似乎没有哦~");
                }
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Toast.makeText(getActivity(),"服务器君貌似出问题啦~",Toast.LENGTH_LONG).show();
            }
        },getActivity(),null),url,km);
    }
}
