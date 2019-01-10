package com.github.xiaomogu.datasource.jdbc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * mysql 多数据源配置
 */
@Data
public class MultiMysqlProperties {
    private Map<String, DataSourceProperties> moguMysql;
}
