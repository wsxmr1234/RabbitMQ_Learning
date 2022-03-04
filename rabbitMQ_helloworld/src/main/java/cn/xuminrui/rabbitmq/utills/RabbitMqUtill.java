package cn.xuminrui.rabbitmq.utills;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtill {
    //获取一个连接的channel
    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.110.134");
        factory.setUsername("root");
        factory.setPassword("admin");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        return channel;
    }


}
