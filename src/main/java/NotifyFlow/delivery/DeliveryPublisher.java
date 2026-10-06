package NotifyFlow.delivery;

import NotifyFlow.messaging.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DeliveryPublisher(
            RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(DeliveryCommand command) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.DELIVERY_QUEUE,
                command
        );
    }
}