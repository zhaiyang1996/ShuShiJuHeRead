package com.shushijuhe.shushijuheread.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

/**
 * 翟阳：IO流管理类，写入文本和删除文本
 */

public class IOUtils {

    public static void close(Closeable closeable){
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
            //close error
        }
    }

    /**
     * IO流：将txt文件写入到内存卡之中
     * @param bookid
     * @param bookmixname 章节名
     * @param bookBody 书籍内容
     */
    public static void setText_SD(Context context,String bookid,String bookmixname,String bookBody){
        String filePath = "./sdcard/ShuShiJuhe/BOOKTXT/"+bookid+"/";
        String fileName = bookmixname+".txt";
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        RandomAccessFile raf = null;
        // 每次写入时，都换行写
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(bookBody.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }finally {
            if(raf!=null){
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    //删除指定txt文件   通过路径
    public static void deleteFile(String path) {
        File f = new File(path);  // 输入要删除的文件位置
        if (f.exists()) {
            f.delete();
        }

    }

    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

        // 读取文件
    public static String getText(String path) {
        String res = "";
        FileInputStream fin = null;
        BufferedReader reader = null;
        StringBuffer content = new StringBuffer();
        try {
            fin = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            while ((res = reader.readLine()) != null) {
                content.append(res+"\r\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
            if(reader!=null){

                    reader.close();
            }
            if(fin!=null)
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuffer sub;
        sub = new StringBuffer();
        sub.append(content.toString());
        String x = sub.toString();
        return x;
    }
}
