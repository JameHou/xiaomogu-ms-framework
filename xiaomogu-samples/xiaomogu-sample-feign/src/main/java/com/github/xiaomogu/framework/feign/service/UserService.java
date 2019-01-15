package com.github.xiaomogu.framework.feign.service;

import com.github.xiaomogu.framework.feign.dao.read.UserReadMapper;
import com.github.xiaomogu.framework.feign.dao.write.UserWriteMapper;
import com.github.xiaomogu.framework.feign.domain.User;
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

    @Transactional(value = "writeTx")
    public User updateUserById(Long uid){
        return writeMapper.updateUserById(uid);
    }

}
