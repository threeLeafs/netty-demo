package com.zhangln.push.wspush.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sherry
 * @description
 * @date Create in 2019-08-15
 * @modified By:
 */
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 静态内部类声明
     */
    private static class SingletonWSSever {
        static final WebSocketServer instance = new WebSocketServer();
    }

    /**
     * 公开获取的静态方法
     *
     * @return
     */
    public static WebSocketServer getInstance() {
        return SingletonWSSever.instance;
    }

    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;

    /**
     * 构造器私有
     */
    private WebSocketServer() {
        //1. 两个线程组
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        //2. 启动类
        bootstrap = new ServerBootstrap();
        //3. 定义启动的线程组,channel和初始化器
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketSeverInitializer());
    }

    /**
     * 服务器开启的方法
     */
    public void start(int port) {
        this.future = bootstrap.bind(port);
        log.info("Netty Websocket Server Starting ....  on port:{}", port);
    }
}
