package com.vylitkova.closetMATE.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vylitkova.closetMATE.entity.item.enums.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;
    private static final String OPENAI_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public Map<String, Object> categorizeItem(String imageUrl) throws Exception {
        String prompt = String.format(
                "Define every category from the list about the wardrobe item on the photo.\n" +
                        "- Type: %s\n" +
                        "- Colors: %s\n" +
                        "- Color_warmness: warm, cold, neutral\n" +
                        "- Color_darkness: light, dark, neutral\n" +
                        "- Pattern: %s\n" +
                        "- Material: %s\n" +
                        "- Season: %s\n" +
                        "- Minimum temperature outside:\n" +
                        "- Maximum temperature outside:\n" +
                        "- Formality: %s\n" +
                        "- Style: %s\n" +
                        "- Mood: %s\n" +
                        "- Purpose: %s\n\n" +
                        "Analyze the item and return JSON response with these attributes. Mind that only Colors attribute should contain all colors of the item, other should have 1 value. If the item is an accessory, set its minimal and maximal temperature due to the seasons average range of temperature, including numbers lower than 0",
                EnumUtils.enumToString(ItemType.class),
                EnumUtils.enumToString(Color.class),
                EnumUtils.enumToString(ItemPattern.class),
                EnumUtils.enumToString(ItemMaterial.class),
                EnumUtils.enumToString(ItemSeason.class),
                EnumUtils.enumToString(ItemFormality.class),
                EnumUtils.enumToString(ItemStyle.class),
                EnumUtils.enumToString(ItemMood.class),
                EnumUtils.enumToString(ItemPurpose.class)
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");  // Використовуємо модель, що підтримує зображення
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a fashion AI assistant."),
                Map.of("role", "user", "content", prompt),
                Map.of("role", "user", "content", List.of(
                        Map.of("type", "image_url", "image_url", Map.of("url", imageUrl))
                ))
        ));
        requestBody.put("temperature", 0.5);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(OPENAI_ENDPOINT, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String content = jsonResponse.get("choices").get(0).get("message").get("content").asText();
            content = content.replaceAll("```json", "").replaceAll("```", "").trim();
            content = content.replaceAll("(\"Minimum temperature outside\"\\s*:\\s*)(\\d+)", "$1\"$2\"");
            content = content.replaceAll("(\"Maximum temperature outside\"\\s*:\\s*)(\\d+)", "$1\"$2\"");

            return objectMapper.readValue(content, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON response from OpenAI: " + responseBody, e);
        }
    }


}
