package com.zhangln.push.wspush.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 推送给WebSocket客户端的通用消息格式
 *
 * @author sherry
 * @description
 * @date Create in 2019-08-13
 * @modified By:
 */
@Data
@Builder
public class WsRespVo implements Serializable {

    /**
     * 推送id
     */
    private String id;
    /**
     * 时间，yyyy-MM-dd hh:mm:ss
     */
    private String date;
    /**
     * 推送状态
     * 200-正常
     * 500-异常
     */
    private Integer code;
    /**
     * 状态描述
     */
    private String msg;

    /**
     * 推送类型
     */
    private String pushType;

    /**
     * 推送的数据
     */
    private Object data;

}
