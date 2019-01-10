package com.github.xiaomogu.framework.web.dao.write;

import com.github.xiaomogu.framework.web.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @auther JameHou
 * @date 2019/1/9 19:18
 */
@Repository
public interface UserWriteMapper {

    User updateUserById(@Param("uid") Long uid);
}
