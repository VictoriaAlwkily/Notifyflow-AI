package NotifyFlow.delivery.sender;

import NotifyFlow.delivery.DeliveryCommand;
import NotifyFlow.notification.DeliveryChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InAppNotificationSender
        implements NotificationSender {

    private static final Logger log =
            LoggerFactory.getLogger(InAppNotificationSender.class);

    @Override
    public boolean supports(DeliveryChannel channel) {
        return channel == DeliveryChannel.IN_APP;
    }

    @Override
    public void send(DeliveryCommand command) {

        log.info(
                "Sending IN_APP notification userId={} notificationId={}",
                command.userId(),
                command.notificationId()
        );
    }
}