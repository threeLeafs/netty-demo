package com.zhangln.push.wspush.entity;

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
 * token验证表
 * </p>
 *
 * @author sherry
 * @since 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@TableName("access_token")
public class AccessTokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pk_id", type = IdType.ID_WORKER_STR)
    private String pkId;

    private String token;

    private String clientType;

    private String accessKey;

    private LocalDateTime createTime;

    private LocalDateTime expTime;


    public static final String PK_ID = "pk_id";

    public static final String TOKEN = "token";

    public static final String CLIENT_TYPE = "client_type";

    public static final String ACCESS_KEY = "access_key";

    public static final String CREATE_TIME = "create_time";

    public static final String EXP_TIME = "exp_time";

}
