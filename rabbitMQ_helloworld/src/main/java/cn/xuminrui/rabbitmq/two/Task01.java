package cn.xuminrui.rabbitmq.two;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Task01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{
        final Channel channel = RabbitMqUtill.getChannel();

        final Scanner scanner = new Scanner(System.in);
        System.out.println("请输入");
        while (scanner.hasNext()) {
            System.out.println("请输入一条消息");
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null, message.getBytes());
            System.out.println("发送消息完成" + message);
        }
    }
}
