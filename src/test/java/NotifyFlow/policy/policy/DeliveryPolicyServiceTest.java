package NotifyFlow.policy.policy;

import NotifyFlow.notification.DeliveryChannel;
import NotifyFlow.notification.DeliveryDecision;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import NotifyFlow.policy.DeliveryPolicyService;
import NotifyFlow.preference.UserPreference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryPolicyServiceTest {

    private final DeliveryPolicyService deliveryPolicyService =
            new DeliveryPolicyService();

    @Test
    void highPriorityShouldAlwaysUsePushAndSendImmediately() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Security alert",
                        "Suspicious login detected",
                        NotificationPriority.HIGH
                );

        UserPreference preference =
                new UserPreference(
                        "user-1",
                        false,  // push disabled
                        false,  // email disabled
                        true,   // in-app enabled
                        false,  // quiet hours disabled
                        23,
                        7
                );

        DeliveryDecision result =
                deliveryPolicyService.decide(
                        draft,
                        preference
                );

        assertTrue(result.sendNow());

        assertEquals(
                DeliveryChannel.PUSH,
                result.channel()
        );
    }

    @Test
    void mediumPriorityShouldUsePushWhenPushIsEnabled() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Reminder",
                        "Meeting starts soon",
                        NotificationPriority.MEDIUM
                );

        UserPreference preference =
                new UserPreference(
                        "user-2",
                        true,   // push enabled
                        true,   // email enabled
                        true,   // in-app enabled
                        false,  // quiet hours disabled
                        23,
                        7
                );

        DeliveryDecision result =
                deliveryPolicyService.decide(
                        draft,
                        preference
                );

        assertTrue(result.sendNow());

        assertEquals(
                DeliveryChannel.PUSH,
                result.channel()
        );
    }

    @Test
    void shouldUseEmailWhenPushIsDisabledAndEmailEnabled() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Document update",
                        "A document was updated",
                        NotificationPriority.LOW
                );

        UserPreference preference =
                new UserPreference(
                        "user-3",
                        false,  // push disabled
                        true,   // email enabled
                        true,   // in-app enabled
                        false,  // quiet hours disabled
                        23,
                        7
                );

        DeliveryDecision result =
                deliveryPolicyService.decide(
                        draft,
                        preference
                );

        assertTrue(result.sendNow());

        assertEquals(
                DeliveryChannel.EMAIL,
                result.channel()
        );
    }

    @Test
    void shouldFallbackToInAppWhenPushAndEmailAreDisabled() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Document update",
                        "A document was updated",
                        NotificationPriority.LOW
                );

        UserPreference preference =
                new UserPreference(
                        "user-4",
                        false,  // push disabled
                        false,  // email disabled
                        true,   // in-app enabled
                        false,  // quiet hours disabled
                        23,
                        7
                );

        DeliveryDecision result =
                deliveryPolicyService.decide(
                        draft,
                        preference
                );

        assertTrue(result.sendNow());

        assertEquals(
                DeliveryChannel.IN_APP,
                result.channel()
        );
    }
}