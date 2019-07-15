package pub.ants.rabbitmq.api.dlx;

import com.rabbitmq.client.*;

import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/14 下午11:09
 * @description: ttl消息，以及死信息队列
 */
public class DlxConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        final Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        final String exchangeName="test_dlx_exchange1";
        String routingKey = "test.dlx";
        String queueName = "test_dlx_queue1";
        String type = "direct";

        //声明死信队列
        final String dlxExchangeName="test_dlx_exchange_dlx";
        String dlxQueueName = "test_dlx_queue_dlx";
        String dlxType = "direct";


        channel.exchangeDeclare(exchangeName,type);
        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "test_dlx_exchange_dlx");
        channel.queueDeclare(queueName,true,false,false,agruments);
        channel.queueBind(queueName,exchangeName,routingKey);

        //死信队列
        channel.queueDeclare(dlxQueueName,true,false,false,null);
        channel.exchangeDeclare(dlxExchangeName,dlxType);
        channel.queueBind(dlxQueueName,dlxExchangeName,routingKey);

        Consumer consumer = new DefaultConsumer(channel){
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException
            {
                System.out.println(new String(body));
                System.out.println(consumerTag);
                System.out.println(envelope.getDeliveryTag());
                channel.basicAck(envelope.getDeliveryTag(), false);

            }
        };
        channel.basicConsume(queueName,false,consumer);

    }
}
