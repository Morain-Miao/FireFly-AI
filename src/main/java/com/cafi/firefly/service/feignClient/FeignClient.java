package com.cafi.firefly.service.feignClient;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @program: FireFly-AI
 * @description:
 * @author: Miao
 * @create: 2021-03-26 21:09
 */
@org.springframework.cloud.openfeign.FeignClient(url = "https://api.weixin.qq.com",name = "getSessionKey")
public interface FeignClient {

    @GetMapping(value = "/sns/jscode2session",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    JSONObject getSessionKey(@RequestParam String appid,
                             @RequestParam String secret,
                             @RequestParam String js_code,
                             @RequestParam String grant_type);
}
