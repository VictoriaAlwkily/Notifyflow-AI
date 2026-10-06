package NotifyFlow.delivery.sender;

import NotifyFlow.notification.DeliveryChannel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SenderResolver {

    private final List<NotificationSender> senders;

    public SenderResolver(
            List<NotificationSender> senders) {

        this.senders = senders;
    }

    public NotificationSender resolve(
            DeliveryChannel channel) {

        return senders.stream()
                .filter(sender ->
                        sender.supports(channel))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No sender for channel: "
                                        + channel
                        )
                );
    }
}