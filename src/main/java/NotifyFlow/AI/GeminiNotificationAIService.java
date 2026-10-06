package NotifyFlow.AI;

import NotifyFlow.event.NotificationEvent;
import NotifyFlow.notification.NotificationDraft;
import NotifyFlow.notification.NotificationPriority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GeminiNotificationAIService
        implements NotificationAIService {

    private static final Logger log =
            LoggerFactory.getLogger(GeminiNotificationAIService.class);

    private final Client client;
    private final ObjectMapper objectMapper;

    public GeminiNotificationAIService() {
        this.client = new Client();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public NotificationDraft generateNotification(
            NotificationEvent event) {

        try {

            log.info(
                    "Generating notification with Gemini eventId={} type={} source={}",
                    event.getEventId(),
                    event.getType(),
                    event.getSource()
            );

            String prompt = """
                    You are part of an intelligent notification platform.

                    Convert the incoming event into a concise user notification.

                    Rules:
                    - Do not invent information.
                    - Keep the title short.
                    - Keep the body concise.
                    - priority must be exactly one of:
                      HIGH, MEDIUM, LOW.

                    Event type: %s
                    Source: %s
                    Message: %s
                    """.formatted(
                    event.getType(),
                    event.getSource(),
                    event.getMessage()
            );

            GenerateContentConfig config =
                    GenerateContentConfig.builder()
                            .responseMimeType("application/json")
                            .responseSchema(
                                    NotificationAISchema.create()
                            )
                            .build();

            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-3.8-flash",
                            prompt,
                            config
                    );

            AIResponse aiResponse =
                    objectMapper.readValue(
                            response.text(),
                            AIResponse.class
                    );

            NotificationDraft draft =
                    new NotificationDraft(
                            aiResponse.title(),
                            aiResponse.body(),
                            NotificationPriority.valueOf(
                                    aiResponse.priority()
                            )
                    );

            log.info(
                    "Gemini notification generated successfully eventId={} priority={}",
                    event.getEventId(),
                    draft.priority()
            );

            return draft;

        } catch (Exception e) {

            log.error(
                    "Gemini notification generation failed eventId={} type={}",
                    event.getEventId(),
                    event.getType(),
                    e
            );

            throw new RuntimeException(
                    "Failed to generate notification with Gemini",
                    e
            );
        }
    }

    private record AIResponse(
            String title,
            String body,
            String priority
    ) {
    }
}