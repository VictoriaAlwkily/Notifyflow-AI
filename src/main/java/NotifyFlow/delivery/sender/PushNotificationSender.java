package NotifyFlow.delivery.sender;

import NotifyFlow.delivery.DeliveryCommand;
import NotifyFlow.notification.DeliveryChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PushNotificationSender
        implements NotificationSender {

    private static final Logger log =
            LoggerFactory.getLogger(PushNotificationSender.class);

    @Override
    public boolean supports(DeliveryChannel channel) {
        return channel == DeliveryChannel.PUSH;
    }

    @Override
    public void send(DeliveryCommand command) {

        log.info(
                "Sending PUSH notification userId={} notificationId={}",
                command.userId(),
                command.notificationId()
        );
    }
}