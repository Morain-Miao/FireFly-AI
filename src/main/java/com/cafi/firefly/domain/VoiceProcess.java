package com.cafi.firefly.domain;

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
    public TextToVoiceResponse getVoice(String text){
        try{
            Credential cred = new Credential(SecretId, SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tts.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            //region 地域列表
            TtsClient client = new TtsClient(cred, "ap-beijing", clientProfile);
            TextToVoiceRequest req = new TextToVoiceRequest();
            req.setText(text);
            req.setSessionId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            req.setModelType(1L);
            TextToVoiceResponse resp = client.TextToVoice(req);
            log.info(TextToVoiceResponse.toJsonString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            log.error(e.toString());
            return null;
        }
    }
}
