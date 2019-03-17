package com.github.xiaomogu.framework.web.common;

import lombok.Data;

/**
 * @auther JameHou
 * @date 2019/3/17 15:09
 */
@Data
public class SentinelFlowRuleProperties {
    private String resourceName	;//资源名，资源名是限流规则的作用对象
    private int count = 500;	//限流阈值
    private int grade;	//限流阈值类型，QPS 或线程数模式	QPS 模式

}
