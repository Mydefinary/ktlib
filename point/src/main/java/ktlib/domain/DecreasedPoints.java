package ktlib.domain;

import java.util.Date;
import ktlib.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

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
