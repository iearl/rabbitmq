package pub.ants.rabbitmq.api.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:38
 * @description: 消息生产者
 *
 *  exchange the exchange to publish the message to  交换机
 *  routingKey the routing key  路由key
 *  props other properties for the message - routing headers etc  属性
 *  body the message body  消息体
 *  void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
 *  发送消息时不指定交换机时，会使用(AMQP default)，使用路由键作为对列的名字，如果存在这个路由键的队列，就把消息路由过去，找不到就发送不出去，发送失败
 *
 *  The default exchange is implicitly bound to every queue, with a routing key equal to the queue name.
 *  It is not possible to explicitly bind to, or unbind from the default exchange. It also cannot be deleted.
 *
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1 创建连接
        Connection connection = ConnectionUtil.getConnection();
        //2 创建信道
        Channel channel = connection.createChannel();
        //3 发送一条消息
        channel.basicPublish("","test",null,"hello,rabbitmq".getBytes());
        System.out.println("发送成功");
        //4 关闭连接，先关小的，再关大的
        channel.close();
        connection.close();
    }
}
