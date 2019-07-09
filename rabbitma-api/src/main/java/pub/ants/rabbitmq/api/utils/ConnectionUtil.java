package pub.ants.rabbitmq.api.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:34
 * @description: rabbitmq 工厂工具类
 */
public class ConnectionUtil {
    //获得rabbitmq的连接工厂
    public static Connection getConnection() throws IOException, TimeoutException {
        //1 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.101.65.44");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        //2 创建连接,并返回
        return factory.newConnection();
    }
}
