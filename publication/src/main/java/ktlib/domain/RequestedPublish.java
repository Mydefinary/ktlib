package ktlib.domain;
 
import java.util.*;
import ktlib.infra.AbstractEvent;
import lombok.*;
 
@Data
@ToString
public class RequestedPublish extends AbstractEvent {
 
    private Long manuscriptId;
    private String title;
    private String content;
    private String status;
    private Long authorId;
    private String authorNickname;
    private Date lastModified;
 
    public boolean validate() {
        return manuscriptId != null &&
               title != null && !title.trim().isEmpty() &&
               content != null && !content.trim().isEmpty();
    }
}