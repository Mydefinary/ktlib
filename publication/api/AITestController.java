package ktlib.api;
 
import ktlib.infra.AIClient;
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
}