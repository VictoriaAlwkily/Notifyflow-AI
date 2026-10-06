package NotifyFlow.AI;
import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.NotificationDraft;
public interface NotificationAIService {

    NotificationDraft generateNotification(
            NotificationEvent event
    );
}
