package NotifyFlow.messaging;

import NotifyFlow.delivery.DeliveryCommand;
import NotifyFlow.delivery.DeliveryPublisher;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.DeliveryDecision;
import NotifyFlow.notification.Notification;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationProcessor;
import NotifyFlow.notification.service.NotificationService;
import NotifyFlow.policy.DeliveryPolicyService;
import NotifyFlow.preference.UserPreference;
import NotifyFlow.preference.UserPreferenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(EventConsumer.class);

    private final NotificationProcessor notificationProcessor;
    private final UserPreferenceService userPreferenceService;
    private final DeliveryPolicyService deliveryPolicyService;
    private final DeliveryPublisher deliveryPublisher;
    private final NotificationService notificationService;

    public EventConsumer(
            NotificationProcessor notificationProcessor,
            UserPreferenceService userPreferenceService,
            DeliveryPolicyService deliveryPolicyService,
            DeliveryPublisher deliveryPublisher,
            NotificationService notificationService) {

        this.notificationProcessor = notificationProcessor;
        this.userPreferenceService = userPreferenceService;
        this.deliveryPolicyService = deliveryPolicyService;
        this.deliveryPublisher = deliveryPublisher;
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitConfig.EVENT_QUEUE)
    public void consume(NotificationEvent event) {

        log.info(
                "Received event eventId={} userId={} type={} source={}",
                event.getEventId(),
                event.getUserId(),
                event.getType(),
                event.getSource()
        );

        // 1. Create notification draft using AI + validation + policy
        NotificationDraft draft =
                notificationProcessor.process(event);

        log.info(
                "Notification generated eventId={} priority={} title={}",
                event.getEventId(),
                draft.priority(),
                draft.title()
        );

        // 2. Load user preferences
        UserPreference preference =
                userPreferenceService
                        .getPreferences(event.getUserId());

        // 3. Decide whether to send now and through which channel
        DeliveryDecision decision =
                deliveryPolicyService
                        .decide(draft, preference);

        log.info(
                "Delivery decision eventId={} sendNow={} channel={}",
                event.getEventId(),
                decision.sendNow(),
                decision.channel()
        );

        // 4. Save notification in PostgreSQL as PENDING
        Notification notification =
                notificationService.create(
                        event,
                        draft,
                        decision.channel()
                );

        log.info(
                "Notification persisted notificationId={} eventId={} status={}",
                notification.getId(),
                event.getEventId(),
                notification.getStatus()
        );

        // 5. If notification should be sent now,
        // create and publish a delivery command
        if (decision.sendNow()) {

            DeliveryCommand command =
                    new DeliveryCommand(
                            notification.getId(),
                            event.getUserId(),
                            draft.title(),
                            draft.body(),
                            draft.priority(),
                            decision.channel()
                    );

            deliveryPublisher.publish(command);

            log.info(
                    "Delivery command published notificationId={} channel={}",
                    notification.getId(),
                    decision.channel()
            );

        } else {

            log.info(
                    "Notification deferred notificationId={} eventId={}",
                    notification.getId(),
                    event.getEventId()
            );
        }
    }
}