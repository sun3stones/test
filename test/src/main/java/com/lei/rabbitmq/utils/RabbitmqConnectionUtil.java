package com.lei.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
public  class RabbitmqConnectionUtil {
    public static Connection getConnection() throws IOException {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //连接5672端口  注意15672为工具界面端口  25672为集群端口
        factory.setPort(5672);
        //factory.setVirtualHost("/xxxxx");
       // factory.setUsername("微信号：codedq");
       // factory.setPassword("123456");
        //获取连接
        Connection connection = factory.newConnection();
        return connection;
    }
}