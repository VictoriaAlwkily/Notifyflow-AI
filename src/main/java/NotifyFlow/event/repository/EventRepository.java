package NotifyFlow.event.repository;

import NotifyFlow.event.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository
        extends JpaRepository<NotificationEvent, UUID> {

    boolean existsByEventId(String eventId);
}