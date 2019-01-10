package com.github.xiaomogu.commons.jackson;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ResponseResult<T>  {
    @JsonView(ResponseResultView.class)
    private int code;
    @JsonView(ResponseResultView.class)
    private String msg;
    @JsonView(ResponseResultView.class)
    private T data;

    public ResponseResult(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ResponseResult(){
        this.code = RespResultCode.SUCCESS.getCode();
        this.msg = RespResultCode.SUCCESS.getMsg();
    }

    public interface ResponseResultView {

    }

    public static <T> ResponseResult<T> success(){
        return new ResponseResult<>();
    }


    public static <T> ResponseResult<T> success(T data){
        return (ResponseResult<T>) ResponseResult.builder()
                .code(RespResultCode.SUCCESS.getCode()).msg(RespResultCode.SUCCESS.getMsg())
                .data(data)
                .build();
    }

    public static <T> ResponseResult<T> fail(){
        return fail(RespResultCode.FAIL.getCode(),RespResultCode.FAIL.getMsg());
    }

    public static <T> ResponseResult<T> fail(String msg){
        return fail(RespResultCode.FAIL.getCode(),msg);
    }

    public static <T> ResponseResult<T> fail(int code,String message){
        return new ResponseResult<>(code,message);
    }
}
