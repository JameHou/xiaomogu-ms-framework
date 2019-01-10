package com.github.xiaomogu.commons.exception;

import lombok.Data;

@Data
public class MicroServiceException extends RuntimeException {

    private String msg;
    private int code = -1;

    public MicroServiceException(String msg){
        super(msg);
        this.msg = msg;
    }


    public MicroServiceException(int code , String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }


    public MicroServiceException(int code, String msg, Throwable e){
        super(msg,e);
        this.msg = msg;
        this.code = code;
    }
}
