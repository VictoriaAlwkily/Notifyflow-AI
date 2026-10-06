package NotifyFlow.delivery.sender;

import NotifyFlow.delivery.DeliveryCommand;
import NotifyFlow.notification.DeliveryChannel;

public interface NotificationSender {

    boolean supports(DeliveryChannel channel);

    void send(DeliveryCommand command);
}