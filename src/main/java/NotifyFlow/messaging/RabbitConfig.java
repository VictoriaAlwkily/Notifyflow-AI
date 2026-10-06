package NotifyFlow.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EVENT_QUEUE =
            "notification.events";

    public static final String DELIVERY_QUEUE =
            "notification.delivery";

    public static final String DELIVERY_DLQ =
            "notification.delivery.dlq";

    public static final String DELIVERY_DLX =
            "notification.delivery.dlx";

    public static final String DELIVERY_DLQ_ROUTING_KEY =
            "notification.delivery.failed";


    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE, true);
    }


    @Bean
    public Queue deliveryQueue() {

        return QueueBuilder
                .durable(DELIVERY_QUEUE)

                // אם message נכשל סופית,
                // RabbitMQ ישלח אותו ל-DLX
                .deadLetterExchange(DELIVERY_DLX)

                // routing key עבור הודעות שנכשלו
                .deadLetterRoutingKey(
                        DELIVERY_DLQ_ROUTING_KEY
                )

                .build();
    }


    @Bean
    public DirectExchange deliveryDeadLetterExchange() {

        return new DirectExchange(
                DELIVERY_DLX
        );
    }


    @Bean
    public Queue deliveryDeadLetterQueue() {

        return QueueBuilder
                .durable(DELIVERY_DLQ)
                .build();
    }


    @Bean
    public Binding deliveryDeadLetterBinding(
            Queue deliveryDeadLetterQueue,
            DirectExchange deliveryDeadLetterExchange) {

        return BindingBuilder
                .bind(deliveryDeadLetterQueue)
                .to(deliveryDeadLetterExchange)
                .with(DELIVERY_DLQ_ROUTING_KEY);
    }


    @Bean
    public JacksonJsonMessageConverter messageConverter() {

        return new JacksonJsonMessageConverter();
    }
}