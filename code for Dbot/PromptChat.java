import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ContextException;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PromptChat implements ICommand {

    private final OkHttpClient client = new OkHttpClient();
    private final MediaType mediaType = MediaType.parse("application/json");
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String getName() {
        return "prompt";
    }

    @Override
    public String getDescription() {
        return "Chat with AI";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "prompt", "Provide a prompt to start the conversation", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String prompt = event.getOption("prompt").getAsString();

        final Message initialMessage = event.getChannel().sendMessage("Thinking...").complete(); // Send initial partial message

        scheduleAiResponseFetch(prompt, initialMessage, event);
    }

    private void scheduleAiResponseFetch(String prompt, Message initialMessage, SlashCommandInteractionEvent event) {
        scheduler.schedule(() -> {
            String jsonPayload = constructJsonPayload(prompt);
            RequestBody requestBody = RequestBody.create(jsonPayload, mediaType);
            Request request = buildRequest(requestBody);

            // Fetch AI response asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        String content = extractContent(responseBody);
                        if (content != null) {
                            editInitialMessage(initialMessage, content, event);
                        } else {
                            editInitialMessage(initialMessage, "Failed to get response from Server.", event);
                        }
                    } else {
                        editInitialMessage(initialMessage, "Failed to get response from Server.", event);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    editInitialMessage(initialMessage, "Sorry Couldn't pass the GATE try again with asking to make the answer short.", event);
                }
            });
        }, 1, TimeUnit.SECONDS); // Schedule after 3 seconds
    }


    private String extractContent(String responseBody) {
        Gson gson = new Gson();
        ChatCompletionMessage completionMessage = gson.fromJson(responseBody, ChatCompletionMessage.class);
        if (completionMessage.choices != null && completionMessage.choices.length > 0) {
            return completionMessage.choices[0].message.content;
        } else {
            return null;
        }
    }

    private String constructJsonPayload(String prompt) {
        // Customize JSON payload to your LM studio API requirements
        String jsonPayload = String.format("" +
                "{\"messages\": [{\"role\": \"system\", \"content\": \" answer funny, reply within 50 tokens .\"}, {\"role\": \"user\", \"content\": \"%s\"}]}", prompt);
        return jsonPayload;
    }

    private Request buildRequest(RequestBody requestBody) {
        // Customize request based on your LM studio API endpoint and authentication
        Request request = new Request.Builder()
                .url("url from LM studio Server section")
                .method("POST", requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        return request;
    }

    private void editInitialMessage(Message initialMessage, String content, SlashCommandInteractionEvent event) {
        initialMessage.editMessage(content).queue();
    }
}
class ChatCompletionMessage {
    ChatChoice[] choices;
}

class ChatChoice {
    ChatMessage message;
}

class ChatMessage {
    String content;
}
