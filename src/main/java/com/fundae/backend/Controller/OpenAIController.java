package com.fundae.backend.Controller;


import com.fundae.backend.Service.OpenAIService;
import com.fundae.backend.dto.ChatRequestDTO;
import com.fundae.backend.dto.ChatResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/openai")
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @PostMapping(
            value = "/chat",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ChatResponseDTO> chat(@RequestBody ChatRequestDTO req) {
        return openAIService.chat(req);
    }
}

