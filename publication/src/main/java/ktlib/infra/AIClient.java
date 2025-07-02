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
    String prompt = "The following is a manuscript that will be published as an ebook.\n" +
            "Please summarize its core storyline in **no more than 2 to 3 concise and clear sentences** so that readers can quickly understand the content.\n" +
            "Avoid unnecessary adjectives and focus only on the essential points.\n\n" +
            "Manuscript:\n" + content;
    return callChatGPT(prompt);
}


    // 2. 장르 분류
    public String classifyGenre(String content) {
    String prompt = "Classify the following story into one of the predefined Korean genres below. " +
            "Respond with only the most appropriate sub-genre in Korean, and nothing else.\n\n" +
            "**Available genre categories (Korean):**\n" +
            "- 소설: 로맨스, 판타지, 미스터리, 성장, SF, 현실극\n" +
            "- 인문: 심리학, 철학, 에세이\n" +
            "- 사회: 정치, 경제, 역사\n" +
            "- 과학: 자연과학, IT, 공학\n" +
            "- 청소년: 동화, 청소년소설, 학습\n\n" +
            "Story:\n" + content;

    return callChatGPT(prompt).trim();
}


    // 3-1. 가격 추천 (숫자만 문자열로 추출)
    public String recommendPrice(String content) {
        String prompt = "You are recommending the price per book for a digital subscription service.\n" +
        "Basic users pay 1000 KRW/month, KT users pay 6000 KRW/month.\n" +
        "Based on the following story, suggest a suitable per-book price **between 200 and 750 Korean Won**.\n" +
        "Only respond with a numeric value (e.g., 500), without currency symbols or units.\n" +
        "Story: " + content;

        return callChatGPT(prompt).replaceAll("[^0-9]", ""); // 숫자만 추출
    }

    // 3-2. 가격 추천 (숫자 → Long 변환)
    public Long recommendPriceValue(String content) {
        try {
            String raw = recommendPrice(content);
            return Long.parseLong(raw);
        } catch (Exception e) {
            System.err.println("❌ 가격 변환 실패: " + e.getMessage());
            return 0L;
        }
    }

    // 4. 표지 이미지 생성
    public String generateCoverImage(String title, String content) {
    try {
        String prompt = String.format(
            "Create a high-quality 3D-rendered image of a single hardcover book standing upright.\n" +
            "The book is titled \"%s\".\n" +
            "Its content is summarized as follows: %s\n\n" +
            "Design the front cover to reflect the mood, message, and symbolic meaning of the story.\n" +
            "Use metaphorical imagery to hint at the genre and theme, such as loneliness, love, growth, conflict, or mystery.\n" +
            "The cover should not contain any text or characters.\n" +
            "Use soft lighting and a simple background to highlight the book's emotional tone.\n" +
            "Make the visual compelling and artistically expressive.",
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
