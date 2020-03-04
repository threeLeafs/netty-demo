package com.zhangln.push.wspush.websocket.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangln.push.wspush.config.prop.AppProp;
import com.zhangln.push.wspush.entity.LogWsConnectEntity;
import com.zhangln.push.wspush.service.ILogWsConnectService;
import com.zhangln.push.wspush.vo.WsRegVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Service
@Slf4j
public class WsService {

    @Autowired
    private ILogWsConnectService iLogWsConnectService;

    @Autowired
    private AppProp appProp;

    /**
     * 设备连接，未注册
     *
     * @param id
     */
    public void connected(String id) {
        LogWsConnectEntity logWsConnectEntity = LogWsConnectEntity.builder()
                .channelId(id)
                .status(0)
                .token("")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .instanceFlag("")
                .build();
        iLogWsConnectService.save(logWsConnectEntity);
    }

    /**
     * 设备注册成功
     *
     * @param id
     * @param tokenId
     * @param wsRegVo
     */
    public void regSuccess(String id, String tokenId, WsRegVo wsRegVo) throws UnknownHostException {
//        不去更新connected时insert的数据，这里也是直接insert
        LogWsConnectEntity logWsConnectEntity = LogWsConnectEntity.builder()
                .channelId(id)
                .status(1)
                .token(tokenId)
                .clientType(wsRegVo.getClientType())
                .app(wsRegVo.getApp())
                .user(wsRegVo.getUser())
                .group(wsRegVo.getGroup())
                .areaCode(wsRegVo.getAreaCode())
                .country(StringUtils.isEmpty(wsRegVo.getCountry()) ? "CN" : wsRegVo.getCountry())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .serverHost(InetAddress.getLocalHost().getHostAddress())
                .serverPort(appProp.getWsPort())
                .instanceFlag(this.getInstanceFlag())
                .build();
        iLogWsConnectService.save(logWsConnectEntity);

//            注册成功后，可以把连接上的那条记录删了
        iLogWsConnectService.remove(new QueryWrapper<LogWsConnectEntity>()
                .eq(LogWsConnectEntity.CHANNEL_ID, id)
                .eq(LogWsConnectEntity.STATUS, 0));

    }

    /**
     * 计算实例标识
     * 这个规则可以变化
     * 这个计算规则的变化，往往和部署方式有关
     *
     * 第一版计算规则：运行环境的IP:PORT
     *
     * @return
     */
    public String getInstanceFlag() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress()+":"+appProp.getWsPort();
    }

    /**
     * 设备离线
     *
     * @param channelId
     */
    public void offLine(String channelId) {
        List<LogWsConnectEntity> list = iLogWsConnectService.list(new QueryWrapper<LogWsConnectEntity>()
                .eq(LogWsConnectEntity.CHANNEL_ID, channelId));
        List<LogWsConnectEntity> logWsConnectEntities = list.stream()
                .map(logWsConnectEntity -> logWsConnectEntity.setStatus(3))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(logWsConnectEntities)){
            iLogWsConnectService.updateBatchById(logWsConnectEntities);
        }
    }
}
