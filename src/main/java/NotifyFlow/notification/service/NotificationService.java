package NotifyFlow.notification.service;

import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.*;
import NotifyFlow.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import NotifyFlow.notification.dto.NotificationResponse;
import java.util.List;
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    public Notification create(
            NotificationEvent event,
            NotificationDraft draft,
            DeliveryChannel channel) {

        Notification notification =
                new Notification(
                        event.getEventId(),
                        event.getUserId(),
                        draft.title(),
                        draft.body(),
                        draft.priority(),
                        channel,
                        NotificationStatus.PENDING,
                        Instant.now()
                );

        return notificationRepository.save(notification);
    }

    public void markSent(Notification notification) {
        notification.setStatus(NotificationStatus.SENT);
        notificationRepository.save(notification);
    }

    public void markFailed(Notification notification) {
        notification.setStatus(NotificationStatus.FAILED);
        notificationRepository.save(notification);
    }

    public Notification getById(UUID id) {
        return notificationRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Notification not found: " + id
                        )
                );
    }

    public List<NotificationResponse> getAllNotifications() {

        return notificationRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getId(),
                                notification.getEventId(),
                                notification.getUserId(),
                                notification.getTitle(),
                                notification.getBody(),
                                notification.getPriority(),
                                notification.getChannel(),
                                notification.getStatus(),
                                notification.getCreatedAt()
                        )
                )
                .toList();
    }
}