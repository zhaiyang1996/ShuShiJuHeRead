package com.shushijuhe.shushijuheread.utils;

public class StringUtils {
    /**
     * 将文本中的半角字符，转换成全角字符
     * @param input
     * @return
     */
    public static String halfToFull(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++)
        {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i]> 32 && c[i]< 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    //功能：字符串全角转换为半角
    public static String fullToHalf(String input)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++)
        {
            if (c[i] == 12288) //全角空格
            {
                c[i] = (char) 32;
                continue;
            }

            if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
}
