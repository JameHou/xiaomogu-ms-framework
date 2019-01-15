package com.github.xiaomogu.framework.feign.invoke;

import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.feign.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
@Component
@FeignClient(name = "WEB-SAMPLE" ,fallbackFactory = HelloFeignFallBackFactory.class)
public interface HelloFeignApi {

    @GetMapping(value = "/hello/getUserById",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<User> getUserById(@RequestParam("uid")  @NotNull(message = "请填写年龄")Long uid);

}
