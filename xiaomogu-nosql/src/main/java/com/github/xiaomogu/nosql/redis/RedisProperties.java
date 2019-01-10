package com.github.xiaomogu.nosql.redis;

import lombok.Data;

import java.util.List;

/**
 * @date 2019/1/9 17:19
 */
@Data
public class RedisProperties {
    private String host;
    private String port;
    private List<String> nodes;
}
