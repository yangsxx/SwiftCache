package top.yangsc.swiftcache.Schedule.mq;

import cn.hutool.json.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final String QUEUE_NAME = "aiClipboard.queue";
    
    private final RabbitTemplate rabbitTemplate;
    
    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        System.out.println("Sent: " + message);
    }

    public void sendAiTask(String json) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, json);
    }
}
