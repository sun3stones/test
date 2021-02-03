package com.lei.rpc.consumer;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CallProxy implements InvocationHandler {

    private Class clazz;

    public CallProxy(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * 获取被代理接口实例对象
     * @param <T>
     * @return
     */
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return registerAndCall(method,args);
    }

    private Object registerAndCall(Method method, Object[] args){
        Object result = null;
        String name = clazz.getName();
        RegisterHandler handler = new RegisterHandler();
        InvokerProtocol protocol = new InvokerProtocol(name,method.getName(),args);
        handler.setProtocol(protocol);
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
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(handler);
                }
            });
            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 2181).sync();
            Transfer<InvokerProtocol> transfer = new Transfer<>();
            f.channel().writeAndFlush(transfer.serialize(new InvokerProtocol(name,method.getName(),args)));
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
            System.out.println("找不到注册中心【2181】");
            workerGroup.shutdownGracefully();
        } finally {
          return result;
        }
    }

    private class RegisterHandler extends ChannelInboundHandlerAdapter{

        private InvokerProtocol protocol;

        public void setProtocol(InvokerProtocol protocol) {
            this.protocol = protocol;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            String[] split = msg.toString().split(":");
            call(split[0], Integer.valueOf(split[1]),protocol);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }
    }

    private Object call(String address, Integer port, InvokerProtocol protocol) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ConsumerHandler handler = new ConsumerHandler();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new StringEncoder());
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(handler);
                }
            });
            // Start the client.
            ChannelFuture f = b.connect(address, port).sync();
            Transfer<InvokerProtocol> transfer = new Transfer<>();
            f.channel().writeAndFlush(transfer.serialize(protocol));
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            return handler.getResult();
        }
    }
}
