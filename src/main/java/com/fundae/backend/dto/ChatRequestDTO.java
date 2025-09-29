package com.fundae.backend.dto;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ChatRequestDTO {
    private String message;
    private String model;
    private Integer maxTokens;
    private Double temperature;
    private List<Map<String, Object>> extra;
}
