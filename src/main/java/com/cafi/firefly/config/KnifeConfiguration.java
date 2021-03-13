package com.cafi.firefly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @program: firefly
 * @description: knife配置类
 * @author: Miao
 * @create: 2021-03-13 15:52
 */
@Configuration
@EnableSwagger2WebMvc
public class KnifeConfiguration {
    @Bean
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("0.1版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.cafi.firefly.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("米粒之光智能引导")
                .description("示例Restful API")
                //服务Url
                .termsOfServiceUrl("http://localhost:9001/doc.html")
                .contact("CAFI小队")
                .version("0.0.1-SNAPSHOT")
                .build();
    }
}
