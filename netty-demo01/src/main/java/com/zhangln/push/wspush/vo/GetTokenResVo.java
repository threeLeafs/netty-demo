package com.zhangln.push.wspush.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */

@Data
@Builder
public class GetTokenResVo implements Serializable {
    private String token;
    //    过期时间
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime expTime;
}
