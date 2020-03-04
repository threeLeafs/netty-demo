package com.zhangln.push.wspush.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * http推送请求通用类
 *
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Data
@ToString
public class HttpWsPushCondition implements Serializable {

    private String pushId;
    private String clientType = "";
    private String app = "";
    private String user = "";
    private String group = "";
    private String areaCode = "";
    private String country = "CN";
    private String pushType = "";
    //    json形式的字符串
    private String content = "";
}
