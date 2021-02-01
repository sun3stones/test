package com.lei.rpc.register;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class RegisterHandler extends ChannelInboundHandlerAdapter {

    private static Map<String, LinkedHashSet<String>> interfaceMap = new ConcurrentHashMap<>();
    private static Map<String, Iterator<String>> roundBobinMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        InetSocketAddress localAddress = (InetSocketAddress) ctx.channel().localAddress();
        String msg = "服务注册成功" + address.getAddress().getHostAddress() + ":" + address.getPort();
        System.out.println(msg);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        Transfer<InvokerProtocol> transfer = new Transfer<>();
        InvokerProtocol protocol = transfer.deserialize(msg.toString(),InvokerProtocol.class);
        if (protocol.isConsumer()){
            String consumerAddress = address.getAddress().getHostAddress() + ":" + address.getPort();
            LinkedHashSet<String> providers = interfaceMap.get(protocol.getClassName());
            if (CollectionUtils.isEmpty(providers)){
                System.out.println("provider [" + protocol.getClassName() + "] is not found");
                return;
            }
            Iterator<String> iterator = roundBobinMap.get(protocol.getClassName());
            if (iterator == null){
                iterator = providers.iterator();
                roundBobinMap.put(protocol.getClassName(),iterator);
            }
            while (iterator.hasNext()){
                String provider = iterator.next();
                ctx.writeAndFlush(provider);
                System.out.println("消费者【" + consumerAddress + "】获取服务【" + protocol.getClassName() + "】服务地址【" + provider +"】");
                return;
            }
            iterator = providers.iterator();
            String provider = iterator.next();
            ctx.writeAndFlush(provider);
            System.out.println("消费者【" + consumerAddress + "】获取服务【" + protocol.getClassName() + "】服务地址【" + provider +"】");
        } else {
            String providerAddress = address.getAddress().getHostAddress() + ":" + address.getPort();
            LinkedHashSet<String> providers = interfaceMap.get(protocol.getClassName());
            if (CollectionUtils.isEmpty(providers)){
                providers = new LinkedHashSet<>(Collections.singleton(providerAddress));
                interfaceMap.put(protocol.getClassName(),providers);
            } else {
                providers.add(providerAddress);
            }
            System.out.println("生产者【" + providerAddress + "】提供服务【" + protocol.getClassName() + "】");
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String msg = "服务断开连接" + address.getAddress().getHostAddress() + ":" + address.getPort();
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
