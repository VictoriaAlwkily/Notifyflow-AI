package NotifyFlow.preference;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private boolean pushEnabled;

    @Column(nullable = false)
    private boolean emailEnabled;

    @Column(nullable = false)
    private boolean inAppEnabled;

    @Column(nullable = false)
    private boolean quietHoursEnabled;

    @Column(nullable = false)
    private int quietStartHour;

    @Column(nullable = false)
    private int quietEndHour;

    public UserPreference() {
    }

    public UserPreference(
            String userId,
            boolean pushEnabled,
            boolean emailEnabled,
            boolean inAppEnabled,
            boolean quietHoursEnabled,
            int quietStartHour,
            int quietEndHour) {

        this.userId = userId;
        this.pushEnabled = pushEnabled;
        this.emailEnabled = emailEnabled;
        this.inAppEnabled = inAppEnabled;
        this.quietHoursEnabled = quietHoursEnabled;
        this.quietStartHour = quietStartHour;
        this.quietEndHour = quietEndHour;
    }

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public boolean isInAppEnabled() {
        return inAppEnabled;
    }

    public boolean isQuietHoursEnabled() {
        return quietHoursEnabled;
    }

    public int getQuietStartHour() {
        return quietStartHour;
    }

    public int getQuietEndHour() {
        return quietEndHour;
    }
}