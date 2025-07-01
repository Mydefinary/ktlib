package ktlib.infra;

<<<<<<< HEAD
import javax.transaction.Transactional;

import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
import ktlib.service.PointService;

=======
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
//<<< Clean Arch / Inbound Adaptor
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
@Service
@Transactional
public class PolicyHandler {

    @Autowired
<<<<<<< HEAD
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    // BookSubscribed ↓
=======
    PointRepository pointRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookSubscribed'"
    )
    public void wheneverBookSubscribed_DecreasePoints(
        @Payload BookSubscribed bookSubscribed
    ) {
<<<<<<< HEAD
        System.out.println(
            "\n\n##### listener DecreasePoints : " + bookSubscribed + "\n\n"
        );
        pointService.decreasePoints(bookSubscribed);
    }

    // RegisteredUser ↓
=======
        BookSubscribed event = bookSubscribed;
        System.out.println(
            "\n\n##### listener DecreasePoints : " + bookSubscribed + "\n\n"
        );

        // Sample Logic //
        Point.decreasePoints(event);
    }

>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredUser'"
    )
    public void wheneverRegisteredUser_RegisterPoints(
        @Payload RegisteredUser registeredUser
    ) {
<<<<<<< HEAD
        System.out.println(
            "\n\n##### listener RegisterPoints : " + registeredUser + "\n\n"
        );
        Point.registerPoints(registeredUser);
    }

    // 더미 리스너 (원하면 삭제 가능)
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}
}
=======
        RegisteredUser event = registeredUser;
        System.out.println(
            "\n\n##### listener RegisterPoints : " + registeredUser + "\n\n"
        );

        // Sample Logic //
        Point.registerPoints(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
