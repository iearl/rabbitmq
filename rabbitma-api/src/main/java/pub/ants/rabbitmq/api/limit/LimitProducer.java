package pub.ants.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/14 下午11:09
 * @description: 限流生产者
 */
public class LimitProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String exchangeName="test_limit_exchange";
        String message ="限流消息消费";
        String routingKey = "test.limit";
        for(int i=0;i<9;i++) {
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        }
        System.out.println("消息发送结束");
        channel.close();
        connection.close();
    }
}
