package pub.ants.springboot.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("message: "+new String(message.getBody()));
            System.out.println("replyCode: "+replyCode);
            System.out.println("replyText: "+replyText);
            System.out.println("exchange: "+exchange);
            System.out.println("routingKey: "+routingKey);
        }
    };

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        /**
         *
         * @param correlationData
         * @param ack  确认消息
         * @param cause 异常原因
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            if(ack){
                System.out.println("correlationData: "+correlationData);
            }else{
                System.out.println(" 异常处理 cause: "+cause);
            }
        }
    };

    public void send(Object message, Map<String,Object> properties) throws RuntimeException{
        MessageHeaders msh = new MessageHeaders(properties);
        Message<Object> msg = MessageBuilder.createMessage(message, msh);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //全局唯一
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(String.valueOf(new Date().getTime()));
        rabbitTemplate.convertAndSend("exchange1","spri2ngboot.hello",msg,correlationData);
    }
}
