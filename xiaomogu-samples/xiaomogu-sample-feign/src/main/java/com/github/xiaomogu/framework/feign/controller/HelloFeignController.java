package com.github.xiaomogu.framework.feign.controller;

import com.github.xiaomogu.commons.jackson.RespResultCode;
import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.feign.domain.User;
import com.github.xiaomogu.framework.feign.invoke.HelloFeignApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/hello")
@Api(tags = "Hello World")
@Slf4j
@Validated
public class HelloFeignController {
   /* @Autowired
    private UserService userService;*/
    @Autowired
    private HelloFeignApi helloFeignApi;


    @ApiImplicitParams({
        @ApiImplicitParam(value = "用户id" ,name = "uid" )
    })
    @GetMapping(value = "/getUserById",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<User> getUserById(@RequestParam("uid")  @NotNull(message = "请填写年龄")Long uid){
        try {
            return helloFeignApi.getUserById(uid);
        }catch (Exception e){
            log.error("feign查询用户出错：{}",e);
            return ResponseResult.fail(RespResultCode.INNER_ERROR.getCode(),e.getMessage());
        }
    }

}
