package com.github.xiaomogu.framework.web.controller;

import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.web.domain.User;
import com.github.xiaomogu.framework.web.service.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/hello")
@Api(tags = "Hello World")
@Slf4j
@Validated
public class HelloWorldController {
    @Autowired
    private UserService userService;
    @ApiImplicitParams({
            @ApiImplicitParam(value = "姓名" ,name = "name" ),
            @ApiImplicitParam(value = "年龄" ,name = "age" )
        }
    )
    @GetMapping(value = "/getUser",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<User> getUser(@RequestParam("name") @NotNull(message = "请填写名称") String name,
                                        @RequestParam("age")  @NotNull(message = "请填写年龄")Integer age , HttpServletRequest request){
        User user = new User();
        user.setAge(age == null ? 23 : age);
        user.setMobile("18900009999");
        user.setName(name == null?"JameHou":name);
        log.info("请求信息_{}",request);
       // throw new MicroServiceException(999,"系统异常");
        return ResponseResult.success(user);
    }


    @ApiImplicitParams({
        @ApiImplicitParam(value = "用户id" ,name = "uid" )
    })
    @GetMapping(value = "/getUserById",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<User> getUserById(@RequestParam("uid")  @NotNull(message = "请填写年龄")Long uid){
        try {
            return ResponseResult.success(userService.findUserById(uid));
        }catch (Exception e){
            return ResponseResult.fail(-1,"查询用户异常"+e.getMessage());
        }
    }

}
