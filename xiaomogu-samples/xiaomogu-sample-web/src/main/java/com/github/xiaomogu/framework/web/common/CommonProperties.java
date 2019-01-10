package com.github.xiaomogu.framework.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @auther JameHou
 * @date 2019/1/9 19:26
 */
@Component
@Data
@ConfigurationProperties(prefix = "test-web")
public class CommonProperties {

    @NotNull(message = "测试userName不能为空")
    private String userName;
    @NotNull(message = "age不能为空")
    private String age;
    @NotNull(message = "mobile不能为空")
    private String mobile;

}
