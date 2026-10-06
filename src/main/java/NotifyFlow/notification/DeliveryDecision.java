package NotifyFlow.notification;

public record DeliveryDecision(
        boolean sendNow,
        DeliveryChannel channel
) {
}