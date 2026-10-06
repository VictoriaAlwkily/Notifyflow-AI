package NotifyFlow.delivery;

import NotifyFlow.messaging.RabbitConfig;
import NotifyFlow.notification.Notification;
import NotifyFlow.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterWorker {

    private static final Logger log =
            LoggerFactory.getLogger(DeadLetterWorker.class);

    private final NotificationService notificationService;

    public DeadLetterWorker(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @RabbitListener(
            queues = RabbitConfig.DELIVERY_DLQ
    )
    public void handleFailedDelivery(
            DeliveryCommand command) {

        log.warn(
                "Delivery command reached DLQ notificationId={} userId={} channel={}",
                command.notificationId(),
                command.userId(),
                command.channel()
        );

        Notification notification =
                notificationService
                        .getById(
                                command.notificationId()
                        );

        notificationService
                .markFailed(notification);

        log.error(
                "Notification marked as FAILED notificationId={}",
                command.notificationId()
        );
    }
}