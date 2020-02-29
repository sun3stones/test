package com.lei.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 闪电侠
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                });

        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
        ExecutorService es = Executors.newFixedThreadPool(10);
        while (true) {
            for (int i = 0; i < 10; i++) {
                es.execute(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "线程名：" + Thread.currentThread().getName()
                                + "时间：" + LocalDateTime.now() ;
                        channel.writeAndFlush(msg);
                    }
                });
            }
            Thread.sleep(10000);
        }

    }
}