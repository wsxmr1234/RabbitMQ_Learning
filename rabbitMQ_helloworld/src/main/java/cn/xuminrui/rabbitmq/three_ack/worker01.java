package cn.xuminrui.rabbitmq.three_ack;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class worker01 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtill.getChannel();



        try (AutoCloseable proxyInstance = (AutoCloseable)Proxy.newProxyInstance(worker01.class.getClassLoader(), new Class[]{AutoCloseable.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + "运行了");
                return method.invoke(channel);
            }
        });) {
//            DeliverCallback deliverCallback = new DeliverCallback() {
//                @Override
//                public void handle(String s, Delivery delivery) throws IOException {
//                    final String message = new String(delivery.getBody());
//                    TimeUnit.SECONDS.sleep(1);
//                    System.out.println("接收到消息：" + message);
//
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//                }
//            };
//            Channel channel = RabbitMqUtill.getChannel();



            DeliverCallback deliverCallback = (s, delivery) -> {
                System.out.println(channel);
                final String message = new String(delivery.getBody());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println("接收到消息：" + message + "手动确认");

                System.out.println(channel.isOpen());
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                System.out.println("手动确认结束");
            };

            CancelCallback cancelCallback = new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println(s + "消费者取消");
                }
            };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);

        }
    }
}
