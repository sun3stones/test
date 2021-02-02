package com.lei.rpc.consumer;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import com.lei.rpc.service.IRpcService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Consumer {


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new ConsumerHandler());
            }
        });
        // Start the client.
        ChannelFuture f = b.connect("127.0.0.1", 8000).sync();
        Transfer<InvokerProtocol> transfer = new Transfer<>();
        args = new String[]{"hello"};
        f.channel().writeAndFlush(transfer.serialize(new InvokerProtocol(IRpcService.class.getName(),"echo",args)));
        // Wait until the connection is closed.
        f.channel().closeFuture().sync();
    }
}
