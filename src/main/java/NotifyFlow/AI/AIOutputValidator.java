package NotifyFlow.AI;

import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import org.springframework.stereotype.Component;

@Component
public class AIOutputValidator {

    public boolean isValid(NotificationDraft draft) {

        if (draft == null) {
            return false;
        }

        if (draft.title() == null || draft.title().isBlank()) {
            return false;
        }

        if (draft.body() == null || draft.body().isBlank()) {
            return false;
        }

        if (draft.priority() == null) {
            return false;
        }

        return true;
    }
}