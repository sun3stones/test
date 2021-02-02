package com.lei.rpc.provider;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ProviderHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(123);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        Transfer<InvokerProtocol> transfer = new Transfer<>();
        InvokerProtocol protocol = transfer.deserialize(msg.toString(),InvokerProtocol.class);
        Object result = "result";
        try {
            ServiceLoader loader = ServiceLoader.load(Class.forName(protocol.getClassName()));
            Iterator iterator = loader.iterator();
            if (iterator.hasNext()){
                Object o = iterator.next();
                Method method = o.getClass().getMethod(protocol.getMethodName());
                result = method.invoke(o, protocol.getParams());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(result);
        ctx.writeAndFlush(result).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
