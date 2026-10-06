package NotifyFlow.messaging;

import NotifyFlow.event.NotificationEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(NotificationEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EVENT_QUEUE,
                event
        );
    }
}