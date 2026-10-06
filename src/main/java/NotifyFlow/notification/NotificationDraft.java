package NotifyFlow.notification;

public record NotificationDraft(
                                String title,
                                String body,
                                NotificationPriority priority
)  {
}
