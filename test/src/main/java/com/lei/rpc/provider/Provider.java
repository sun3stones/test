package com.lei.rpc.provider;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import com.lei.rpc.service.IRpcService;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Provider {

    public static void register(Integer providerPort) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
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
            ChannelFuture f = b.connect("127.0.0.1", 2181).sync();
            Transfer<InvokerProtocol> transfer = new Transfer<>();
            f.channel().writeAndFlush(transfer.serialize(new InvokerProtocol(IRpcService.class.getName(),providerPort)));
            provider(providerPort);
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
            System.out.println("找不到注册中心【2181】");
            workerGroup.shutdownGracefully();
        }

    }

    public static void provider(Integer providerPort){
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ProviderHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture future = bootstrap.bind(providerPort).sync();
            System.out.println("提供者启动，端口：" + providerPort);
            future.channel().closeFuture().sync();
        } catch (Exception e){
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        register(8002);

    }
}
