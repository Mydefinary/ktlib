package ktlib.domain;

import java.time.LocalDate;
import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class NotDecreasedPoints extends AbstractEvent {
    private Long pointId;
    private Long userId;
    private Long pointBalance;
    private String failReason;

    public NotDecreasedPoints(Point aggregate, String failReason) {
        super(aggregate);
        this.pointId = aggregate.getPointId();          // getId() → getPointId()
        this.userId = aggregate.getUserId();
        this.pointBalance = aggregate.getPointBalance(); // getBalance() → getPointBalance()
        this.failReason = failReason;
    }

    public NotDecreasedPoints() {
        super();
    }
}
//>>> DDD / Domain Event
