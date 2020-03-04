package com.zhangln.push.wspush.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Ws客户端注册对象
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Data
@ToString
public class WsRegVo implements Serializable {

    private String clientType = "";
    private String app = "";
    private String user = "";
    private String group = "";
    private String areaCode = "";
    private String country = "";

}
