package com.github.xiaomogu.framework.feign.dao.write;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.github.xiaomogu.framework.feign.domain.User;

/**
 * @auther JameHou
 * @date 2019/1/9 19:18
 */
@Repository
public interface UserWriteMapper {

    User updateUserById(@Param("uid") Long uid);
}
