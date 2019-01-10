package com.github.xiaomogu.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @auther JameHou
 * @date 2019/1/8 18:54
 */
@Slf4j
public class EnvironmentUtil {


    // 读取配置并转换成对象
    public static <T> T resolverSetting(Class<T> clazz, String targetName,
                                        ConfigurableEnvironment environment) {
        try {
            return Binder.get(environment)
                    .bind(targetName, Bindable.of(clazz))
                    .orElseThrow(
                            () -> new FatalBeanException("Could not bind properties"));
        } catch (Exception e) {
            //ignore
            log.error("Could not bind properties: " + e.getMessage(), e);
            throw new FatalBeanException("Could not bind properties", e);
        }
    }

    /**
     * 注册bean的定义信息 ，供后续spring实例化bean使用
     *
     * @param beanFactory
     * @param bean
     * @param name
     * @param alias
     */
    public static void register(ConfigurableListableBeanFactory beanFactory, Object bean, String name,
                                String alias) {
        beanFactory.registerSingleton(name, bean);
        if (!beanFactory.containsSingleton(alias)) {
            beanFactory.registerAlias(name, alias);
        }
    }
}
