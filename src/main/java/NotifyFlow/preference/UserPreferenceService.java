package NotifyFlow.preference;

import NotifyFlow.preference.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {

    private final UserPreferenceRepository repository;

    public UserPreferenceService(
            UserPreferenceRepository repository) {

        this.repository = repository;
    }

    public UserPreference getPreferences(String userId) {

        return repository
                .findByUserId(userId)
                .orElseGet(() ->
                        createDefaultPreferences(userId)
                );
    }

    private UserPreference createDefaultPreferences(
            String userId) {

        UserPreference preference =
                new UserPreference(
                        userId,
                        true,   // push
                        false,  // email
                        true,   // in-app
                        true,   // quiet hours
                        23,
                        7
                );

        return repository.save(preference);
    }
}