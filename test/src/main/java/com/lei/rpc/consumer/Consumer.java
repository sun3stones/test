package com.lei.rpc.consumer;

import com.lei.rpc.service.IRpcService;

public class Consumer {


    public static void main(String[] args) throws InterruptedException {
        CallProxy proxy = new CallProxy(IRpcService.class);
        IRpcService service = proxy.getProxy();
        service.echo("123456");
    }
}
