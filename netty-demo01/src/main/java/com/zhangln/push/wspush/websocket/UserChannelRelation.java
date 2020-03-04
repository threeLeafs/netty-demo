package com.zhangln.push.wspush.websocket;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * user和channel的关系类,用于存储对应的关联
 *
 * @author mac
 */
@Slf4j
public class UserChannelRelation {

    /**
     * 定义静态变量
     */
    private static final HashMap<String, Channel> manager = new HashMap<>();

    /**
     * 定义存储方法
     */
    public static void put(String sessionId, Channel channel) {
        manager.put(sessionId, channel);
    }

    /**
     * 定义获取方法
     */
    public static Optional<Channel> get(String sessionId) {
        //根据 sessionId 获取对应的channel
        return Optional.ofNullable(manager.get(sessionId));
    }

    /**
     * 输出所有的user和channel信息
     */
    public static void output() {
        for (Map.Entry<String, Channel> channelEntry : manager.entrySet()) {
            log.info("user: " + channelEntry.getKey() + ", channelId: " + channelEntry.getValue().id().asLongText());
        }
    }

}