package com.github.xiaomogu.framework.web.service;

import com.github.xiaomogu.commons.exception.MicroServiceException;
import com.github.xiaomogu.commons.jackson.RespResultCode;
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



    public User findUserById(Long uid){
        return readMapper.findUserById(uid);
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
