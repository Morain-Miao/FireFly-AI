package com.cafi.firefly.example.test;

import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: firefly
 * @description: 图片转base64测试类
 * @author: Miao
 * @create: 2021-03-13 17:43
 */
public class base64Test {
    public static void main(String[] args) {
        String imgFilePath = "E:\\1.jpg";
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        System.out.println(encoder.encode(data).replaceAll("\r|\n", "").trim());//返回字符串
    }
}
