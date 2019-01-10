package com.github.xiaomogu.datasource.jdbc;

import lombok.Data;

/**
 * @auther JameHou
 * @date 2019/1/8 18:28
 */
@Data
public class DruidPoolConfig {

    private int initialSize;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int timeBetweenEvictionRunsMillis;

    private int minEvictableIdleTimeMillis;

    private String validationQuery;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

    private int maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    private String connectionProperties;

}
