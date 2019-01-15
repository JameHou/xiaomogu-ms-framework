package com.github.xiaomogu.framework.feign.dao.read;

import com.github.xiaomogu.framework.feign.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @auther JameHou
 * @date 2019/1/9 19:17
 */
@Repository
public interface UserReadMapper {

    User findUserById(@Param("uid") Long uid);
}
