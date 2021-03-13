package com.cafi.firefly.controller;

import com.cafi.firefly.bean.ImgEntityVo;
import com.cafi.firefly.domain.ImgMainProcess;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicParameters;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
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

    @ApiOperation(value = "图片处理接口", notes = "")
    @ApiOperationSupport(
            author = "万能的苗",
            params = @DynamicParameters(name = "CreateOrderHashMapModel",properties = {
                    @DynamicParameter(name = "request",value = "请求体",example = "{}",required = true,dataTypeClass = ImgEntityVo.class),
            }),
            responses = @DynamicResponseParameters(name = "")
    )
    @RequestMapping(value = "/getImg")
    public void getClassQr(@RequestBody ImgEntityVo request, HttpServletResponse response) {
        // 设置响应流信息
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
//        response.setContentType("application/octet-stream");
        boolean result = imgMainProcess.imgProcessed(request, response);
        if (!result){
            String data = "{\"status\":\"0\",\"data\":\"调用图片处理接口失败\"}";
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
}
