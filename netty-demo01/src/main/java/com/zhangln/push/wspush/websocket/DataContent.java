package com.zhangln.push.wspush.websocket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * WebSocket客户端请求对象
 * @author zhangliuning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataContent implements Serializable {

    /**
     * 动作类型,参考消息类型的枚举，定义见：MsgActionEnum
     */
    private Integer action;

    /**
     * 认证id
     */
    private String tokenId;

    /**
     * 传递过来的消息，json形式的字符串，具体内容格式，由action类型确定
     */
    private String jsonObjStr;
    /**
     * 扩展字段
     */
    private String extend;
}