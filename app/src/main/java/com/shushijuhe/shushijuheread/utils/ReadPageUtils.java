package com.shushijuhe.shushijuheread.utils;

import android.content.Context;
import android.text.TextPaint;

import com.shushijuhe.shushijuheread.activity.base.TxtPageBean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *文本分页类
 */
public class ReadPageUtils {
    //书籍绘制区域的宽高
    private  int mVisibleWidth;
    private  int mVisibleHeight;
    private final int DEFAULT_MARGIN_HEIGHT = 28;
    private final int DEFAULT_MARGIN_WIDTH = 15;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara = 0;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mTextInterval;
    private Context context;
    private TextPaint mTextPaint;
    /**
            * 作用：设置与文字相关的参数
     *
             * @param textSize
     */
    public void setUpTextParams(int textSize) {
        // 文字大小
        mTextSize = textSize;
        // 行间距(大小为字体的一半)
        mTextInterval = mTextSize / 2;
        // 段落间距(大小为字体的高度)
        mTextPara = mTextSize;
        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
    }

    public void setToolWh(Context context, int w, int h){
        mVisibleWidth = w;
        mVisibleHeight = h;
        // 初始化参数
        mMarginWidth = Tool.dip2px(context,DEFAULT_MARGIN_WIDTH);
        mMarginHeight = Tool.dip2px(context,DEFAULT_MARGIN_HEIGHT);

    }
    /**
     * 获取章节的文本流
     *
     * @param chapter
     * @return
     */
    public BufferedReader getChapterReader(String chapter){
        byte[] by = chapter.getBytes();
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(by));
        BufferedReader reader = new BufferedReader(isr);
        return reader;
    }

    /**************************************private method********************************************/
    /**
     * 将章节数据，解析成页面列表
     *
     * @return
     */
    public List<TxtPageBean> loadPages(String title, String body) throws Exception {
        BufferedReader br;
        br = getChapterReader(body);
        //生成的页面
        List<TxtPageBean> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        int rHeight = mVisibleHeight;
        int titleLinesCount = 0;
        boolean showTitle = false; // 是否展示标题
        String paragraph = title;//默认展示标题
        try {
            while (showTitle || (paragraph = br.readLine()) != null) {
                // 重置段落
                if (!showTitle) {
                    paragraph = paragraph.replaceAll("\\s", "");
                    // 如果只有换行符，那么就不执行
                    if (paragraph.equals("")) continue;
                    paragraph = StringUtils.halfToFull("  " + paragraph + "\n");
                }
                int wordCount = 0;
                String subStr = null;
                while (paragraph.length() > 0) {
                    //当前空间，是否容得下一行文字
                    if (!showTitle) {
                        rHeight -= mTextPaint.getTextSize();
                    }
                    // 一页已经填充满了，创建 TextPage
                    if (rHeight <= 0) {
                        // 创建Page
                        TxtPageBean page = new TxtPageBean();
                        page.position = pages.size();
                        page.title = title;
                        page.lines = new ArrayList<>(lines);
                        page.titleLines = titleLinesCount;
                        pages.add(page);
                        // 重置Lines
                        lines.clear();
                        rHeight = mVisibleHeight;
                        titleLinesCount = 0;

                        continue;
                    }

                    //测量一行占用的字节数
                    if (!showTitle) {
                        wordCount = mTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    }

                    subStr = paragraph.substring(0, wordCount);
                    if (!subStr.equals("\n")) {
                        //将一行字节，存储到lines中
                        lines.add(subStr);

                        //设置段落间距
                        if (!showTitle) {
                            rHeight -= mTextInterval;
                        }
                    }
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }

                //增加段落的间距
                if (!showTitle && lines.size() != 0) {
                    rHeight = rHeight - mTextPara + mTextInterval;
                }
            }
            if (lines.size() != 0) {
                //创建Page
                TxtPageBean page = new TxtPageBean();
                page.position = pages.size();
                page.title = title;
                page.lines = new ArrayList<>(lines);
                page.titleLines = titleLinesCount;
                pages.add(page);
                //重置Lines
                lines.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br!=null){
//                IOUtils.close(br);
            }
        }
        return pages;
    }
}
