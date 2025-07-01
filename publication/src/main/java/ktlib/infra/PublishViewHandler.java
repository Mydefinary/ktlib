package ktlib.infra;

import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.PreparedPublish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class PublishViewHandler {

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PreparedPublish'"
    )
    public void wheneverPreparedPublish_ReceiveEvent(@Payload PreparedPublish event) {
        if (event == null) return;

        log.info("📘 PreparedPublish 수신됨:");
        log.info("   📖 publicationId: {}", event.getPublicationId());
        log.info("   📕 manuscriptId: {}", event.getManuscriptId());
        log.info("   🖼️ coverUrl: {}", event.getCoverUrl());
        log.info("   🏷️ genre: {}", event.getGenre());
        log.info("   📚 summary: {}", event.getSummary());
        log.info("   💰 readCost: {}", event.getReadCost());
        log.info("   🕒 publicationDate: {}", event.getPublicationDate());

        // 이곳에 ReadModel 업데이트 로직이나 외부 알림 전송 등을 추가 가능
    }

    // 예외 상황용 fallback 로그
    @StreamListener(KafkaProcessor.INPUT)
    public void fallback(@Payload byte[] raw) {
        String rawMsg = new String(raw);
        if (rawMsg.contains("PreparedPublish")) {
            log.warn("❗ PreparedPublish 이벤트가 수신되었지만 조건문과 매칭되지 않음: {}", rawMsg);
        }
    }
}
