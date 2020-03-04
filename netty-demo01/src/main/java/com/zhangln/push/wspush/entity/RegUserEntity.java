package com.zhangln.push.wspush.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注册用户，即允许使用推送服务的账号
 * </p>
 *
 * @author sherry
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("reg_user")
public class RegUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，使用雪花算法生成
     */
    @TableId(value = "pk_id", type = IdType.ID_WORKER_STR)
    private String pkId;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 认证key
     */
    private String accessKey;

    /**
     * 认证密码，前期为了方便可以使用明文
     */
    private String accessSecret;

    /**
     * 账号状态

0-未启用
1-启用
2-已禁用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


    public static final String PK_ID = "pk_id";

    public static final String CLIENT_TYPE = "client_type";

    public static final String ACCESS_KEY = "access_key";

    public static final String ACCESS_SECRET = "access_secret";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

}
