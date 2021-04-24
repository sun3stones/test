package com.lei.rabbitmq.work;

import com.lei.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
 
public class Sender2 {
    private final  static String QUEUE_NAME = "queue_work";
 
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection connection = RabbitmqConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
 
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for(int i = 0; i < 100; i++){
            String message = "sender2:" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("[x] Sent '"+message + "'");
            Thread.sleep(i*10);
        }
        channel.close();
        connection.close();
    }
}