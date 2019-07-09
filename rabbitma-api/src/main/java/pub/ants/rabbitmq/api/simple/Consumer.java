package pub.ants.rabbitmq.api.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:52
 * @description: rabbitmq 消费者
 *
 * queue the name of the queue 对列的名字
 * durable true if we are declaring a durable queue (the queue will survive a server restart) 是否持久化
 *                持久化重启服务器不会丢失数据
 * exclusive true if we are declaring an exclusive queue (restricted to this connection) 是否独占
 *                  顺序消费，保证在多线程环境下，abc 消费的进度是有序的
 * autoDelete true if we are declaring an autodelete queue (server will delete it when no longer in use)
 *                   是否自动删除
 * arguments other properties (construction arguments) for the queue  参数
 *
 * Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,
            Map<String, Object> arguments) throws IOException;
 */
public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //1 获得连接
        Connection connection = ConnectionUtil.getConnection();
        //2 创建channel
        Channel channel = connection.createChannel();
        //3 声明对列
        String queueName = "test";
        channel.queueDeclare(queueName,true,false,false,null);
        //4 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //5 设置channel
        channel.basicConsume(queueName,true,queueingConsumer);
        //6 获取消息
        while(true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String s = new String(delivery.getBody());
            System.out.println(s);
            Envelope envelope = delivery.getEnvelope();
            System.out.println(envelope.getDeliveryTag());//全局的id
            System.out.println(envelope.getExchange());
            System.out.println(envelope.getRoutingKey());
            System.out.println("---------------");
        }


    }
}
