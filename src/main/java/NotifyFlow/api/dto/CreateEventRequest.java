package NotifyFlow.api.dto;

import NotifyFlow.event.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEventRequest(

        @NotBlank
        String eventId,

        @NotBlank
        String userId,

        @NotBlank
        String source,

        @NotNull
        EventType type,

        @NotBlank
        String message

) {
}