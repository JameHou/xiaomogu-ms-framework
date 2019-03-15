package com.github.xiaomogu.framework.web.controller;

import com.github.xiaomogu.commons.jackson.HzJson;
import com.github.xiaomogu.commons.jackson.ResponseResult;
import com.github.xiaomogu.framework.web.domain.TextMessage;
import com.github.xiaomogu.framework.web.domain.User;
import com.github.xiaomogu.framework.web.service.UserService;
import com.github.xiaomogu.framework.web.util.WxCheckUtil;
import com.github.xiaomogu.framework.web.util.XmlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
@Api(tags = "Hello World")
@Slf4j
@Validated
public class HelloWorldController {
    @Autowired
    private UserService userService;

    @Autowired
    private OkHttpClient httpClient;

   /* @Autowired
    private RestTemplate restTemplate;*/

    @Bean
    //@LoadBalanced
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
           // log.info("restTemplate info {}",restTemplate);
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


    @ApiImplicitParams({
            @ApiImplicitParam(value = "微信加密签名" ,name = "signature" ),
            @ApiImplicitParam(value = "时间戳" ,name = "timestamp" ),
            @ApiImplicitParam(value = "随机数" ,name = "nonce" ),
            @ApiImplicitParam(value = "随机字符串" ,name = "echostr" )
    })
    @GetMapping(value = "/welcome/wx")
    public String welcomeWx(@RequestParam("signature")  String signature,
                                             @RequestParam("timestamp")  String timestamp,
                                             @RequestParam("nonce") String nonce,@RequestParam("echostr") String echostr){
        boolean checkSignature = WxCheckUtil.checkSignature(signature,timestamp,nonce);
        if(!checkSignature){
            return null;
        }
        return echostr;
    }


    @PostMapping(value = "/welcome/wx")
    public void welcomeWx(HttpServletRequest request, HttpServletResponse response){

        PrintWriter printWriter = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<String, String>  msgMap = XmlUtils.parseXml(request);
            log.info(hzJson.obj2string(msgMap));

            String content = getContentFromRobbte(msgMap.get("Content"));
            String returnMsg = setTextMess(content,msgMap.get("ToUserName"),msgMap.get("FromUserName"));
            printWriter = response.getWriter();
            printWriter.println(returnMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    public String getContentFromRobbte(String content){
        String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg="+content;
        Request request = new Request.Builder().url(url).build();
        String returnMsg = "";
        try {
            Response response = httpClient.newCall(request).execute();
            returnMsg = response.body().string();
            System.out.println(returnMsg);
            Map<String,Object> map = hzJson.str2obj(returnMsg, HashMap.class);
            returnMsg = (String)map.get("content");
        } catch (IOException e) {
            e.printStackTrace();
            returnMsg = "找不道消息😀";
        }
        return returnMsg;

    }


    public String setTextMess(String content, String fromUserName, String toUserName) {
        TextMessage textMessage = new TextMessage();
        textMessage.setFromUserName(fromUserName);
        textMessage.setToUserName(toUserName);
        textMessage.setContent(content);
        textMessage.setMsgType("text");
        textMessage.setCreateTime(new Date().getTime());
        String messageToXml = XmlUtils.messageToXml(textMessage);
        log.info("####setTextMess()###messageToXml:" + messageToXml);
        return messageToXml;
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
