package NotifyFlow.delivery.repository;

import NotifyFlow.delivery.DeliveryAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryAttemptRepository
        extends JpaRepository<DeliveryAttempt, UUID> {

    long countByNotificationId(UUID notificationId);
}