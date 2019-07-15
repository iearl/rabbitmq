package pub.ants.rabbitmq.api.dlx;

import com.rabbitmq.client.*;
import pub.ants.rabbitmq.api.utils.ConnectionUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/14 下午11:09
 * @description: ttl消息，以及死信息队列
 */
public class DlxProducer {
        public static void main(String[] args) throws IOException, TimeoutException {
            final Connection connection = ConnectionUtil.getConnection();
            Channel channel = connection.createChannel();

            String exchangeName="test_dlx_exchange1";
            String message ="测试消息的过期时间,我是消息i";
            String routingKey = "test.dlx";

            //采用异步confirm的发誓发送设置了过期的时间消息
            final TreeSet<Long> confirmSet = new TreeSet<>();
            channel.confirmSelect();//开启confirm模式
            //添加confirm异步监听
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    if(multiple){
                        confirmSet.headSet(deliveryTag-1).clear();
                    }else{
                        confirmSet.remove(deliveryTag);
                    }
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    if(multiple){
                        confirmSet.headSet(deliveryTag-1).clear();
                    }else{
                        confirmSet.remove(deliveryTag);
                    }
                }
            });

            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)
                    .contentEncoding("utf-8")
                    .expiration("10000").build();
            for (int i=0;i<3;i++){
                long nextPublishSeqNo = channel.getNextPublishSeqNo();
                channel.basicPublish(exchangeName,routingKey,true, basicProperties,(message+i).getBytes());
                confirmSet.add(nextPublishSeqNo);
            }

    }
}
