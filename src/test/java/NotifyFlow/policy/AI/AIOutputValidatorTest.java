package NotifyFlow.policy.AI;

import NotifyFlow.AI.AIOutputValidator;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AIOutputValidatorTest {

    private final AIOutputValidator validator =
            new AIOutputValidator();

    @Test
    void validDraftShouldPassValidation() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Security Alert",
                        "Suspicious login detected",
                        NotificationPriority.HIGH
                );

        assertTrue(
                validator.isValid(draft)
        );
    }

    @Test
    void nullDraftShouldFailValidation() {

        assertFalse(
                validator.isValid(null)
        );
    }

    @Test
    void blankTitleShouldFailValidation() {

        NotificationDraft draft =
                new NotificationDraft(
                        "",
                        "Message body",
                        NotificationPriority.MEDIUM
                );

        assertFalse(
                validator.isValid(draft)
        );
    }

    @Test
    void blankBodyShouldFailValidation() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Title",
                        "",
                        NotificationPriority.MEDIUM
                );

        assertFalse(
                validator.isValid(draft)
        );
    }

    @Test
    void nullPriorityShouldFailValidation() {

        NotificationDraft draft =
                new NotificationDraft(
                        "Title",
                        "Message body",
                        null
                );

        assertFalse(
                validator.isValid(draft)
        );
    }
}