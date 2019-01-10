package com.github.xiaomogu.datasource.mybatis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @auther JameHou
 * @date 2019/1/9 15:25
 */
@Data
public class MultiMybatisProperties {

    private Map<String,MybatisProperties> moguMysql;
}
