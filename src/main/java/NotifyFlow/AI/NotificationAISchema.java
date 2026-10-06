package NotifyFlow.AI;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;

import java.util.Arrays;
import java.util.Map;

public class NotificationAISchema {

    public static Schema create() {

        return Schema.builder()
                .type(Type.Known.OBJECT)
                .properties(
                        Map.of(
                                "title",
                                Schema.builder()
                                        .type(Type.Known.STRING)
                                        .build(),

                                "body",
                                Schema.builder()
                                        .type(Type.Known.STRING)
                                        .build(),

                                "priority",
                                Schema.builder()
                                        .type(Type.Known.STRING)
                                        .build()
                        )
                )
                .required(
                        Arrays.asList(
                                "title",
                                "body",
                                "priority"
                        )
                )
                .build();
    }
}