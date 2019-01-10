package com.github.xiaomogu.commons.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther JameHou
 * @date 2019/1/6 20:59
 */
@Configuration
@ConditionalOnBean(ObjectMapper.class)
@AutoConfigureAfter(name = "org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration")
public class JsonConfig {

    @Bean
    @ConditionalOnMissingBean
    public HzJson jackson(ObjectMapper objectMapper) {
        return new HzJson(objectMapper);
    }
}
