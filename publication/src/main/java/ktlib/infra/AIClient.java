package ktlib.infra;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
 
import java.util.List;
import java.util.Map;
 
@Component
public class AIClient {
 
    @Value("${OPENAI_API_KEY}")
    private String apiKey;
 
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
 
    private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String IMAGE_URL = "https://api.openai.com/v1/images/generations";
 
    // 1. 줄거리 요약
    public String summarize(String content) {
        String prompt = "Summarize the following story in three sentences or fewer. Story: " + content;
        return callChatGPT(prompt);
    }
 
    // 2. 장르 분류
    public String classifyGenre(String content) {
        String prompt = "Identify the most fitting literary genre for the following story. " +
                        "Possible genres include fantasy, romance, mystery, coming-of-age, drama, etc. Story: " + content;
        return callChatGPT(prompt);
    }
 
    // 3. 가격 추천 (숫자만 추출)
    public String recommendPrice(String content) {
        String prompt = "Estimate an appropriate monthly subscription price in Korean won for the following story. " +
                        "Only respond with the numeric value (e.g., 3200). Story: " + content;
        return callChatGPT(prompt).replaceAll("[^0-9]", ""); // 숫자만 추출
    }
 
    // 4. 표지 이미지 생성
    public String generateCoverImage(String title, String content) {
        try {
            String prompt = String.format(
                "Create a high-quality 3D-rendered image of a single hardcover book standing upright.\n" +
                "The book is titled \"%s\".\n" +
                "Its story is about: %s\n\n" +
                "Design the front cover to visually reflect the core feeling or theme of the story. " +
                "Use symbolic or abstract imagery that conveys the mood — such as hope, loneliness, growth, mystery, or wonder.\n" +
                "The cover should use artistic and metaphorical visuals that hint at the genre and tone without using any text or characters.\n" +
                "Keep the background simple and softly lit. Focus on making the book appear visually striking and emotionally resonant.",
                title, content
            );
 
            Map<String, Object> body = Map.of(
                "model", "dall-e-3",
                "prompt", prompt,
                "n", 1,
                "size", "1024x1024",
                "quality", "standard"
            );
 
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
 
            ResponseEntity<String> response = restTemplate.postForEntity(IMAGE_URL, request, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());
 
            return json.at("/data/0/url").asText();
 
        } catch (Exception e) {
            System.err.println("❌ 이미지 생성 실패: " + e.getMessage());
            return null;
        }
    }
 
    // 내부 공통 GPT 호출
    private String callChatGPT(String prompt) {
        try {
            Map<String, Object> body = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                    Map.of("role", "system", "content", "You are a helpful assistant."),
                    Map.of("role", "user", "content", prompt)
                )
            );
 
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
 
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
 
            ResponseEntity<String> response = restTemplate.postForEntity(CHAT_URL, request, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());
 
            return json.at("/choices/0/message/content").asText();
 
        } catch (Exception e) {
            System.err.println("❌ GPT 호출 실패: " + e.getMessage());
            return "";
        }
    }
}