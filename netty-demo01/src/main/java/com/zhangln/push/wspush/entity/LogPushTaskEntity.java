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
 * 推送日志
 * </p>
 *
 * @author sherry
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("log_push_task")
public class LogPushTaskEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.ID_WORKER_STR)
    private String pkId;

    /**
     * 一次推送请求，一条记录
     */
    private String pushId;

    private String channelId;

    private String clientType;

    private String app;

    private String user;

    /**
     * 群组
     */
    @TableField(value = "`group`")
    private String group;

    private String areaCode;

    private String country;

    private LocalDateTime createTime;

    /**
     * 0-待推送
1-推送成功
2-推送失败
     */
    private Integer status;

    private String content;

    /**
     * 推送类型
     */
    private String pushType;


    public static final String PK_ID = "pk_id";

    public static final String PUSH_ID = "push_id";

    public static final String CHANNEL_ID = "channel_id";

    public static final String CLIENT_TYPE = "client_type";

    public static final String APP = "app";

    public static final String USER = "user";

    public static final String GROUP = "group";

    public static final String AREA_CODE = "area_code";

    public static final String COUNTRY = "country";

    public static final String CREATE_TIME = "create_time";

    public static final String STATUS = "status";

    public static final String CONTENT = "content";

    public static final String PUSH_TYPE = "push_type";

}
