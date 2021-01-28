package com.lei.rpc.service;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class Provider {

    private static int registerPort = 2181;

    public static void register() throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        // Start the client.
        ChannelFuture f = b.connect("127.0.0.1", registerPort).sync();
        f.channel().writeAndFlush(IRpcService.class.getName());
        // Wait until the connection is closed.
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        register();
    }
}
