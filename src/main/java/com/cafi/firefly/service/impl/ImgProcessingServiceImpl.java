package com.cafi.firefly.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cafi.firefly.common.util.Tools;
import com.cafi.firefly.service.ImgProcessingService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * @program: firefly
 * @description: ImgProcessingService实现类
 * @author: Miao
 * @create: 2021-03-13 16:10
 */
@Slf4j
@Service
public class ImgProcessingServiceImpl implements ImgProcessingService {
    @Value("${tencent.SecretId}")
    String SecretId;
    @Value("${tencent.SecretKey}")
    String SecretKey;


    @Override
    public String generalBasicOCR(String base64) {
        Credential cred = new Credential(SecretId, SecretKey);

        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");

        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);

        OcrClient client = new OcrClient(cred, "ap-beijing", clientProfile);

        GeneralBasicOCRRequest req = new GeneralBasicOCRRequest();
        req.setImageBase64(base64);
        try {
            GeneralBasicOCRResponse resp = client.GeneralBasicOCR(req);
            return GeneralBasicOCRResponse.toJsonString(resp);
        } catch (TencentCloudSDKException e) {
            log.error("调用腾讯AI接口返回异常：" + e.toString());
            return "调用腾讯AI接口失败";
        }
    }

    @Override
    public BufferedImage imageOverlays(ArrayList<Integer> arrayList, InputStream inputStream) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert image != null;
        Graphics g = image.getGraphics();
        Graphics2D g2D = (Graphics2D)g;
        g2D.setStroke(new BasicStroke(2.5f));
        g2D.setColor(Color.RED);//画笔颜色
        g2D.drawRect(arrayList.get(0),
                arrayList.get(1),
                arrayList.get(2),
                arrayList.get(3));//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)

        return image;
    }

    /*
{
  "Response": {
    "TextDetections": [
      {
        "DetectedText": "Confetteria",
        "Confidence": 99,
        "ItemPolygon": {
          "X": 473,
          "Y": 273,
          "Width": 112,
          "Height": 22
        },
        "Polygon": [
          {
            "X": 450,
            "Y": 211
          },
          {
            "X": 560,
            "Y": 223
          },
          {
            "X": 558,
            "Y": 244
          },
          {
            "X": 448,
            "Y": 232
          }
        ],
        "AdvancedInfo": "{\"Parag\":{\"ParagNo\":1}}"
      },
      {
        "DetectedText": "Raffaello",
        "Confidence": 99,
        "ItemPolygon": {
          "X": 396,
          "Y": 304,
          "Width": 282,
          "Height": 68
        },
        "Polygon": [
          {
            "X": 370,
            "Y": 233
          },
          {
            "X": 649,
            "Y": 265
          },
          {
            "X": 642,
            "Y": 331
          },
          {
            "X": 362,
            "Y": 299
          }
        ],
        "AdvancedInfo": "{\"Parag\":{\"ParagNo\":2}}"
      },
      {
        "DetectedText": "费列罗臻点坊",
        "Confidence": 99,
        "ItemPolygon": {
          "X": 437,
          "Y": 385,
          "Width": 188,
          "Height": 32
        },
        "Polygon": [
          {
            "X": 402,
            "Y": 318
          },
          {
            "X": 587,
            "Y": 339
          },
          {
            "X": 584,
            "Y": 370
          },
          {
            "X": 398,
            "Y": 349
          }
        ],
        "AdvancedInfo": "{\"Parag\":{\"ParagNo\":3}}"
      },
      {
        "DetectedText": "拉斐尔脆雪柔",
        "Confidence": 99,
        "ItemPolygon": {
          "X": 427,
          "Y": 435,
          "Width": 207,
          "Height": 34
        },
        "Polygon": [
          {
            "X": 386,
            "Y": 366
          },
          {
            "X": 591,
            "Y": 390
          },
          {
            "X": 587,
            "Y": 423
          },
          {
            "X": 382,
            "Y": 399
          }
        ],
        "AdvancedInfo": "{\"Parag\":{\"ParagNo\":3}}"
      }
    ],
    "Language": "zh",
    "Angel": 6.5,
    "PdfPageSize": 0,
    "RequestId": "03e66873-5209-4d26-abee-c4acd66fab91"
  }
}
     */
    @Override
    public ArrayList<Integer> getCoordinate(String tencentRsp, String note) {
        String targetStr = Tools.subString(note, "[", "]");
        ArrayList<Integer> arrayList = new ArrayList<>(4);
        JSONObject jsonObject = JSON.parseObject(tencentRsp);
        JSONArray jsonArray = jsonObject.getJSONArray("TextDetections");
        for (Object obj : jsonArray) {
            JSONObject singleData = JSON.parseObject(obj.toString());
            String detectedText = singleData.get("DetectedText").toString();
            if (!detectedText.isEmpty() && targetStr.equals(detectedText)) {
                JSONObject itemPolygonJson = singleData.getJSONObject("ItemPolygon");
                arrayList.add((Integer) itemPolygonJson.get("X"));
                arrayList.add((Integer) itemPolygonJson.get("Y"));
                arrayList.add((Integer) itemPolygonJson.get("Width"));
                arrayList.add((Integer) itemPolygonJson.get("Height"));
            }
        }
        return arrayList;
    }

    @Override
    public ByteArrayInputStream changeBase64ToInputStream(String base64) {
        //将字符串转换为byte数组
        byte[] bytes = new byte[0];
        try {
            bytes = new BASE64Decoder().decodeBuffer(base64.trim());
        } catch (IOException e) {
            log.error("图片base64转输入流失败：" + e.toString());
        }
        return new ByteArrayInputStream(bytes);
    }

}
