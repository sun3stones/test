package com.lei.rpc.register;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RegisterHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Set<String>> interfaceMap = new ConcurrentHashMap<>();

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

        } else {
            String providerAddress = address.getAddress().getHostAddress() + ":" + address.getPort();
            Set<String> providers = interfaceMap.get(protocol.getClassName());
            if (CollectionUtils.isEmpty(providers)){
                providers = Collections.singleton(providerAddress);
            } else {
                providers.add(providerAddress);
            }
        }
        System.out.println(msg);
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
