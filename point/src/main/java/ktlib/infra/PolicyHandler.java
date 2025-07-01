package ktlib.infra;

import javax.transaction.Transactional;

import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
import ktlib.service.PointService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscribed'"
    )
    public void wheneverBookSubscribed_DecreasePoints(
        @Payload BookSubscribed bookSubscribed
    ) {
        System.out.println(
            "\n\n##### listener DecreasePoints : " + bookSubscribed + "\n\n"
        );
        pointService.decreasePoints(bookSubscribed);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredUser'"
    )
    public void wheneverRegisteredUser_RegisterPoints(
        @Payload RegisteredUser registeredUser
    ) {
        System.out.println(
            "\n\n##### listener RegisterPoints : " + registeredUser + "\n\n"
        );
        Point.registerPoints(registeredUser);
    }

    // 더미 리스너 (원하면 삭제 가능)
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}
}
