package ktlib.domain;

import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RegisteredUser extends AbstractEvent {

    private Long userId;
    private String name;
    private Long point;
<<<<<<< HEAD
=======
    private String carrier;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
}
