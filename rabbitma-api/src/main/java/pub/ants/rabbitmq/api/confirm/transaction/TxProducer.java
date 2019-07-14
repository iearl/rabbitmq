package pub.ants.rabbitmq.api.confirm.transaction;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/14 下午10:09
 * @description: 通过事务机制来完成消息确认发送
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_transaction_confirm";
        String routingKey="test.confirm.transaction";
        String msg = "我通过事务发送的消息,发送的次数是：";
        channel.txSelect();
        for(int i=0;i<5;i++){
            try{
                channel.basicPublish(exchangeName,routingKey,null,(msg+i).getBytes());
                channel.txCommit();
            }catch (Exception e){
                e.printStackTrace();
                channel.txRollback();
            }
        }
    }
}
