package NotifyFlow.delivery.service;

import NotifyFlow.delivery.DeliveryAttempt;
import NotifyFlow.delivery.DeliveryStatus;
import org.springframework.stereotype.Service;
import NotifyFlow.delivery.repository.DeliveryAttemptRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class DeliveryAttemptService {

    private final DeliveryAttemptRepository repository;

    public DeliveryAttemptService(
            DeliveryAttemptRepository repository) {
        this.repository = repository;
    }
    public int getNextAttemptNumber(UUID notificationId) {

        long previousAttempts =
                repository.countByNotificationId(notificationId);

        return (int) previousAttempts + 1;
    }

    public void saveSuccess(
            UUID notificationId,
            int attemptNumber) {

        DeliveryAttempt attempt =
                new DeliveryAttempt(
                        notificationId,
                        attemptNumber,
                        DeliveryStatus.SUCCESS,
                        null,
                        Instant.now()
                );

        repository.save(attempt);
    }

    public void saveFailure(
            UUID notificationId,
            int attemptNumber,
            String errorMessage) {

        DeliveryAttempt attempt =
                new DeliveryAttempt(
                        notificationId,
                        attemptNumber,
                        DeliveryStatus.FAILED,
                        errorMessage,
                        Instant.now()
                );

        repository.save(attempt);
    }

}