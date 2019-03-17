package com.github.xiaomogu.framework.web.common;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.github.xiaomogu.commons.utils.EnvironmentUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @auther JameHou
 * @date 2019/3/17 13:07
 */
@Configuration
@ConditionalOnClass({FlowRuleManager.class,FlowRule.class, SentinelResourceAspect.class})
public class SentinelFlowRuleConfig implements EnvironmentAware {
    private ConfigurableEnvironment environment;
    private void initFlowRules() {
        MultiSentinelFlowRuleProperties ruleProperties = EnvironmentUtil.
                resolverSetting(MultiSentinelFlowRuleProperties.class, "", environment);
        List<FlowRule> rules = new ArrayList<>();
        ruleProperties.getSentinelFlowRule().forEach((name,properties)->{
            FlowRule rule = new FlowRule();
            Optional.ofNullable(properties.getResourceName()).ifPresent(rule::setResource);
            rule.setCount(properties.getCount());
            rule.setGrade(properties.getGrade());
            rule.setLimitApp("default");
            rules.add(rule);
        });
        FlowRuleManager.loadRules(rules);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
        initFlowRules();
    }

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

}
