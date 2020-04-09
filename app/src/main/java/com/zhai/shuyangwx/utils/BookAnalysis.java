package com.zhai.shuyangwx.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 本地小说解析类
 */
public class BookAnalysis {

    public void read(String fileNamedirs) throws FileNotFoundException {
        List<String> books = new ArrayList<>();
        try {
            // 编码格式
            String encoding = "GBK";
            // 文件路径
            File file = new File(fileNamedirs);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                // 输入流
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                Long count = (long) 0;
                boolean bflag=false;
                int n=0;
                String newStr=null;
                String titleName=null;
                String newChapterName = null;//新章节名称
                String substring=null;
                int indexOf=0;
                int indexOf1=0;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    count++;
                    // 正则表达式
                    Pattern p = Pattern.compile("(^\\s*第)(.{1,9})[章节卷集部篇回](\\s{1})(.*)($\\s*)");

                    Matcher matcher = p.matcher(lineTxt);
                    Matcher matcher1 = p.matcher(lineTxt);
                    newStr=newStr+lineTxt;

                    while (matcher.find()) {
                        titleName = matcher.group();
                        //章节去空
                        newChapterName = titleName.trim();
                        indexOf1=indexOf;
                        indexOf = newStr.indexOf(newChapterName);
                        //System.out.println(newChapterName + ":" + "第" + count + "行"); // 得到返回的章
                        if(bflag) {
                            bflag=false;
                            break;
                        }
                        if(n==0) {
                            indexOf1 = newStr.indexOf(newChapterName);
                        }
                        n=1;
                        bflag=true;
                    }
                    while(matcher1.find()) {
                        if(indexOf!=indexOf1) {
                            substring = newStr.substring(indexOf1, indexOf);
                            books.add(substring);
                            System.out.println(substring);
                        }

                    }
                }
                bufferedReader.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }
}
