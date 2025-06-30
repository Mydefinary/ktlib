package ktlib.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='NotDecreasedPoints'"
    )
    public void wheneverNotDecreasedPoints_SuggestPeriodSubscription(
        @Payload NotDecreasedPoints notDecreasedPoints
    ) {
        NotDecreasedPoints event = notDecreasedPoints;
        System.out.println(
            "\n\n##### listener SuggestPeriodSubscription : " +
            notDecreasedPoints +
            "\n\n"
        );
        // Sample Logic //

    }

    // 기존에 이벤트 스토밍에서 넣지 않은 스티커 추가 
    // (포인트 등록됨 -> 유저정보에 point 등록 Policy)
    @StreamListener( 
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredPoints'"
    )
    public void wheneverRegisteredPoints_UpdateUserPoints(
        @Payload RegisteredPoints registeredPoints
    ) {
        System.out.println("\n\n##### listener UpdateUserPoints : " + registeredPoints + "\n\n");

        userRepository.findById(registeredPoints.getUserId()).ifPresent(user -> {
            user.setPoint(registeredPoints.getPointBalance()); // 포인트 업데이트
            userRepository.save(user);
        });
    }

}
//>>> Clean Arch / Inbound Adaptor
