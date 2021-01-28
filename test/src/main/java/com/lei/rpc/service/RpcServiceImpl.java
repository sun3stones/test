package com.lei.rpc.service;

public class RpcServiceImpl implements IRpcService {
    @Override
    public String echo(String msg) {
        return msg;
    }
}
