package com.github.xiaomogu.framework.web.service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.github.xiaomogu.commons.exception.MicroServiceException;
import com.github.xiaomogu.commons.jackson.RespResultCode;
import com.github.xiaomogu.framework.web.controller.ExceptionUtil;
import com.github.xiaomogu.framework.web.dao.read.UserReadMapper;
import com.github.xiaomogu.framework.web.dao.write.UserWriteMapper;
import com.github.xiaomogu.framework.web.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auther JameHou
 * @date 2019/1/9 19:15
 */
@Service
public class UserService {

    @Autowired
    private UserReadMapper readMapper;

    @Autowired
    private UserWriteMapper writeMapper;


    @SentinelResource(value = "findUserById",entryType = EntryType.IN,blockHandler = "handleException", blockHandlerClass={ExceptionUtil.class})
    public User findUserById(Long uid){
        User user = new User();
        user.setName("测试限流");
        user.setMobile("2233333");
        return user;
        //return readMapper.findUserById(uid);
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public void exceptionHandler(Long uid, BlockException ex) {
        // Do some log here.
        System.out.println("限流异常................参数："+uid);
        ex.printStackTrace();
    }

   @Transactional(value = "writeNodeTx")
    public User updateUserById(Long uid,String name,String mobile){
        /*User user = readMapper.findUserById(uid);
        user.setName(name);
        user.setMobile(mobile);
        int rows = writeMapper.updateUserById(user);
        if(rows > 0 ){
            throw new MicroServiceException(RespResultCode.FAIL.getCode(),"回滚测试");
        }
        throw new MicroServiceException(RespResultCode.FAIL.getCode(),"修改user失败");*/
        return updateUserById2( uid, name, mobile);
    }

    @Transactional(value = "writeNodeTx")
    public User updateUserById2(Long uid,String name,String mobile){
        User user = readMapper.findUserById(uid);
        user.setName(name);
        user.setMobile(mobile);
        int rows = writeMapper.updateUserById(user);
        if(rows > 0 ){
            throw new MicroServiceException(RespResultCode.FAIL.getCode(),"回滚测试");
        }
        throw new MicroServiceException(RespResultCode.FAIL.getCode(),"修改user失败");
    }

}
