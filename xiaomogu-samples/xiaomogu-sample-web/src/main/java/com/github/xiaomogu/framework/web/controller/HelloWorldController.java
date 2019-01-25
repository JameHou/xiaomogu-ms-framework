package com.github.xiaomogu.framework.web.controller;

import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.web.domain.User;
import com.github.xiaomogu.framework.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate template(){

        return new RestTemplate();
    }

    @Autowired
    private HzJson hzJson;

/*
    @Autowired
    @CustomQualifier
    private User user;*/
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
            log.info("restTemplate info {}",restTemplate);
            return ResponseResult.success(userService.findUserById(uid));
        }catch (Exception e){
            return ResponseResult.fail(-1,"查询用户异常"+e.getMessage());
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id" ,name = "uid" ),
            @ApiImplicitParam(value = "用户name" ,name = "name" ),
            @ApiImplicitParam(value = "用户mobile" ,name = "mobile" )
    })
    @GetMapping(value = "/updateUserId",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<User> updateUserId(@RequestParam("uid")  @NotNull(message = "请填写userId")Long uid,
                                             @RequestParam("name")  @NotNull(message = "请填写name")String name,
                                             @RequestParam("mobile")  @NotNull(message = "请填写mobile")String mobile){
        return ResponseResult.success(userService.updateUserById(uid,name,mobile));
    }



   /** @CustomQualifier
    @Bean
    public User user1(){
        User user = new User();
        user.setName("houzhen1");
        return user;
    }

    @Bean(name = "myString")
    String string(){
        return new String("hello");
    }

    @Bean
    @LoadBalanced
    public User user2(){
        User user = new User();
        user.setName("houzhen2");
        return user;
    }



    @Bean
    public Object CoustomUserBeans(@CustomQualifier List<User> userList){
        userList.stream().forEach(a-> System.out.println(a.getName()));
        return new Object();
    }*/



}
