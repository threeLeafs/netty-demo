package com.zhangln.push.wspush.websocket;

import com.alibaba.fastjson.JSONObject;
import com.zhangln.push.wspush.controller.service.AccessService;
import com.zhangln.push.wspush.vo.WsRegVo;
import com.zhangln.push.wspush.vo.WsRespVo;
import com.zhangln.push.wspush.util.SpringUtils;
import com.zhangln.push.wspush.websocket.service.WsService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 自定义handler,继承简单频道入站处理程序,范围为wen文本套接字Frame
 * websocket间通过frame进行数据的传递和发送
 * 此版本为user与channel绑定的版本，消息会定向发送和接收到指定的user的channel中。
 *
 * @author mac
 */
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /**
     * 定义channel集合,管理channel,传入全局事件执行器
     */
    public static final ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 当客户端连接服务端之后(打开连接)----->handlerAdded
     * 获取客户端的channel,并且放到ChannelGroup中去管理
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("新接入连接id:{}", ctx.channel().id());
        users.add(ctx.channel());

//        设备连接日志
        WsService wsService = SpringUtils.getBean(WsService.class);
        wsService.connected(ctx.channel().id().asShortText());

        log.info("当前客户端连接数：{}", users.size());
    }

    /**
     * 定义信道的消息处理机制,该方法处理一次,故需要同时对所有客户端进行操作(channelGroup)
     *
     * @param ctx 上下文
     * @param msg 文本消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //1. 获取客户端传递过来的消息,其对象为TextWebSocketFrame
        String text = msg.text();
        log.info("接收到数据为:{}", text);
        //2. 对传递过来的消息类型进行判断，不同类型进行不同的逻辑处理，使用枚举类
        DataContent content = JSONObject.parseObject(text, DataContent.class);
        //获取动作类型
        Integer action = content.getAction();
        log.info("长连接请求类型：{}", action);
        //获取channel
        Channel currentChannel = ctx.channel();

        //2.1 websocket第一次open时，此时初始化channel
        if (Objects.equals(action, MsgActionEnum.CONNECT.type)) {
            //连接注册
            regConnection(content, currentChannel);
        } else if (Objects.equals(action, MsgActionEnum.KEEPALIVE.type)) {
            //2.2 心跳类型消息
            keepAlive(currentChannel);
        } else if (Objects.equals(action, MsgActionEnum.CLIENT_ERROR.type)) {
            //客户端异常
            execClientError(content.getJsonObjStr());
        } else if (Objects.equals(action, MsgActionEnum.PAGE_CHANGE.type)) {
            pageChange(content.getJsonObjStr());
        }

    }

    /**
     * 页面切换
     *
     * @param jsonObjStr
     */
    private void pageChange(String jsonObjStr) {
//        后续再完善
        log.info("现在什么都不需要做");
        log.info(jsonObjStr);
    }

    /**
     * 客户端产生异常
     *
     * @param jsonObjStr
     */
    private void execClientError(String jsonObjStr) {
//        处理客户端异常
        log.info("现在什么都不需要做");
        log.error(jsonObjStr);
    }

    /**
     * 心跳保持
     *
     * @param currentChannel
     */
    private void keepAlive(Channel currentChannel) {
        log.debug("收到来自channel为" + currentChannel.id().asShortText() + "的心跳包");
    }

    /**
     * 连接注册
     *
     * @param content
     * @param currentChannel
     */
    private void regConnection(DataContent content, Channel currentChannel) {

        String tokenId = content.getTokenId();
//        对tokenId进行合法性检查
        AccessService accessService = SpringUtils.getBean(AccessService.class);
        boolean checkResult = accessService.exists(tokenId);

        if (!checkResult) {
            log.info("{} ws连接校验不通过", content.getTokenId());
            users.remove(currentChannel);
            currentChannel.writeAndFlush(new TextWebSocketFrame(
                    JSONObject.toJSONString(WsRespVo.builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                            .msg("tokenId无效，注册失败").build())
            ));
        } else {

            try {
                log.info("注册成功了，id：{}", currentChannel.id().asShortText());

                //将channel与userId放入对应的关系类中
                UserChannelRelation.put(currentChannel.id().asShortText(), currentChannel);

//              注册信息记录到客户端在线表
                WsService wsService = SpringUtils.getBean(WsService.class);
                String jsonObjStr = content.getJsonObjStr();
                WsRegVo wsRegVo = JSONObject.parseObject(jsonObjStr, WsRegVo.class);

//                完善注册信息
                wsService.regSuccess(currentChannel.id().asShortText(), tokenId, wsRegVo);

                currentChannel.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(WsRespVo.builder()
                                .code(HttpStatus.OK.value())
                                .date(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                                .msg("连接成功").build())
                ));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("{} ws连接校验不通过", content.getTokenId());
                users.remove(currentChannel);
                currentChannel.writeAndFlush(new TextWebSocketFrame(
                        JSONObject.toJSONString(WsRespVo.builder()
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .date(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()))
                                .msg("tokenId无效，注册失败").build())
                ));
            }


        }
    }

    /**
     * 处理器移除时,移除channelGroup中的channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //打印移除的channel
        String channelId = ctx.channel().id().asShortText();
        log.info("客户端被移除，channelId为：" + channelId);

//        移除
        WsService wsService = SpringUtils.getBean(WsService.class);
        wsService.offLine(channelId);

        users.remove(ctx.channel());
    }

    /**
     * 发生异常时，关闭连接（channel），随后将channel从ChannelGroup中移除
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出错啦, 原因是:" + cause.getMessage());
        String channelId = ctx.channel().id().asShortText();
        ctx.channel().close();
//        移除
        WsService wsService = SpringUtils.getBean(WsService.class);
        wsService.offLine(channelId);

        users.remove(ctx.channel());
    }
}