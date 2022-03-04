package cn.xuminrui.rabbitmq.three_ack;

import cn.xuminrui.rabbitmq.utills.RabbitMqUtill;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class task {
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtill.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        final Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要发送的消息：");
        while (scanner.hasNext()) {

            final String message = scanner.nextLine(); // 注意这里不能用next()
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送了消息：" + message);

        }

    }
}
