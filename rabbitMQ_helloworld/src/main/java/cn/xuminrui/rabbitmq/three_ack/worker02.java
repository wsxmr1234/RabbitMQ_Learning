package cn.xuminrui.rabbitmq.three_ack;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class worker02 {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        try (Channel channel = RabbitMqUtill.getChannel()) {
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
            DeliverCallback deliverCallback = (s, delivery) -> {
                final String message = new String(delivery.getBody());
                try {
                    TimeUnit.SECONDS.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                System.out.println("接收到消息：" + message);

                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };

            CancelCallback cancelCallback = new CancelCallback() {
                @Override
                public void handle(String s) throws IOException {
                    System.out.println(s + "消费者取消");
                }
            };

            channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);

            System.out.println(channel);
        }
    }
}
