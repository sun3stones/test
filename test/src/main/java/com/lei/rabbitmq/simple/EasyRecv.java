package com.lei.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class EasyRecv {
    //队列名称
    private final static String QUEUE_NAME = "hello world";
    public static void main(String[] argv) throws java.io.IOException,java.lang.InterruptedException {


        //打开连接和创建频道，与发送端一样
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        /**
         * 队列名
         * 是否持久化
         *  是否排外  即只允许该channel访问该队列   一般等于true的话用于一个队列只能有一个消费者来消费的场景
         *  是否自动删除  消费完删除
         *  其他属性
         *
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("Waiting for messages. To exit press CTRL+C");

        //创建队列消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //指定消费队列
        /**
         * 队列名
         * 其他属性  路由
         * 消息body
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
        while (true)
        {
            //nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println("Received '" + message + "'");
        }

    }
}