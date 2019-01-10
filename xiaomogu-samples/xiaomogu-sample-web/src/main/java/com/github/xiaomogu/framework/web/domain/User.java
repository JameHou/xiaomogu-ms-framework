package com.github.xiaomogu.framework.web.domain;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("user")
public class User {
    private int age;
    private String name;
    private String mobile;

}
