package NotifyFlow.delivery.sender;

import NotifyFlow.delivery.DeliveryCommand;
import NotifyFlow.notification.DeliveryChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender
        implements NotificationSender {

    private static final Logger log =
            LoggerFactory.getLogger(EmailNotificationSender.class);

    @Override
    public boolean supports(DeliveryChannel channel) {
        return channel == DeliveryChannel.EMAIL;
    }

    @Override
    public void send(DeliveryCommand command) {

        log.info(
                "Sending EMAIL notification userId={} notificationId={}",
                command.userId(),
                command.notificationId()
        );
    }
}