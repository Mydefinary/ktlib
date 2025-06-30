package ktlib.domain;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlib.PublicationApplication;
import ktlib.domain.PreparedPublish;
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
        PublishRepository publishRepository = PublicationApplication.applicationContext.getBean(
            PublishRepository.class
        );
        return publishRepository;
    }
 
    //<<< Clean Arch / Port Method
    public static void preparePublish(RequestedPublish requestedPublish) {
        //implement business logic here:
 
        /** Example 1:  new item
        Publish publish = new Publish();
        repository().save(publish);
 
        PreparedPublish preparedPublish = new PreparedPublish(publish);
        preparedPublish.publishAfterCommit();
        */
 
        /** Example 2:  finding and process
       
 
        repository().findById(requestedPublish.get???()).ifPresent(publish->{
           
            publish // do something
            repository().save(publish);
 
            PreparedPublish preparedPublish = new PreparedPublish(publish);
            preparedPublish.publishAfterCommit();
 
         });
        */
        Publish publish = new Publish();
 
        // Map event data to entity fields
        publish.setManuscriptId(requestedPublish.getManuscriptId());
        Date today = new Date();
        publish.setPublicationDate(today);
        publish.setCoverUrl("");
        publish.setGenre("");
        publish.setSummary("");
        publish.setReadCost(0L);
 
        // Persist and emit domain event
        repository().save(publish);
        PreparedPublish preparedPublish = new PreparedPublish(publish);
        preparedPublish.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
 
}
//>>> DDD / Aggregate Root