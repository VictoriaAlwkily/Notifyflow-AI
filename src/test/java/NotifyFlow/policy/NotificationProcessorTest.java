package NotifyFlow.policy;

import NotifyFlow.AI.AIOutputValidator;
import NotifyFlow.AI.NotificationAIService;
import NotifyFlow.event.EventType;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import NotifyFlow.notification.NotificationProcessor;
import NotifyFlow.policy.NotificationPolicyEngine;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationProcessorTest {

    @Test
    void validAiDraftShouldBeProcessedByPolicyEngine() {

        NotificationAIService aiService =
                event -> new NotificationDraft(
                        "AI title",
                        "AI body",
                        NotificationPriority.LOW
                );

        AIOutputValidator validator =
                new AIOutputValidator();

        NotificationPolicyEngine policyEngine =
                new NotificationPolicyEngine();

        NotificationProcessor processor =
                new NotificationProcessor(
                        aiService,
                        validator,
                        policyEngine
                );

        NotificationEvent event =
                new NotificationEvent(
                        "evt-processor-1",
                        "user-1",
                        "security-service",
                        EventType.SECURITY_ALERT,
                        "Suspicious login detected",
                        Instant.now()
                );

        NotificationDraft result =
                processor.process(event);

        assertEquals(
                "AI title",
                result.title()
        );

        assertEquals(
                "AI body",
                result.body()
        );

        // Policy engine must override LOW -> HIGH
        assertEquals(
                NotificationPriority.HIGH,
                result.priority()
        );
    }

    @Test
    void invalidAiDraftShouldUseFallback() {

        NotificationAIService aiService =
                event -> new NotificationDraft(
                        "",
                        "",
                        null
                );

        AIOutputValidator validator =
                new AIOutputValidator();

        NotificationPolicyEngine policyEngine =
                new NotificationPolicyEngine();

        NotificationProcessor processor =
                new NotificationProcessor(
                        aiService,
                        validator,
                        policyEngine
                );

        NotificationEvent event =
                new NotificationEvent(
                        "evt-processor-2",
                        "user-1",
                        "calendar-service",
                        EventType.CALENDAR_REMINDER,
                        "Meeting starts soon",
                        Instant.now()
                );

        NotificationDraft result =
                processor.process(event);

        assertEquals(
                "Calendar reminder",
                result.title()
        );

        assertEquals(
                "Meeting starts soon",
                result.body()
        );

        assertEquals(
                NotificationPriority.MEDIUM,
                result.priority()
        );
    }

    @Test
    void aiExceptionShouldUseFallback() {

        NotificationAIService aiService =
                event -> {
                    throw new RuntimeException(
                            "Gemini unavailable"
                    );
                };

        AIOutputValidator validator =
                new AIOutputValidator();

        NotificationPolicyEngine policyEngine =
                new NotificationPolicyEngine();

        NotificationProcessor processor =
                new NotificationProcessor(
                        aiService,
                        validator,
                        policyEngine
                );

        NotificationEvent event =
                new NotificationEvent(
                        "evt-processor-3",
                        "user-1",
                        "document-service",
                        EventType.DOCUMENT_ACTIVITY,
                        "Document was updated",
                        Instant.now()
                );

        NotificationDraft result =
                processor.process(event);

        assertEquals(
                "Document update",
                result.title()
        );

        assertEquals(
                "Document was updated",
                result.body()
        );

        assertEquals(
                NotificationPriority.LOW,
                result.priority()
        );
    }
}