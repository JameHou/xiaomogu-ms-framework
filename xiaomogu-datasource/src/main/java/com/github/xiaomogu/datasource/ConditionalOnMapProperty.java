package com.github.xiaomogu.datasource;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * 自定义条件装配
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(MapPropertyCondition.class)
public @interface ConditionalOnMapProperty {

  String prefix() default "";

  Class<?> value();
}