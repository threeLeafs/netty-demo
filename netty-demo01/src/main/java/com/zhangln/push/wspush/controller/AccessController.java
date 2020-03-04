package com.zhangln.push.wspush.controller;

import com.zhangln.push.wspush.vo.GetTokenCondition;
import com.zhangln.push.wspush.vo.GetTokenResVo;
import com.zhangln.push.wspush.vo.HttpResVo;
import com.zhangln.push.wspush.config.prop.AppProp;
import com.zhangln.push.wspush.controller.service.AccessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 认证授权
 *
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@RestController
@RequestMapping("/access")
@Slf4j
public class AccessController {

    @Autowired
    private AccessService accessService;

    @Autowired
    private AppProp appProp;

    /**
     * 获取token
     *
     * @return
     */
    @PostMapping(value = "/token")
    public ResponseEntity getToken(@Validated GetTokenCondition condition) {

        log.info("获取token：{}", condition);

//        检查认证信息是否正确
        boolean isAuthMsgRight = accessService.checkAuth(condition.getClientType(), condition.getAccessKey(), condition.getAccessSecret());
        if (!isAuthMsgRight) {
            return ResponseEntity.ok(HttpResVo.buildError("认证信息有误"));
        }

//        获取配置的过期时间，分钟
        Integer expTimeMinute = accessService.getExpTimeConfig();
//        生成token
        String token = accessService.createToken();
//        计算出过期时间
        LocalDateTime expTime = LocalDateTime.now().plusMinutes(expTimeMinute);

//        保存到数据库中
        boolean isSaveDbOk = accessService.saveToken2Db(token, condition, expTime);

        if (!isSaveDbOk) {
            return ResponseEntity.ok(HttpResVo.buildError("token入库失败"));
        }

//        保存到redis中
        boolean isSaveCacheOk = accessService.saveToken2Cache(token, condition, expTimeMinute);
        if (isSaveCacheOk) {
//            构建返回对象
            GetTokenResVo getTokenResVo = GetTokenResVo.builder()
                    .token(token)
                    .expTime(expTime)
                    .build();
            return ResponseEntity.ok(HttpResVo.buildSuccess(getTokenResVo));
        }

        return ResponseEntity.ok(HttpResVo.error());
    }


    /**
     * 检查token是否有效
     *
     * @param access
     * @return
     */
    @PostMapping(value = "/auth/token", headers = {"access"})
    public ResponseEntity checkToken(@RequestHeader String access) {

//        检查这个access在redis中还是否存在
        boolean isExistsInCache = accessService.exists(access);
        if (isExistsInCache) {
            return ResponseEntity.ok(HttpResVo.success());
        }

        return ResponseEntity.ok(HttpResVo.error());
    }

}
