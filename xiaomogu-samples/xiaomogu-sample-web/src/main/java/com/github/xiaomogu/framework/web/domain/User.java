package com.github.xiaomogu.framework.web.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Data
@Alias("user")
@ToString
public class User {
    private Long id;
    private int age;
    private String name;
    private String mobile;

}
