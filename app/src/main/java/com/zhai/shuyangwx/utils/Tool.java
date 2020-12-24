package com.zhai.shuyangwx.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zhai.shuyangwx.R;
import com.zhai.shuyangwx.bean.BdUser;
import com.zhai.shuyangwx.bean.ReadPatternBean;
import com.zhai.shuyangwx.bean.ThecustomBJ;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaiyang on 2018/6/5.
 */

public class Tool {
    // 临时存放搜索记录
    public static void setJiLu(Context context, String name) {
        List<Map<String, String>> a = getJiLu();
        if(a!=null){
            for(Map<String, String> num:a){
                if(num.get("jilu").equals(name)){
                    return;
                }
            }
        }

        String lujin = "seekbook.txt";
        File file = new File(context.getFilesDir().getPath(), lujin);
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = new FileOutputStream(file, true);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            String neirong = name + "#";
            writer.write(neirong);
            System.out.println("搜索记录数据存入成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 读取搜索记录的方法
    public static List<Map<String, String>> getJiLu() {
        String[] jilu = null;
        List<Map<String, String>> arr = new ArrayList<Map<String, String>>();
        // 读取聊天记录文件
        String name = "/data/data/com.zhai.shuyangwx/files/seekbook.txt";
        String res = "";
        FileInputStream fin = null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            fin = new FileInputStream(name);
            reader = new BufferedReader(new InputStreamReader(fin));
            while ((res = reader.readLine()) != null) {
                content.append(res);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StringBuffer sub;
        sub = new StringBuffer();
        sub.append(content.toString());
        if (content.toString().equals("") && content.toString().isEmpty()) {
            return arr = null;
        }
        sub.deleteCharAt(sub.length() - 1);
        String x = sub.toString();
        jilu = x.split("#");
        for (String o : jilu) {
            // 进行文本数据解析
            Map<String, String> map = new HashMap<String, String>();
            map.put("jilu", o);
            arr.add(map);
        }
        return arr;
    }
    public static void qingkong(){
        File file = new File("/data/data/com.zhai.shuyangwx/files/seekbook.txt");
        file.delete();
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 动态权限获取
     * @param activity
     * @return
     */

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
            }, 1);
            return false;
        }

        return true;
    }
    /**
     * android 6.0 以上需要动态申请权限
     * 朗读所需要的权限
     */
    private void initPermission(Activity activity) {
        String permissions[] = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                toApplyList.add(perm);
            //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), 123);
        }

    }
    /**
     * unicode 转 中文
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 0; i < hex.length; i++) {

            try {
                // 汉字范围 \u4e00-\u9fa5 (中文)
                if(hex[i].length()>=4){//取前四个，判断是否是汉字
                    String chinese = hex[i].substring(0, 4);
                    try {
                        int chr = Integer.parseInt(chinese, 16);
                        boolean isChinese = isChinese((char) chr);
                        //转化成功，判断是否在  汉字范围内
                        if (isChinese){//在汉字范围内
                            // 追加成string
                            string.append((char) chr);
                            //并且追加  后面的字符
                            String behindString = hex[i].substring(4);
                            string.append(behindString);
                        }else {
                            string.append(hex[i]);
                        }
                    } catch (NumberFormatException e1) {
                        string.append(hex[i]);
                    }

                }else{
                    string.append(hex[i]);
                }
            } catch (NumberFormatException e) {
                string.append(hex[i]);
            }
        }

        return string.toString();
    }
    /*
    * 中文转unicode编码
    */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * 判断是否为中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    /**
     * 是否是无线
     * @param icontext
     * @return
     */
    public static boolean isWifiActive(Context icontext){
        Context context = icontext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info;
        if (connectivity != null) {
            info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 获取屏幕宽高
     * @param context
     * @return
     */
    public static int[] getWHDP(Context context){
        DisplayMetrics dm=context.getResources().getDisplayMetrics();
        int w4=dm.widthPixels;
        int h4=dm.heightPixels;
        return new int[]{w4,h4};
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率from dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    //获取状态栏高度
    public static int getZTheight(Context con){
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = con.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = con.getResources().getDimensionPixelSize(resourceId);
        }
        return  statusBarHeight1;
    }

    //缓存看小说部分的界面布局（文字大小，字体，模式等）；
    public static void setBuJu(Context con, int size, String font, int buju, int fontcr){
        SharedPreferences sharedPreferences = con.getSharedPreferences("buju",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putInt("size", size);
        editor.putString("font", font);
        editor.putInt("buju", buju);
        editor.putInt("fontcr", fontcr);
        editor.commit();// 提交修改
    }
    //取出保存模式
    public static ReadPatternBean getBuJu(Context con){
        SharedPreferences preferences = con.getSharedPreferences("buju",
                Context.MODE_PRIVATE);
        int size = preferences.getInt("size", 22);
        String font = preferences.getString("font", "1");
        int buju = preferences.getInt("buju", 3);
        int fontcr = preferences.getInt("fontcr", 0);
        ReadPatternBean yd = new ReadPatternBean();
        yd.setSize(size);
        yd.setFont(font);
        yd.setBuju(buju);
        yd.setFontcr(fontcr);
        return yd;
    }

    /**
     * 缓存自定义阅读模式
     * @param con 上下文
     * @param isImg 是否为图片
     * @param bjColor 布局图片地址或者RGB颜色
     * @param textColor 字体颜色
     */
    public static void setThecustomBJ(Context con,int is, int isImg,String bjColor,int textColor){
        SharedPreferences sharedPreferences = con.getSharedPreferences("TBJ",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putInt("is", is);
        editor.putInt("isImg", isImg);
        editor.putString("bjColor", bjColor);
        editor.putInt("textColor", textColor);
        editor.commit();// 提交修改
    }
    //取出自定义阅读模式
    public static ThecustomBJ getThecustomBJ(Context con){
        SharedPreferences preferences = con.getSharedPreferences("TBJ",
                Context.MODE_PRIVATE);
        int size = preferences.getInt("isImg", 0);
        int is = preferences.getInt("is", -1);
        String bjColor = preferences.getString("bjColor", "-1");
        int textColor = preferences.getInt("textColor", -1);
        ThecustomBJ yd = new ThecustomBJ();
        yd.setIs(is);
        yd.setIsImg(size);
        yd.setBjColor(bjColor);
        yd.setTextColor(textColor);
        return yd;
    }
    //删除自定义阅读模式
    public static void delThecustomBJ(Context con){
        SharedPreferences preferences = con.getSharedPreferences("TBJ",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// 获取编辑器
        editor.clear();
        editor.commit();
    }
    //缓存自定义启动页背景
    public static void setTransition(Context con,String path){
        SharedPreferences sharedPreferences = con.getSharedPreferences("QDY",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString("path", path);
        editor.commit();// 提交修改
    }
    //取出自定义启动页背景
    public static String getTransition(Context con){
        SharedPreferences preferences = con.getSharedPreferences("QDY",
                Context.MODE_PRIVATE);
        String path = preferences.getString("path", "-1");
        return path;
    }
    //删除自定义启动页背景
    public static void delTransition(Context con){
        SharedPreferences preferences = con.getSharedPreferences("QDY",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();// 获取编辑器
        editor.clear();
        editor.commit();
    }
    /**
     * 将String转换成字符缓冲流
     * @param s
     * @return
     */
    public static BufferedReader toString_BufferedReader(String s){
        byte[] by = s.getBytes();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(by));
        BufferedReader reader = new BufferedReader(isr);
        return reader;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTime(){
        Calendar cal;
        String year;
        String month;
        String day;
        String hour;
        String minute;
        String second;
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String my_time_1;
        String my_time_2;

        year = String.valueOf(cal.get(Calendar.YEAR));
        month = Integer.parseInt(String.valueOf(cal.get(Calendar.MONTH)))+1+"";
        day = String.valueOf(cal.get(Calendar.DATE));
        if (cal.get(Calendar.AM_PM) == 0)
            hour = String.valueOf(cal.get(Calendar.HOUR));
        else
            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        second = String.valueOf(cal.get(Calendar.SECOND));

        my_time_1 = year + "-" + month + "-" + day+" ";
        my_time_2 = hour + ":" + minute + ":" + second;
        return my_time_1+my_time_2;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
    public static Bitmap getBitmap(String path){
        File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            System.out.println(path);
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            return bitmap;
        }
        return null;
    }

    /**
     * 获取当前时间毫秒
     */
    public static Long getTime_miao(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(Tool.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    /**
     * 讲毫秒时间转换为时间
     */
    public static String getTime_x(Long time){
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time * 1000));
        return result1;
    }
    /**
     * 字符串去除空格、换行符
     */
    public static String replaceBlank(String str) {

        String dest = "";

        if (str != null) {

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");

        }

        return dest;

    }
    /**
     * 隐藏键盘
     */
    public static void  closeKeybord(Activity activity){
        InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public static void showKeybord(final EditText et,Activity activity) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 转义特殊符号
     */
    public static String changeFH(String str){
        str = str.replaceAll("\"","\\\""); //替换"
        str = str.replaceAll("\\\\","\\\\\\\\"); //替换/
        return str;
    }
    /**
     * 缓存用户
     * @param con 上下文
     * @param name 姓名
     * @param sex 性别
     * @param isQD 是否为首启动页码
     */
    public static void setUser(Context con,String name, String sex,String isQD){
        SharedPreferences sharedPreferences = con.getSharedPreferences("USER",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString("name", name);
        editor.putString("sex", sex);
        editor.putString("isQD", isQD);
        editor.commit();// 提交修改
    }
    //取出用户
    public static BdUser getUser(Context con){
        SharedPreferences preferences = con.getSharedPreferences("USER",
                Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        String sex = preferences.getString("sex", "-1");
        String isQD = preferences.getString("isQD", "-1");
        BdUser bdUser = new BdUser();
        bdUser.setName(name);
        bdUser.setSex(sex);
        bdUser.setIsQd(isQD);
        return bdUser;
    }
}
