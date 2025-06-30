package ktlib.api;
 
import ktlib.domain.RequestedPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.stream.function.StreamBridge;
 
@RestController
@RequestMapping("/test")
public class KafkaTestController {
 
    @Autowired
    private StreamBridge streamBridge;
 
    @PostMapping("/publish")
    public String sendPublishEvent(@RequestBody RequestedPublish event) {
        streamBridge.send("requestedPublish-out-0",
            MessageBuilder
                .withPayload(event)
                .setHeader("type", "RequestedPublish")
                .build()
        );
        return "✅ RequestedPublish 이벤트 발행됨";
    }
}
 