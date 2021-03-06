package com.cafi.firefly.domain;

import com.cafi.firefly.bean.ImgEntityVo;
import com.cafi.firefly.service.ImgProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @program: firefly
 * @description: 图片处理主流程（DDD）
 * @author: Miao
 * @create: 2021-03-13 16:42
 */
@Slf4j
@Component
public class ImgMainProcess {
    @Autowired
    ImgProcessingService imgProcessingService;

    public String imgProcessed(ImgEntityVo request, HttpServletResponse response) {
        //调用腾讯AI OCR接口获取识别信息 返回是个Json
        String tencentRsp = imgProcessingService.generalBasicOCR(request.getImgBase64());
        //写死返回用于测试
//        tencentRsp = "{\n" +
//                "  \"TextDetections\": [\n" +
//                "    {\n" +
//                "      \"DetectedText\": \"中粮\",\n" +
//                "      \"Confidence\": 99,\n" +
//                "      \"ItemPolygon\": {\n" +
//                "        \"X\": 249,\n" +
//                "        \"Y\": 102,\n" +
//                "        \"Width\": 135,\n" +
//                "        \"Height\": 59\n" +
//                "      },\n" +
//                "      \"Polygon\": [\n" +
//                "        {\n" +
//                "          \"X\": 249,\n" +
//                "          \"Y\": 102\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 384,\n" +
//                "          \"Y\": 102\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 384,\n" +
//                "          \"Y\": 161\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 249,\n" +
//                "          \"Y\": 161\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      \"AdvancedInfo\": \"{\\\"Parag\\\":{\\\"ParagNo\\\":1}}\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"DetectedText\": \"COFCO\",\n" +
//                "      \"Confidence\": 98,\n" +
//                "      \"ItemPolygon\": {\n" +
//                "        \"X\": 252,\n" +
//                "        \"Y\": 170,\n" +
//                "        \"Width\": 130,\n" +
//                "        \"Height\": 33\n" +
//                "      },\n" +
//                "      \"Polygon\": [\n" +
//                "        {\n" +
//                "          \"X\": 252,\n" +
//                "          \"Y\": 170\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 382,\n" +
//                "          \"Y\": 170\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 382,\n" +
//                "          \"Y\": 203\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 252,\n" +
//                "          \"Y\": 203\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      \"AdvancedInfo\": \"{\\\"Parag\\\":{\\\"ParagNo\\\":2}}\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"DetectedText\": \"自然之源重塑你我\",\n" +
//                "      \"Confidence\": 99,\n" +
//                "      \"ItemPolygon\": {\n" +
//                "        \"X\": 254,\n" +
//                "        \"Y\": 215,\n" +
//                "        \"Width\": 127,\n" +
//                "        \"Height\": 15\n" +
//                "      },\n" +
//                "      \"Polygon\": [\n" +
//                "        {\n" +
//                "          \"X\": 254,\n" +
//                "          \"Y\": 215\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 381,\n" +
//                "          \"Y\": 215\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 381,\n" +
//                "          \"Y\": 230\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"X\": 254,\n" +
//                "          \"Y\": 230\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      \"AdvancedInfo\": \"{\\\"Parag\\\":{\\\"ParagNo\\\":2}}\"\n" +
//                "    }\n" +
//                "  ],\n" +
//                "  \"Language\": \"zh\",\n" +
//                "  \"Angel\": 359.989990234375,\n" +
//                "  \"PdfPageSize\": 0,\n" +
//                "  \"RequestId\": \"9e95cadf-95f1-4a07-bf12-751344c696c2\"\n" +
//                "}";
        //根据腾讯的返回Json和请求里的用户录入信息获取坐标数组
        ArrayList<Integer> coordinate = imgProcessingService.getCoordinate(tencentRsp, request.getNote());
        //图片base64转输入流
        ByteArrayInputStream byteArrayInputStream = imgProcessingService.changeBase64ToInputStream(request.getImgBase64());
        //图片标注
        BufferedImage bufferedImage = imgProcessingService.imageOverlays(coordinate, byteArrayInputStream);
        return BufferedImageToBase64(bufferedImage);

    }
    /**
     * BufferedImage 编码转换为 base64
     * @param bufferedImage
     * @return
     */
    @org.jetbrains.annotations.NotNull
    public static String BufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();//io流
        try {
            ImageIO.write(bufferedImage, "png", baos);//写入流中
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//转换成字节
        BASE64Encoder encoder = new BASE64Encoder();
        String png_base64 = encoder.encodeBuffer(bytes).trim();//转换成base64串
        png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
//        System.out.println("值为：" + "data:image/jpg;base64," + png_base64);
        return png_base64;
    }
}
