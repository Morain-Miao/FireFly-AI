package com.cafi.firefly.service;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *  图片处理service
 */
public interface ImgProcessingService {
    /**
     * TencentAI OCR process
     * @param base64 图片的base64字符串编码
     * @return 腾讯AI平台ORC的返回报文
     */
    String generalBasicOCR(String base64);

    /**
     * 图片标注
     * @param arrayList 坐标数组：原点x坐标，原点y坐标，矩形的长，矩形的宽
     * @param inputStream 图片的输入流
     * @return 带缓冲区的图片流
     */
    BufferedImage imageOverlays(ArrayList<Integer> arrayList,InputStream inputStream);

    /**
     * 获取坐标
     * @param tencentRsp 腾讯的返回报文
     * @param note 用户输入的说明
     * @return 坐标数组
     */
    ArrayList<Integer> getCoordinate(String tencentRsp,String note);

    /**
     * 图片base64转输入流
     * @param base64 base64字符串
     * @return 输入流
     */
    ByteArrayInputStream changeBase64ToInputStream(String base64);
}
