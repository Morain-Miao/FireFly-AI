package com.cafi.firefly.controller;

import com.alibaba.fastjson.JSONObject;
import com.cafi.firefly.bean.ImgEntityVo;
import com.cafi.firefly.bean.VoiceVo;
import com.cafi.firefly.domain.ImgMainProcess;
import com.cafi.firefly.domain.VoiceProcess;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tts.v20190823.models.TextToVoiceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @program: firefly
 * @description: 主要Controller入口
 * @author: Miao
 * @create: 2021-03-13 15:50
 */
@Slf4j
@RestController
@RequestMapping(value = "/img/processing")
@Api(value = "MainController|图片处理接口")
public class MainController {
    @Autowired
    ImgMainProcess imgMainProcess;

    @Autowired
    VoiceProcess voiceProcess;

    @ApiOperation(value = "图片处理接口", notes = "")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel",properties = {
                    @DynamicParameter(name = "request",value = "请求体",example = "{}",required = true,dataTypeClass = ImgEntityVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @RequestMapping(value = "/getImg")
    public JSONObject getClassQr(@RequestBody ImgEntityVo request, HttpServletResponse response) {
        // 设置响应流信息
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
//        response.setContentType("application/octet-stream");
        String result = imgMainProcess.imgProcessed(request, response);
        JSONObject responseJson = new JSONObject();
        if (result != null){
            responseJson.put("status", "0");
            responseJson.put("data", result);
        }else{
            responseJson.put("status", "1");
            responseJson.put("data", "调用图片处理接口失败");
        }
        return responseJson;

    }

    @ApiOperation(value = "获取语音合成", notes = "     * Audio    String\tbase64编码的wav/mp3音频数据\n" +
            "     * SessionId\tString\t一次请求对应一个SessionId\n" +
            "     * RequestId\tString\t唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel",properties = {
                    @DynamicParameter(name = "request",value = "请求体",example = "{}",required = true,dataTypeClass = ImgEntityVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @RequestMapping(value = "/getVoice")
    public TextToVoiceResponse getVoice(@RequestBody VoiceVo voiceVo) {
        return voiceProcess.getVoice(voiceVo.getText());
    }


}
