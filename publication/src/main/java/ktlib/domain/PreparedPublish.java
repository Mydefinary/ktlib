package ktlib.domain;
<<<<<<< HEAD

=======
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
import java.time.LocalDate;
import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;
<<<<<<< HEAD

=======
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
//<<< DDD / Domain Event
@Data
@ToString
public class PreparedPublish extends AbstractEvent {
<<<<<<< HEAD

=======
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    private Long publicationId;
    private Long manuscriptId;
    private String coverUrl;
    private String genre;
    private String summary;
    private Long readCost;
    private Date publicationDate;
<<<<<<< HEAD

    public PreparedPublish(Publish aggregate) {
        super(aggregate);
    }

=======
 
    public PreparedPublish(Publish aggregate) {
        super(aggregate);
        this.publicationId = aggregate.getPublicationId();
        this.manuscriptId = aggregate.getManuscriptId();
        this.coverUrl = aggregate.getCoverUrl();
        this.genre = aggregate.getGenre();
        this.summary = aggregate.getSummary();
        this.readCost = aggregate.getReadCost();
        this.publicationDate = aggregate.getPublicationDate();
    }
 
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    public PreparedPublish() {
        super();
    }
}
<<<<<<< HEAD
//>>> DDD / Domain Event
=======
//>>> DDD / Domain Event
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
