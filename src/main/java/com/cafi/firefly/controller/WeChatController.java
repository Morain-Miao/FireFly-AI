package com.cafi.firefly.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cafi.firefly.bean.ImgEntityVo;
import com.cafi.firefly.bean.SessionKeyVo;
import com.cafi.firefly.bean.UserIdVo;
import com.cafi.firefly.bean.UserPhoneNumberVo;
import com.cafi.firefly.domain.MiniqrQrProcess;
import com.cafi.firefly.service.MiniqrQrService;
import com.cafi.firefly.service.feignClient.FeignClient;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;

/**
 * @program: FireFly-AI
 * @description: 微信小程序相关接口
 * @author: Miao
 * @create: 2021-03-26 20:46
 */
@Slf4j
@RestController
@RequestMapping(value = "/wechat/processing")
@Api(value = "WeChatController|微信小程序相关处理接口")
public class WeChatController {

    @Autowired
    FeignClient feignClient;

    @Autowired
    MiniqrQrProcess miniqrQrProcess;

    @ApiOperation(value = "获取SessionKey接口", notes = "")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel", properties = {
                    @DynamicParameter(name = "request", value = "请求体", example = "{}", required = true, dataTypeClass = SessionKeyVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @PostMapping(value = "/getSessionKey")
    @ResponseBody
    public JSONObject getsessionkey(@RequestBody SessionKeyVo sessionKeyVo) throws Exception {
        // 发送请求
        JSONObject weChatResponse =
                feignClient.getSessionKey(sessionKeyVo.getAppid(),
                        sessionKeyVo.getSecret(),
                        sessionKeyVo.getJs_code(),
                        sessionKeyVo.getGrant_type());
        // 获取会话密钥（session_key）
        String session_key = weChatResponse.get("session_key").toString();

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", "success");
        responseJson.put("session_key", session_key);
        return responseJson;

    }

    @ApiOperation(value = "获取用户手机号接口", notes = "")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel", properties = {
                    @DynamicParameter(name = "request", value = "请求体", example = "{}", required = true, dataTypeClass = UserPhoneNumberVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @PostMapping(value = "/getPhoneNumber")
    @ResponseBody
    public String deciphering(@RequestBody UserPhoneNumberVo userPhoneNumberVo) throws Exception {
        byte[] encrypData = Base64.decodeBase64(userPhoneNumberVo.getEncrypdata());
        byte[] ivData = Base64.decodeBase64(userPhoneNumberVo.getIvdata());
        byte[] sKey = Base64.decodeBase64(userPhoneNumberVo.getSessionkey());
        return decrypt(sKey, ivData, encrypData);

    }

    @ApiOperation(value = "获取小程序二维码接口", notes = "")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel", properties = {
                    @DynamicParameter(name = "request", value = "请求体", example = "{}", required = true, dataTypeClass = UserPhoneNumberVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @PostMapping(value = "/getMiniqrQr")
    @ResponseBody
    public void getMiniqrQr(@RequestBody UserIdVo userIdVo, HttpServletResponse response) throws Exception {
        boolean result = miniqrQrProcess.MiniqrQrProcessed(userIdVo, response);
        if (!result){
            String data = "{\"status\":\"0\",\"data\":\"获取小程序二维码失败\"}";
            try {
                OutputStream outputStream = response.getOutputStream();
                response.setHeader("content-type", "application/json;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
                byte[] dataByteArr = data.getBytes(StandardCharsets.UTF_8);//将字符转换成字节数组，指定以UTF-8编码进行转换
                outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
            } catch (IOException e) {
                log.error("输出响应异常："+e.toString());
            }
        }
    }


    /**
     * 解密方法
     *
     * @param key
     * @param iv
     * @param encData
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData), StandardCharsets.UTF_8);
    }
}
