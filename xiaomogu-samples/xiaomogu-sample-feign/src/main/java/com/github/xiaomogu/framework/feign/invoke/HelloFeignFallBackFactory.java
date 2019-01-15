package com.github.xiaomogu.framework.feign.invoke;


import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.feign.domain.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther JameHou
 * @date 2019/1/15 12:07
 */
@Slf4j
public class HelloFeignFallBackFactory implements FallbackFactory<HelloFeignApi> {


    @Override
    public HelloFeignApi create(Throwable cause) {
        return new HelloFeignApi() {
            @Override
            public ResponseResult<User> getUserById(Long uid) {
                log.error("getUserById 熔断处理:{}", cause);
                return null;
            }
        };
    }
}
