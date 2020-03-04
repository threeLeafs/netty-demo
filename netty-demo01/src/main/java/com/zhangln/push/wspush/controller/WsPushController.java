package com.zhangln.push.wspush.controller;

import com.zhangln.push.wspush.controller.service.WsPushService;
import com.zhangln.push.wspush.entity.LogPushTaskEntity;
import com.zhangln.push.wspush.vo.HttpResVo;
import com.zhangln.push.wspush.vo.HttpWsPushCondition;
import com.zhangln.push.wspush.websocket.UserChannelRelation;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.List;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/7
 * @modified By:
 */
@RestController
@RequestMapping("/push")
@Slf4j
public class WsPushController {

    @Autowired
    private WsPushService wsPushService;

    /**
     * 测试推送
     *
     * @param channelId
     * @param msg
     * @return
     */
    @GetMapping(value = "/test1", params = {"channelId", "msg"})
    public ResponseEntity pushTest(String channelId, String msg) {

        UserChannelRelation.get(channelId)
                .ifPresent(channel -> {
                    channel.writeAndFlush(new TextWebSocketFrame(msg));
                });

        return ResponseEntity.ok("推送成功");
    }

    /**
     * 测试http
     *
     * @return
     */
    @GetMapping("/test2")
    public ResponseEntity httpTest() {
        return ResponseEntity.ok(HttpResVo.success());
    }

    /**
     * 通用推送接口
     *
     * @param condition
     * @return
     */
    @PostMapping("/common")
    public ResponseEntity commonPush(@Validated HttpWsPushCondition condition) throws UnknownHostException {
        HttpResVo httpResVo = wsPushService.commonPushService(condition);
        return ResponseEntity.ok(httpResVo);

    }



    /**
     * 查询推送情况
     * @return
     */
    @GetMapping("/msg")
    public ResponseEntity getPushMsg(String pushId){
        List<LogPushTaskEntity> logPushTaskEntities = wsPushService.getPushMsgs(pushId);

        return ResponseEntity.ok(HttpResVo.buildSuccess(logPushTaskEntities));
    }
}
