package pub.ants.rabbitmq.api.confirm.asyn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:52
 * @description: rabbitmq exchange=direct模式下得生产者
 */
public class AsynConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1 获得连接
        Connection connection = ConnectionUtil.getConnection();
        //2 创建channel
        Channel channel = connection.createChannel();
        //3 声明
        String exchangeName = "test_asyn_exchange";
        String exchangeType = "direct";
        String queueName = "test_asyn_queue";
        String routingKey = "test.asyn.queue";
        //4 表示声明了一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        //5 表示声明了一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        //6 建立一个绑定关系:
        channel.queueBind(queueName, exchangeName, routingKey);

        //durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);
        //循环获取消息
        while(true){
            //获取消息，如果没有消息，这一步将会一直阻塞
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息：" + msg);
        }
    }
}
