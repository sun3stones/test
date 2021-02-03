package com.lei.rpc.provider;

import com.lei.rpc.protocol.InvokerProtocol;
import com.lei.rpc.protocol.Transfer;
import com.lei.rpc.service.IRpcService;
import com.lei.rpc.service.RpcServiceImpl;
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

    private static HashMap interfaceMap = new HashMap();

    static {
        interfaceMap.put(IRpcService.class.getName(), RpcServiceImpl.class.getName());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        Transfer<InvokerProtocol> transfer = new Transfer<>();
        InvokerProtocol protocol = transfer.deserialize(msg.toString(),InvokerProtocol.class);
        Object result = "result";
        try {
            Object o = interfaceMap.get(protocol.getClassName());
            Object o1 = Class.forName((String) o).newInstance();
            Method method = o1.getClass().getMethod(protocol.getMethodName(),protocol.getParams()[0].getClass());
            result = method.invoke(o1, protocol.getParams());
        } catch (Exception e){
            e.printStackTrace();
            return;
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
