package com.zhangln.push.wspush.controller.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangln.push.wspush.entity.AccessTokenEntity;
import com.zhangln.push.wspush.service.IAccessTokenService;
import com.zhangln.push.wspush.service.IRegUserService;
import com.zhangln.push.wspush.vo.GetTokenCondition;
import com.zhangln.push.wspush.config.prop.AppProp;
import com.zhangln.push.wspush.entity.RegUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Service
@Slf4j
public class AccessService {

    @Autowired
    private IAccessTokenService iAccessTokenService;

    @Autowired
    private IRegUserService iRegUserService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AppProp appProp;

    /**
     * 检查认证信息是否正确
     *
     * @param clientType
     * @param accessKey
     * @param accessSecret
     * @return
     */
    public boolean checkAuth(String clientType, String accessKey, String accessSecret) {
        RegUserEntity one = iRegUserService.getOne(new QueryWrapper<RegUserEntity>()
                .eq(RegUserEntity.CLIENT_TYPE, clientType)
                .eq(RegUserEntity.ACCESS_KEY, accessKey)
                .eq(RegUserEntity.ACCESS_SECRET, accessSecret)
                .eq(RegUserEntity.STATUS, 1)
        );
        return one != null;
    }

    /**
     * 获取token有效时间，分钟
     *
     * @return
     */
    public Integer getExpTimeConfig() {
        return 10;
    }

    /**
     * 生成token
     *
     * @return
     */
    public String createToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * 保存到数据库中
     *
     * @param token
     * @param condition
     * @param expTime
     * @return
     */
    public boolean saveToken2Db(String token, GetTokenCondition condition, LocalDateTime expTime) {
        AccessTokenEntity accessTokenEntity = AccessTokenEntity.builder()
                .clientType(condition.getClientType())
                .accessKey(condition.getAccessKey())
                .token(token)
                .createTime(LocalDateTime.now())
                .expTime(expTime)
                .build();
        boolean save = iAccessTokenService.save(accessTokenEntity);
        return save;
    }

    /**
     * 保存token到redis中
     *
     * @param token
     * @param condition
     * @param expTime
     * @return
     */
    public boolean saveToken2Cache(String token, GetTokenCondition condition, Integer expTime) {
        String key = "access:" + token;
        redisTemplate.opsForValue()
                .set(key, JSONObject.toJSONString(condition), expTime, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 检查token是否存在
     *
     * @param token
     * @return
     */
    public boolean exists(String token) {

//        测试环境下，使用唯一的一个token就行
        if ("DEV".equals(appProp.getActive())
                && "testToken".equals(token)) {
            return true;
        }

        String key = "access:" + token;
        Object o = redisTemplate.opsForValue().get(key);
        return o != null;
    }
}
