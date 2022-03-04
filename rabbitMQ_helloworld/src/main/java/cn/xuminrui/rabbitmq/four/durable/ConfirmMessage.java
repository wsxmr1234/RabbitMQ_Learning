package cn.xuminrui.rabbitmq.four.durable;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;
import java.sql.Time;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtill.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.confirmSelect();

        ConcurrentSkipListMap<Long, String> unconfirmed = new ConcurrentSkipListMap<>();
        ConfirmCallback handleAck = new ConfirmCallback() {
            @Override
            public void handle(long l, boolean b) throws IOException {
                if (b) {
                    unconfirmed.headMap(l, true).clear();
                } else {
                    unconfirmed.remove(l);
                }
            }
        };

        ConfirmCallback handleNack = new ConfirmCallback() {
            @Override
            public void handle(long l, boolean b) throws IOException {
                System.out.println(l + "消息添加至queue失败");
            }
        };

//        也可以用注掉的这种方法
//        channel.addConfirmListener(new ConfirmListener(){
//
//            @Override
//            public void handleAck(long l, boolean b) throws IOException {
//
//            }
//
//            @Override
//            public void handleNack(long l, boolean b) throws IOException {
//
//            }
//        });

        channel.addConfirmListener(handleAck,handleNack);

        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message =  i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
        }

    }
}
