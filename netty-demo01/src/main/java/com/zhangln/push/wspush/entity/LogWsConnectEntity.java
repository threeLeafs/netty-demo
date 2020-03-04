package com.zhangln.push.wspush.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户端ws连接日志
 * </p>
 *
 * @author sherry
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("log_ws_connect")
public class LogWsConnectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.ID_WORKER_STR)
    private String pkId;

    private String channelId;

    /**
     * 0-已连接
1-已注册，只有此状态的连接才是有效的
2-注册失败
3-已下线
     */
    private Integer status;

    private String token;

    /**
     * 客户端类型，描述清楚自己是哪个平台的，Web、APP、PAD
     */
    private String clientType;

    /**
     * 哪个应用，要么是我们系统的应用，要么是others第三方的
     */
    private String app;

    /**
     * 用户名，app+user，唯一标记到一个WebSocket客户端
     */
    private String user;

    /**
     * user所属群组
     */
    @TableField("`group`")
    private String group;

    /**
     * 区域编码，取国标，默认为空字符串
     */
    private String areaCode;

    private String country;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String serverHost;
    /**
     * 标记当前连接注册到了哪个推送实例上
     */
    private String instanceFlag;
    private Integer serverPort;


    public static final String PK_ID = "pk_id";

    public static final String CHANNEL_ID = "channel_id";

    public static final String STATUS = "status";

    public static final String TOKEN = "token";

    public static final String CLIENT_TYPE = "client_type";

    public static final String APP = "app";

    public static final String USER = "user";

    public static final String GROUP = "group";

    public static final String AREA_CODE = "area_code";

    public static final String COUNTRY = "country";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";
    public static final String SERVER_HOST = "server_host";
    public static final String SERVER_PORT = "server_port";
    public static final String INSTANCE_FLAG = "instance_flag";

}
