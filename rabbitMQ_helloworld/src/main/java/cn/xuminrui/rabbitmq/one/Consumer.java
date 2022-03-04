package cn.xuminrui.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.110.134");
        factory.setUsername("root");
        factory.setPassword("admin");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                System.out.println(new String(delivery.getBody()));
            }
        };

        CancelCallback cancelCallback = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {
                System.out.println("消息被中断");
            }
        };



        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
             * 3.消费者成功消费的回调
             * 4.消费者取消消费的回调
             */
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

        }


    }


}
