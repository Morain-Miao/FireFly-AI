package com.cafi.firefly.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: firefly
 * @description: 图片Vo实体类
 * @author: Miao
 * @create: 2021-03-13 16:56
 */
@Getter
@Setter
@ToString
public class ImgEntityVo {
    @ApiModelProperty(value="商家电话唯一标志" ,required=true)
    String phoneNum;
    @ApiModelProperty(value="第几步的图片" ,required=true)
    int number;
    @ApiModelProperty(value="商家图片说明" ,required=true)
    String note;
    @ApiModelProperty(value="图片的base64编码" ,required=true)
    String imgBase64;
}
