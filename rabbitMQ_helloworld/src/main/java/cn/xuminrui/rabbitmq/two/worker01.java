package cn.xuminrui.rabbitmq.two;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 一个工作线程，相当于之前的消费者
 */
public class worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        final Channel channel = RabbitMqUtill.getChannel();

        DeliverCallback deliverCallback = (s, deliver) -> {
            System.out.println("接收到的消息：" + new String(deliver.getBody()));
        };

        CancelCallback cancelCallback = s -> {
            System.out.println(s + "消费者取消消费逻辑");
        };
        System.out.println("消费者2开始接收消息");

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
