package ktlib.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import javax.persistence.*;

import ktlib.PublicationApplication;
import ktlib.infra.AIClient;
import lombok.Data;

@Entity
@Table(name = "Publish_table")
@Data
//<<< DDD / Aggregate Root
public class Publish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long publicationId;

    private Long manuscriptId;

    private Date publicationDate;

    @Column(length = 1024)
    private String coverUrl;

    private String genre;

    @Column(length = 1024)
    private String summary;

    private Long readCost;

    public static PublishRepository repository() {
        return PublicationApplication.applicationContext.getBean(PublishRepository.class);
    }

    //<<< Clean Arch / Port Method
    public static void preparePublish(RequestedPublish requestedPublish) {
        Publish publish = new Publish();

        publish.setManuscriptId(requestedPublish.getManuscriptId());
        publish.setPublicationDate(new Date());

        // AIClient 호출
        try {
            AIClient aiClient = PublicationApplication.applicationContext.getBean(AIClient.class);

            String summary = aiClient.summarize(requestedPublish.getContent());
            String genre = aiClient.classifyGenre(requestedPublish.getContent());
            Long price = aiClient.recommendPriceValue(requestedPublish.getContent());
            String coverUrl = aiClient.generateCoverImage(requestedPublish.getTitle(), requestedPublish.getContent());

            publish.setSummary(summary);
            publish.setGenre(genre);
            publish.setReadCost(price);
            publish.setCoverUrl(coverUrl);

            System.out.println("✅ AI 생성 완료:");
            System.out.println("   📚 요약: " + summary);
            System.out.println("   🏷️ 장르: " + genre);
            System.out.println("   💰 가격: " + price);
            System.out.println("   🖼️ 표지 URL: " + coverUrl);

        } catch (Exception e) {
            System.err.println("❌ AI 처리 중 오류 발생: " + e.getMessage());
        }

        // 저장 및 이벤트 발행
        repository().save(publish);
        PreparedPublish preparedPublish = new PreparedPublish(publish);
        preparedPublish.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
