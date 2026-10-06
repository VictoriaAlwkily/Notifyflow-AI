package NotifyFlow.delivery;

import NotifyFlow.notification.DeliveryChannel;
import NotifyFlow.notification.NotificationPriority;

import java.util.UUID;

public record DeliveryCommand(
        UUID notificationId,
        String userId,
        String title,
        String body,
        NotificationPriority priority,
        DeliveryChannel channel
) {
}