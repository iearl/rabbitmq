package pub.ants.rabbitmq.api.limit;

import com.rabbitmq.client.*;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/14 下午11:09
 * @description: 限流消费者
 */
public class LimitConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        String exchangeName = "test_limit_exchange";
        String routingKey = "test.limit";
        String queueName = "test_limit_queue";
        String type = "topic";

        channel.queueDeclare(queueName, false, false, false, null);
        channel.exchangeDeclare(exchangeName, type);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicQos(1);

        channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //出现异常重新入队
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                    e.printStackTrace();
                }
                System.out.println(new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });


    }
}
