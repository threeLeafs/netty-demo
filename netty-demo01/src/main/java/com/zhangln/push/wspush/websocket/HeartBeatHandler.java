package com.zhangln.push.wspush.websocket;

import com.zhangln.push.wspush.util.SpringUtils;
import com.zhangln.push.wspush.websocket.service.WsService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于处理客户端与服务端的心跳，在客户端空闲（如飞行模式)时关闭channel，节省服务器资源
 *
 * @author mac
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用户事件触发的处理器
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //判断evt是否属于IdleStateEvent，用于触发用户事件，包含读空闲，写空闲，读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //读空闲，不做处理
                log.info("进入读空闲，本实例当前连接数：{}", ChatHandler.users.size());
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //写空闲，不做处理
                log.info("进入写空闲，本实例当前连接数：{}", ChatHandler.users.size());
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("channel关闭前，本实例users的数量为：" + ChatHandler.users.size());
                //关闭channel
                Channel channel = ctx.channel();
                String channelId = channel.id().asShortText();
                channel.close();


//                更新数据库中的客户端连接状态
                WsService wsService = SpringUtils.getBean(WsService.class);
                wsService.offLine(channelId);

                log.info("channel关闭后，本实例users的数量为：" + ChatHandler.users.size());
            }

        }
    }
}
