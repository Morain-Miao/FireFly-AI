package com.cafi.firefly.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: FireFly-AI
 * @description:
 * @author: Miao
 * @create: 2021-03-26 21:28
 */
@Getter
@Setter
@ToString
public class UserPhoneNumberVo {
    @ApiModelProperty(value="encrypdata" ,required=true)
    String encrypdata;
    @ApiModelProperty(value="ivdata" ,required=true)
    String ivdata;
    @ApiModelProperty(value="sessionkey" ,required=true)
    String sessionkey;
}
