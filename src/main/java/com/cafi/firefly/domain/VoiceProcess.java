package com.cafi.firefly.domain;

import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tts.v20190823.TtsClient;
import com.tencentcloudapi.tts.v20190823.models.TextToVoiceRequest;
import com.tencentcloudapi.tts.v20190823.models.TextToVoiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * @program: FireFly-AI
 * @description: 语音合成
 * @author: Miao
 * @create: 2021-03-31 22:16
 */
@Component
@Slf4j
public class VoiceProcess {
    @Value("${tencent.SecretId}")
    String SecretId;
    @Value("${tencent.SecretKey}")
    String SecretKey;

    /**
     *
     * @param text 要转语音的文本
     * @return Json字符串
     * Audio    String	base64编码的wav/mp3音频数据
     * SessionId	String	一次请求对应一个SessionId
     * RequestId	String	唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId
     */
    public JSONObject getVoice(String text){
        try{
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            Credential cred = new Credential(SecretId, SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tts.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            //region 地域列表
            TtsClient client = new TtsClient(cred, "ap-beijing", clientProfile);
            TextToVoiceRequest req = new TextToVoiceRequest();
            req.setText(text);
            req.setSessionId(uuid);
            req.setModelType(1L);
            TextToVoiceResponse resp = client.TextToVoice(req);
            base64ToFile(resp.getAudio(),uuid+".mp3","/home/fireflyApp/temp/");
//            log.info(TextToVoiceResponse.toJsonString(resp));
            JSONObject responseJson = new JSONObject();
            responseJson.put("url","http://119.23.202.210:8080/temp/"+uuid+".mp3");
            return responseJson;
        } catch (TencentCloudSDKException e) {
            log.error(e.toString());
            return null;
        }
    }


    public static void base64ToFile(String base64, String fileName, String savePath) {
        File file = null;
        //创建文件目录
        String filePath = savePath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(filePath + fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
            }
        }
    }
}
