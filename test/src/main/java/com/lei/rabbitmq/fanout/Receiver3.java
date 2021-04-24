package com.lei.rabbitmq.fanout;

import com.lei.rabbitmq.utils.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Receiver3 {
 
    //交换机名称
    private final static String EXCHANGE_NAME = "test_exchange_fanout";
 
    //队列名称
    private static final String QUEUE_NAME    = "test_queue_phone";
 
    public static void main(String[] args)
    {
        try
        {
 
            //获取连接
            Connection connection = RabbitmqConnectionUtil.getConnection();
            //从连接中获取一个通道
            final Channel channel = connection.createChannel();
            //声明交换机（分发:发布/订阅模式）
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            //声明队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //将队列绑定到交换机
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
            //保证一次只分发一个
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);
            //定义消费者
            DefaultConsumer consumer = new DefaultConsumer(channel)
            {
                //当消息到达时执行回调方法
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException
                {
                    String message = new String(body, "utf-8");
                    System.out.println("[phone2] Receive message：" + message);
                    try
                    {
                        //消费者休息1s处理业务
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        System.out.println("[3] done");
                        //手动应答
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };
            //设置手动应答
            boolean autoAck = false;
            //监听队列
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}