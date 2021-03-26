package com.cafi.firefly.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: FireFly-AI
 * @description: 获取SessionKey的类
 * @author: Miao
 * @create: 2021-03-26 21:24
 */
@Getter
@Setter
@ToString
public class SessionKeyVo {
    @ApiModelProperty(value="小程序id" ,required=true)
    String appid;
    @ApiModelProperty(value="secret" ,required=true)
    String secret;
    @ApiModelProperty(value="js代码" ,required=true)
    String js_code;
    @ApiModelProperty(value="类型" ,required=true)
    String grant_type;
}
