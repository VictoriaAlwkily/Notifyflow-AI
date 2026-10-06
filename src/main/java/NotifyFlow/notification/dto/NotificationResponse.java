package NotifyFlow.notification.dto;

import NotifyFlow.notification.DeliveryChannel;
import NotifyFlow.notification.NotificationPriority;
import NotifyFlow.notification.NotificationStatus;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String eventId,
        String userId,
        String title,
        String body,
        NotificationPriority priority,
        DeliveryChannel channel,
        NotificationStatus status,
        Instant createdAt
) {
}