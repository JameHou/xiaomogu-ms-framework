package com.github.xiaomogu.commons.jackson;

import lombok.Getter;

public enum  RespResultCode {
    SUCCESS(0, "success"),
    FAIL(-1, "系统异常"),
    INNER_ERROR(500, "服务器内部错误");

    @Getter private int code;
    @Getter private String msg;
    RespResultCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
