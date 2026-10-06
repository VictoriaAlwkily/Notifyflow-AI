package NotifyFlow.policy.policy;

import NotifyFlow.event.EventType;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import NotifyFlow.policy.NotificationPolicyEngine;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificationPolicyEngineTest {

    private final NotificationPolicyEngine policyEngine =
            new NotificationPolicyEngine();

    @Test
    void securityAlertShouldAlwaysBeHighPriority() {

        NotificationEvent event =
                new NotificationEvent(
                        "evt-test-1",
                        "user-1",
                        "security-service",
                        EventType.SECURITY_ALERT,
                        "Suspicious login detected",
                        Instant.now()
                );

        NotificationDraft aiDraft =
                new NotificationDraft(
                        "Security alert",
                        "Suspicious login detected",
                        NotificationPriority.LOW
                );

        NotificationDraft result =
                policyEngine.apply(event, aiDraft);

        assertEquals(
                NotificationPriority.HIGH,
                result.priority()
        );
    }

    @Test
    void nonSecurityEventShouldKeepOriginalPriority() {

        NotificationEvent event =
                new NotificationEvent(
                        "evt-test-2",
                        "user-1",
                        "calendar-service",
                        EventType.CALENDAR_REMINDER,
                        "Meeting starts soon",
                        Instant.now()
                );

        NotificationDraft aiDraft =
                new NotificationDraft(
                        "Meeting reminder",
                        "Meeting starts soon",
                        NotificationPriority.MEDIUM
                );

        NotificationDraft result =
                policyEngine.apply(event, aiDraft);

        assertEquals(
                NotificationPriority.MEDIUM,
                result.priority()
        );
    }
}