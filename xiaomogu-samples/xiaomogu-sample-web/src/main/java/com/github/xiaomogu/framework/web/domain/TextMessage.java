package com.github.xiaomogu.framework.web.domain;

import lombok.Data;

/**
 * @auther JameHou
 * @date 2019/2/1 9:53
 */
@Data
public class TextMessage {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType;
    private String Content;
    private Long MsgId;
}
