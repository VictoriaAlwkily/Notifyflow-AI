package NotifyFlow.delivery;

import NotifyFlow.delivery.sender.NotificationSender;
import NotifyFlow.delivery.sender.SenderResolver;
import NotifyFlow.delivery.service.DeliveryAttemptService;
import NotifyFlow.messaging.RabbitConfig;
import NotifyFlow.notification.Notification;
import NotifyFlow.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryWorker {

    private static final Logger log =
            LoggerFactory.getLogger(DeliveryWorker.class);

    private final NotificationService notificationService;
    private final DeliveryAttemptService deliveryAttemptService;
    private final SenderResolver senderResolver;

    public DeliveryWorker(
            NotificationService notificationService,
            DeliveryAttemptService deliveryAttemptService,
            SenderResolver senderResolver) {

        this.notificationService = notificationService;
        this.deliveryAttemptService = deliveryAttemptService;
        this.senderResolver = senderResolver;
    }

    @RabbitListener(queues = RabbitConfig.DELIVERY_QUEUE)
    public void deliver(DeliveryCommand command) {

        Notification notification =
                notificationService
                        .getById(command.notificationId());

        int attemptNumber =
                deliveryAttemptService
                        .getNextAttemptNumber(
                                command.notificationId()
                        );

        try {

            log.info(
                    "Attempting delivery notificationId={} userId={} channel={} attempt={}",
                    command.notificationId(),
                    command.userId(),
                    command.channel(),
                    attemptNumber
            );



            // Find the correct sender according to channel
            NotificationSender sender =
                    senderResolver.resolve(
                            command.channel()
                    );

            // Send through PUSH / EMAIL / IN_APP
            sender.send(command);

            // Save successful delivery attempt
            deliveryAttemptService.saveSuccess(
                    command.notificationId(),
                    attemptNumber
            );

            // Update notification status to SENT
            notificationService.markSent(notification);

            log.info(
                    "Delivery succeeded notificationId={} channel={} attempt={}",
                    command.notificationId(),
                    command.channel(),
                    attemptNumber
            );

        } catch (Exception e) {

            log.error(
                    "Delivery failed notificationId={} channel={} attempt={}",
                    command.notificationId(),
                    command.channel(),
                    attemptNumber,
                    e
            );

            // Save failed delivery attempt
            deliveryAttemptService.saveFailure(
                    command.notificationId(),
                    attemptNumber,
                    e.getMessage()
            );

            // Rethrow so Spring Rabbit performs retry / DLQ handling
            throw e;
        }
    }
}