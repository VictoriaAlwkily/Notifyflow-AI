package NotifyFlow.api.dto;

import NotifyFlow.api.dto.CreateEventRequest;
import NotifyFlow.event.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<String> createEvent(
            @Valid @RequestBody CreateEventRequest request) {

        eventService.createEvent(request);

        return ResponseEntity
                .accepted()
                .body("Event accepted");
    }
}