package com.github.xiaomogu.web.config;

import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.commons.jackson.JsonConfig;
import com.github.xiaomogu.web.log.AccessLogFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.web.servlet.WebMvcMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

/**
 * @auther JameHou
 * @date 2019/1/6 22:23
 */
@Configuration
@ConditionalOnBean({HzJson.class})
@ConditionalOnWebApplication
@AutoConfigureAfter({
        JsonConfig.class, SpringContextConfig.class, WebMvcMetricsAutoConfiguration.class
})
@EnableConfigurationProperties(SpringMvcProperties.class)
public class SpringMvcConfig {

    @Bean
    @ConditionalOnClass(OncePerRequestFilter.class)
    @ConditionalOnProperty(value = "mogo-web.log.enabled", matchIfMissing = true)
    public FilterRegistrationBean<AccessLogFilter> accessLogFilter(HzJson hzJson,SpringMvcProperties springMvcProperties) {
        FilterRegistrationBean<AccessLogFilter> registration = new FilterRegistrationBean<>(
                AccessLogFilter.of(springMvcProperties, hzJson));
        registration.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return registration;
    }

    @Bean
    @ConditionalOnClass(CorsFilter.class)
    @ConditionalOnProperty(value = "mogo-web.cors.enabled", matchIfMissing = true)
    public Filter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }


}
