package com.lei.rpc.service;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class Provider {

    public static void register(Integer registerPort) throws InterruptedException {
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
        Transfer<InvokerProtocol> transfer = new Transfer<>();
        f.channel().writeAndFlush(transfer.serialize(new InvokerProtocol(IRpcService.class.getName())));
        // Wait until the connection is closed.
        f.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        register(2181);
    }
}
