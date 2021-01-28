package com.lei.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServerDemo {

    public static void main(String[] args) throws InterruptedException {
        // 创建
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
            // 组装NioEventLoopGroup
            .group(bossGroup,workerGroup)
            // 设置channel类型为NIO类型
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)//TCP的缓冲区设置
            .option(ChannelOption.SO_SNDBUF, 32*1024)//设置发送缓冲的大小
            .option(ChannelOption.SO_RCVBUF, 32*1024)//设置接收缓冲区大小
            .option(ChannelOption.SO_KEEPALIVE, true)//保持连续
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    sc.pipeline().addLast(new StringDecoder());//定义接收类型为字符串把ByteBuf转成String
                    sc.pipeline().addLast(new ServerHandler());//在这里配置具体数据接收方法的处理
                }
            });
        ChannelFuture future = serverBootstrap.bind(8002).sync();//绑定端口
        System.out.println("netty服务端已启动,等待连接...");
        future.channel().closeFuture().sync();//等待关闭(程序阻塞在这里等待客户端请求)
        bossGroup.shutdownGracefully();//关闭线程
        workerGroup.shutdownGracefully();//关闭线程
    }
}
