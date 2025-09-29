package com.fundae.backend.Service;


import com.fundae.backend.dto.ChatRequestDTO;
import com.fundae.backend.dto.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final WebClient openAIClient;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.chat-endpoint}")
    private String chatEndpoint;

    public Mono<ChatResponseDTO> chat(ChatRequestDTO req) {
        String model = (req.getModel() != null && !req.getModel().isBlank()) ? req.getModel() : "gpt-4.1";
        Integer maxTokens = (req.getMaxTokens() != null) ? req.getMaxTokens() : 150;
        Double temperature = (req.getTemperature() != null) ? req.getTemperature() : 0.7;

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", req.getMessage())));
        body.put("max_tokens", maxTokens);
        body.put("temperature", temperature);

        if (req.getExtra() != null) {
            for (Map<String, Object> kv : req.getExtra()) body.putAll(kv);
        }

        return openAIClient.post()
                .uri(chatEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(apiKey))
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(json -> {
                    try {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) json.get("choices");
                        Map<String, Object> first = (choices != null && !choices.isEmpty()) ? choices.get(0) : null;
                        Map<String, Object> message = (first != null) ? (Map<String, Object>) first.get("message") : null;
                        String content = (message != null) ? (String) message.get("content") : "";
                        ChatResponseDTO out = new ChatResponseDTO();
                        out.setContent(content);
                        return out;
                    } catch (Exception e) {
                        ChatResponseDTO out = new ChatResponseDTO();
                        out.setContent("No se pudo leer la respuesta del modelo.");
                        return out;
                    }
                });
    }
}
