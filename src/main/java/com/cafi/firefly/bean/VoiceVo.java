package com.cafi.firefly.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: FireFly-AI
 * @description: 语音合成
 * @author: Miao
 * @create: 2021-03-31 22:38
 */
@Getter
@Setter
@ToString
public class VoiceVo {
    @ApiModelProperty(value="要转语音的文本" ,required=true)
    String text;
}
