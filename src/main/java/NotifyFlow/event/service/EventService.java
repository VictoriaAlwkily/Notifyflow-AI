package NotifyFlow.event.service;

import NotifyFlow.api.dto.CreateEventRequest;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.event.repository.EventRepository;
import NotifyFlow.messaging.EventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventPublisher eventPublisher;

    public EventService(
            EventRepository eventRepository,
            EventPublisher eventPublisher) {

        this.eventRepository = eventRepository;
        this.eventPublisher = eventPublisher;
    }

    public NotificationEvent createEvent(
            CreateEventRequest request) {

        if (eventRepository.existsByEventId(request.eventId())) {
            throw new IllegalArgumentException(
                    "Event already exists: " + request.eventId()
            );
        }

        NotificationEvent event =
                new NotificationEvent(
                        request.eventId(),
                        request.userId(),
                        request.source(),
                        request.type(),
                        request.message(),
                        Instant.now()
                );

        NotificationEvent savedEvent =
                eventRepository.save(event);

        eventPublisher.publish(savedEvent);

        return savedEvent;
    }
}