package com.cafi.firefly.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: FireFly-AI
 * @description: 用户身份标识（手机号）
 * @author: Miao
 * @create: 2021-03-31 21:29
 */
@Getter
@Setter
@ToString
public class UserIdVo {
    @ApiModelProperty(value="用户身份标识（手机号）" ,required=true)
    String userPhoneNumber;
}
