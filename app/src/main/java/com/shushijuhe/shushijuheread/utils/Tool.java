package com.shushijuhe.shushijuheread.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.shushijuhe.shushijuheread.activity.base.TxtPageBean;
import com.shushijuhe.shushijuheread.bean.ReadPatternBean;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name = "/data/data/com.zhai.sunshineread/files/seekbook.txt";
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
        File file = new File("/data/data/com.zhai.sunshineread/files/seekbook.txt");
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
                    Manifest.permission.CAMERA
            }, 1);
            Toast.makeText(activity,"无权限，可能无法缓存书籍搜索历史等",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
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
}
