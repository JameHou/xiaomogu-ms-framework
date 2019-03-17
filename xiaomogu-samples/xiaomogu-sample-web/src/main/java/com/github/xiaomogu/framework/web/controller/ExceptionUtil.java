package com.github.xiaomogu.framework.web.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.github.xiaomogu.framework.web.domain.User;

/**
 * @auther JameHou
 * @date 2019/3/17 17:08
 */
public class ExceptionUtil {
    public static User handleException(Long uid,BlockException ex) {
        System.out.println("Oops: " + ex.getClass().getCanonicalName()+"uid:"+uid);
        return null;
    }
}
