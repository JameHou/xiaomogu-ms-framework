package com.github.xiaomogu.framework.web.common;

import lombok.Data;

import java.util.Map;

/**
 * @auther JameHou
 * @date 2019/3/17 13:09
 */
@Data
public class MultiSentinelFlowRuleProperties {
    Map<String,SentinelFlowRuleProperties> sentinelFlowRule;
}
