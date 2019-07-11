package pub.ants.rabbitmq.api.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:52
 * @description: rabbitmq exchange=direct模式下得生产者
 */
public class DirectProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1. 获得连接
        Connection connection = ConnectionUtil.getConnection();
        //2. 创建channel
        Channel channel = connection.createChannel();
        //3. 生命交换机 发送得消息 routing
        String exchangeName = "test_exchange_direct";
        String msg = "hello direct exchange!";
        String routingKey = "test.direct1";
        //4. 发送消息
        channel.basicPublish(exchangeName,routingKey,null,msg.getBytes());
        //5. 关闭连接
        channel.close();
        connection.close();
    }
}
