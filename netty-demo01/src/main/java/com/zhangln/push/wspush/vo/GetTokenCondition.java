package com.zhangln.push.wspush.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Data
@ToString
public class GetTokenCondition implements Serializable {

    @NotEmpty
    private String clientType;
    @NotEmpty
    private String accessKey;
    @NotEmpty
    private String accessSecret;

}
