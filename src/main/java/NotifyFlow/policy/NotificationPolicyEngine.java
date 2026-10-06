package NotifyFlow.policy;

import NotifyFlow.event.EventType;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import org.springframework.stereotype.Service;

@Service
public class NotificationPolicyEngine {

    public NotificationDraft apply(
            NotificationEvent event,
            NotificationDraft draft) {

        if (event.getType() == EventType.SECURITY_ALERT) {

            return new NotificationDraft(
                    draft.title(),
                    draft.body(),
                    NotificationPriority.HIGH
            );
        }

        return draft;
    }
}