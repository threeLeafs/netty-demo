package com.zhangln.push.wspush.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * http请求通用返回包装类
 *
 * @Author sherry
 * @Description
 * @Date Create in 2019/12/7
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResVo implements Serializable {

    private Integer code;
    private String msg;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private LocalDateTime resTime;

    //    通用成功
    public static HttpResVo success(){
        return HttpResVo.builder()
                .code(200)
                .msg("成功")
                .resTime(LocalDateTime.now())
                .build();
    }

    //    通用失败
    public static HttpResVo error(){
        return HttpResVo.builder()
                .code(500)
                .msg("失败")
                .resTime(LocalDateTime.now())
                .build();
    }

    /**
     * 成功body
     * @param o
     * @return
     */
    public static HttpResVo buildSuccess(Object o){
        return HttpResVo.builder()
                .code(200)
                .msg("成功")
                .resTime(LocalDateTime.now())
                .body(o)
                .build();
    }

    /**
     * 失败body
     * @param o
     * @return
     */
    public static HttpResVo buildError(Object o){
        return HttpResVo.builder()
                .code(500)
                .msg("失败")
                .resTime(LocalDateTime.now())
                .body(o)
                .build();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object body;

}
