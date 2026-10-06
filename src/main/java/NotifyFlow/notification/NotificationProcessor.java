package NotifyFlow.notification;

import NotifyFlow.AI.AIOutputValidator;
import NotifyFlow.AI.NotificationAIService;
import NotifyFlow.event.EventType;
import NotifyFlow.event.NotificationEvent;
import org.springframework.stereotype.Service;
import NotifyFlow.policy.NotificationPolicyEngine;

@Service
public class NotificationProcessor {

    private final NotificationAIService notificationAIService;
    private final AIOutputValidator aiOutputValidator;
    private final NotificationPolicyEngine policyEngine;

    public NotificationProcessor(
            NotificationAIService notificationAIService,
            AIOutputValidator aiOutputValidator,  NotificationPolicyEngine policyEngine) {

        this.notificationAIService = notificationAIService;
        this.aiOutputValidator = aiOutputValidator;
        this.policyEngine = policyEngine;
    }

    public NotificationDraft process(NotificationEvent event) {

        try {

            NotificationDraft draft =
                    notificationAIService
                            .generateNotification(event);

            if (!aiOutputValidator.isValid(draft)) {
                return fallback(event);
            }

             return policyEngine.apply(event, draft);

        } catch (Exception e) {

            return fallback(event);
        }
    }

    private NotificationDraft fallback(
            NotificationEvent event) {

        if (event.getType() == EventType.SECURITY_ALERT) {
            return new NotificationDraft(
                    "Security alert",
                    event.getMessage(),
                    NotificationPriority.HIGH
            );
        }

        if (event.getType() == EventType.CALENDAR_REMINDER) {
            return new NotificationDraft(
                    "Calendar reminder",
                    event.getMessage(),
                    NotificationPriority.MEDIUM
            );
        }

        return new NotificationDraft(
                "Document update",
                event.getMessage(),
                NotificationPriority.LOW
        );
    }
}