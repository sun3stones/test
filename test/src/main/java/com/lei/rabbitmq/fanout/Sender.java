package com.lei.rabbitmq.fanout;

import com.lei.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Sender {
    private final static String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) {
        try {
            //获取连接
            Connection connection = RabbitmqConnectionUtil.getConnection();
            //从连接中获取一个通道
            Channel channel = connection.createChannel();
            //声明交换机（分发:发布/订阅模式）
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            //发送消息
            for (int i = 0; i < 5; i++) {
                String message = "" + i;
                System.out.println("[send]：" + message);
                //发送消息
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("utf-8"));
                Thread.sleep(5 * i);
            }
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}