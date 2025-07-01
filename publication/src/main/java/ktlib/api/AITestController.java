package ktlib.api;
 
import ktlib.infra.AIClient;
import ktlib.api.dto.AITestRequest;
import ktlib.api.dto.AITestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/ai-test")
public class AITestController {
 
    @Autowired
    private AIClient aiClient;
 
    @GetMapping("/summary")
    public String testSummary(@RequestParam String content) {
        return aiClient.summarize(content);
    }
 
    @GetMapping("/genre")
    public String testGenre(@RequestParam String content) {
        return aiClient.classifyGenre(content);
    }
 
    @GetMapping("/price")
    public String testPrice(@RequestParam String content) {
        return aiClient.recommendPrice(content);
    }
 
    @GetMapping("/cover")
    public String testCover(@RequestParam String title, @RequestParam String content) {
        return aiClient.generateCoverImage(title, content);
    }

    @PostMapping("/all")
    public AITestResult testAll(@RequestBody AITestRequest request) {
        AITestResult result = new AITestResult();
        result.summary = aiClient.summarize(request.content);
        result.genre = aiClient.classifyGenre(request.content);
        result.price = aiClient.recommendPrice(request.content);
        result.cover = aiClient.generateCoverImage(request.title, request.content);
        return result;
    }

}
 
 