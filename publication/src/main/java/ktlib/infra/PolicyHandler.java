package ktlib.infra;
<<<<<<< HEAD

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
=======
 
import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
 
import lombok.extern.slf4j.Slf4j;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
<<<<<<< HEAD

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    PublishRepository publishRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

=======
 
import javax.transaction.Transactional;
import java.util.Date;
 
@Slf4j
@Service
@Transactional
public class PolicyHandler {
 
    @Autowired
    private PublishRepository publishRepository;
 
    @Autowired
    private AIClient aiClient;
 
    @StreamListener(KafkaProcessor.INPUT)
    public void fallbackHandler(@Payload byte[] raw) {
        System.out.println("📥 테스트용 메시지 수신 (헤더 없음): " + new String(raw));
    }
 
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestedPublish'"
    )
<<<<<<< HEAD
    public void wheneverRequestedPublish_PreparePublish(
        @Payload RequestedPublish requestedPublish
    ) {
        RequestedPublish event = requestedPublish;
        System.out.println(
            "\n\n##### listener PreparePublish : " + requestedPublish + "\n\n"
        );

        // Sample Logic //
        Publish.preparePublish(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
=======
    public void handleRequestedPublish(@Payload RequestedPublish event) {
        log.info("📩 RequestedPublish 이벤트 수신: manuscriptId={}, title={}",
                 event.getManuscriptId(), event.getTitle());
 
        if (!event.validate()) {
            log.warn("⚠️ 유효하지 않은 이벤트. 처리 중단됨.");
            return;
        }
 
        try {
            // 1. 출간 엔티티 초기화
            Publish publish = new Publish();
            publish.setManuscriptId(event.getManuscriptId());
            publish.setPublicationDate(new Date());
 
            // 2. AIClient 호출 - 생성형 결과 값 수집
            String summary = aiClient.summarize(event.getContent());
            String genre = aiClient.classifyGenre(event.getContent());
            String priceStr = aiClient.recommendPrice(event.getContent());
            String imageUrl = aiClient.generateCoverImage(event.getTitle(), event.getContent());
 
            // 3. 결과값 반영
            publish.setSummary(summary);
            publish.setGenre(genre);
            publish.setReadCost(parsePrice(priceStr));
            publish.setCoverUrl(imageUrl);
 
            // 4. 저장
            publishRepository.save(publish);
            log.info("✅ 출간 정보 저장 완료. publicationId={}", publish.getPublicationId());
 
            // 5. 출간준비됨 이벤트 발행
            PreparedPublish preparedEvent = new PreparedPublish(publish);
            preparedEvent.publishAfterCommit();
            log.info("📤 PreparedPublish 이벤트 발행 완료.");
 
        } catch (Exception e) {
            log.error("❌ PolicyHandler 처리 중 오류 발생: {}", e.getMessage(), e);
        }
    }
 
    private Long parsePrice(String priceStr) {
        try {
            return Long.parseLong(priceStr.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            log.warn("⚠️ 가격 파싱 실패. 기본값 0 사용: {}", priceStr);
            return 0L;
        }
    }
}
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
