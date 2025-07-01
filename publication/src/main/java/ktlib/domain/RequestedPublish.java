package ktlib.domain;
<<<<<<< HEAD

import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RequestedPublish extends AbstractEvent {

=======
 
import java.util.*;
import ktlib.infra.AbstractEvent;
import lombok.*;
 
@Data
@ToString
public class RequestedPublish extends AbstractEvent {
 
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    private Long manuscriptId;
    private String title;
    private String content;
    private String status;
    private Long authorId;
    private String authorNickname;
    private Date lastModified;
<<<<<<< HEAD
}
=======
 
    public boolean validate() {
        return manuscriptId != null &&
               title != null && !title.trim().isEmpty() &&
               content != null && !content.trim().isEmpty();
    }
}
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
