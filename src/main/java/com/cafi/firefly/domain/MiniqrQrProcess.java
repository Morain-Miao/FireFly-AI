package com.cafi.firefly.domain;

import com.cafi.firefly.bean.ImgEntityVo;
import com.cafi.firefly.bean.UserIdVo;
import com.cafi.firefly.service.ImgProcessingService;
import com.cafi.firefly.service.MiniqrQrService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @program: FireFly-AI
 * @description: 获取小程序二维码
 * @author: Miao
 * @create: 2021-03-31 21:22
 */
@Slf4j
@Component
public class MiniqrQrProcess {
    @Autowired
    MiniqrQrService miniqrQrService;
    @Autowired
    ImgMainProcess imgMainProcess;

    public String MiniqrQrProcessed(UserIdVo userIdVo, HttpServletResponse response) {
        String token;
        try {
            token = miniqrQrService.postToken();
        } catch (Exception e) {
            log.error("获取token异常：" + e);
            return null;
        }
        BufferedInputStream inputStream = miniqrQrService.getMiniqrQr(userIdVo.getUserPhoneNumber(), token);
        try {
            String result = IOUtils.toString(inputStream, "UTF-8");
            log.info(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ImgMainProcess.BufferedImageToBase64(image);
    }
}
