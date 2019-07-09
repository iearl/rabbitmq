package pub.ants.rabbitmq.api.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author magw
 * @version 1.0
 * @date 2019/7/9 下午10:43
 * @description: 流关闭工具类
 */
public class CloseUtil {
    public static void close(Closeable ... close){
        for(Closeable co:close){
            if(co!=null){
                try {
                    co.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
