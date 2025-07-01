package ktlib.domain;

<<<<<<< HEAD
import java.util.Date;
import ktlib.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;
=======
import java.time.LocalDate;
import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442

//<<< DDD / Domain Event
@Data
@ToString
public class DecreasedPoints extends AbstractEvent {

    private Long pointId;
    private Long userId;
    private Long pointBalance;
    private Date pointSpendDate;

    public DecreasedPoints(Point aggregate) {
        super(aggregate);
    }

    public DecreasedPoints() {
        super();
    }
}
<<<<<<< HEAD
=======
//>>> DDD / Domain Event
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
