package com.github.xiaomogu.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @auther JameHou
 * @date 2019/1/6 20:47
 */
@Data
@ConfigurationProperties(value = "mogu-web.mvc")
public class SpringMvcProperties {


    private int payloadLength = 2048;




}
