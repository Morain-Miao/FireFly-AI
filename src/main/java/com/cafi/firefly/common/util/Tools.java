package com.cafi.firefly.common.util;

/**
 * @program: firefly
 * @description: 工具类
 * @author: Miao
 * @create: 2021-03-13 18:09
 */
public class Tools {
    /**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     *
     * @param str      原始字符串
     * @param strStart 开始字符串
     * @param strEnd   结束字符串
     * @return 截取后的字符串
     */
    public static String subString(String str, String strStart, String strEnd) {

        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
}
