package NotifyFlow.policy;

import NotifyFlow.notification.DeliveryChannel;
import NotifyFlow.notification.DeliveryDecision;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import NotifyFlow.preference.UserPreference;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class DeliveryPolicyService {

    public DeliveryDecision decide(
            NotificationDraft draft,
            UserPreference preference) {

        if (draft.priority() == NotificationPriority.HIGH) {

            return new DeliveryDecision(
                    true,
                    DeliveryChannel.PUSH
            );
        }

        if (isQuietHours(preference)) {

            return new DeliveryDecision(
                    false,
                    DeliveryChannel.IN_APP
            );
        }

        if (preference.isPushEnabled()) {

            return new DeliveryDecision(
                    true,
                    DeliveryChannel.PUSH
            );
        }

        if (preference.isEmailEnabled()) {

            return new DeliveryDecision(
                    true,
                    DeliveryChannel.EMAIL
            );
        }

        return new DeliveryDecision(
                true,
                DeliveryChannel.IN_APP
        );
    }

    private boolean isQuietHours(
            UserPreference preference) {

        if (!preference.isQuietHoursEnabled()) {
            return false;
        }

        int currentHour = LocalTime.now().getHour();

        int start = preference.getQuietStartHour();
        int end = preference.getQuietEndHour();

        if (start < end) {
            return currentHour >= start
                    && currentHour < end;
        }

        return currentHour >= start
                || currentHour < end;
    }
}