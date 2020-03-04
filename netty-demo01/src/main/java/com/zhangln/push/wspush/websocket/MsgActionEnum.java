package com.zhangln.push.wspush.websocket;

/**
 * 发送消息类型的枚举
 */
public enum MsgActionEnum {

    CONNECT(1, "第一次(或重连)初始化连接"),
    KEEPALIVE(2, "客户端保持心跳"),
    CLIENT_ERROR(3, "客户端异常信息反馈"),
    PAGE_CHANGE(4, "客户端页面切换反馈");

    public final Integer type;
    public final String content;

    MsgActionEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }
}