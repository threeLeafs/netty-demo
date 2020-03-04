package com.zhangln.push.wspush.controller.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhangln.push.wspush.service.ILogPushTaskService;
import com.zhangln.push.wspush.service.ILogWsConnectService;
import com.zhangln.push.wspush.vo.HttpResVo;
import com.zhangln.push.wspush.vo.HttpWsPushCondition;
import com.zhangln.push.wspush.vo.WsRespVo;
import com.zhangln.push.wspush.entity.LogPushTaskEntity;
import com.zhangln.push.wspush.entity.LogWsConnectEntity;
import com.zhangln.push.wspush.websocket.UserChannelRelation;
import com.zhangln.push.wspush.websocket.service.WsService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@Service
@Slf4j
public class WsPushService {

    @Autowired
    private ILogWsConnectService iLogWsConnectService;
    @Autowired
    private ILogPushTaskService iLogPushTaskService;

    @Autowired
    private WsService wsService;

    /**
     * 通用推送
     *
     * @param condition
     * @return
     * @throws UnknownHostException
     */
    public HttpResVo commonPushService(HttpWsPushCondition condition) throws UnknownHostException {
        //        查询channelId

        List<String> channelIds = this.getChannelIds(condition);

        if (!CollectionUtils.isEmpty(channelIds)) {

//          由推送方的服务器来设置本次推送的id
//          整个id对应的是一次调用，而不是一次推送
//          每次具体的推送本身肯定是端对端的
            condition.setPushId(UUID.randomUUID().toString());

            channelIds.stream()
                    .forEach(channelId -> {
                        try {
                            WsRespVo wsRespVo = WsRespVo.builder()
                                    .id(condition.getPushId())
                                    .date(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                                    .pushType(condition.getPushType())
                                    .code(200)
                                    .msg("正常")
                                    .data(condition.getContent())
                                    .build();

                            String pushStr = JSONObject.toJSONString(wsRespVo);
                            log.info("向{}推送{}", channelId, pushStr);

                            UserChannelRelation.get(channelId)
                                    .ifPresent(channel -> {
                                        channel.writeAndFlush(new TextWebSocketFrame(pushStr));
                                        this.savePushTask(condition, pushStr, channel);
                                    });
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }

                    });
        } else {
            return HttpResVo.buildError("无有效客户端连接，推送失败");
        }


        return HttpResVo.buildSuccess(condition.getPushId());
    }

    /**
     * 查询
     *
     * @param condition
     * @return
     */
    public List<String> getChannelIds(HttpWsPushCondition condition) throws UnknownHostException {

        List<LogWsConnectEntity> list = iLogWsConnectService.list(new QueryWrapper<LogWsConnectEntity>()
                .eq(LogWsConnectEntity.STATUS, 1)
                .eq(!StringUtils.isEmpty(condition.getClientType()), LogWsConnectEntity.CLIENT_TYPE, condition.getClientType())
                .eq(!StringUtils.isEmpty(condition.getApp()), LogWsConnectEntity.APP, condition.getApp())
                .eq(!StringUtils.isEmpty(condition.getUser()), LogWsConnectEntity.USER, condition.getUser())
                .eq(!StringUtils.isEmpty(condition.getGroup()), LogWsConnectEntity.GROUP, condition.getGroup())
                .eq(!StringUtils.isEmpty(condition.getAreaCode()), LogWsConnectEntity.AREA_CODE, condition.getAreaCode())
                .eq(!StringUtils.isEmpty(condition.getCountry()), LogWsConnectEntity.COUNTRY, condition.getCountry())
                .eq(LogWsConnectEntity.INSTANCE_FLAG, wsService.getInstanceFlag())
        );
        if (!CollectionUtils.isEmpty(list)) {
            List<String> ids = list.stream()
                    .map(LogWsConnectEntity::getChannelId)
                    .collect(Collectors.toList());
            return ids;
        }
        return null;
    }

    /**
     * 记录推送任务日志
     *
     * @param condition
     * @param pushStr
     * @param channel
     */
    public void savePushTask(HttpWsPushCondition condition, String pushStr, Channel channel) {
        LogPushTaskEntity logPushTaskEntity = LogPushTaskEntity.builder()
                .pushId(condition.getPushId())
                .pushType(condition.getPushType())
                .channelId(channel.id().asShortText())
                .clientType(condition.getClientType())
                .app(condition.getApp())
                .user(condition.getUser())
                .group(condition.getGroup())
                .areaCode(condition.getAreaCode())
                .country(condition.getCountry())
                .content(pushStr)
                .status(1)
                .build();
        iLogPushTaskService.save(logPushTaskEntity);
    }

    /**
     * 根据push_id查询推送详情
     *
     * @param pushId
     * @return
     */
    public List<LogPushTaskEntity> getPushMsgs(String pushId) {
        return iLogPushTaskService.list(new QueryWrapper<LogPushTaskEntity>()
                .eq(LogPushTaskEntity.PUSH_ID, pushId));
    }
}
