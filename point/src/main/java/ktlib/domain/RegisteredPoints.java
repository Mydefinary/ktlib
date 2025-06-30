package ktlib.domain;

import java.util.Date;
import ktlib.infra.AbstractEvent;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegisteredPoints extends AbstractEvent {

    private Long pointId;
    private Long userId;
    private Long pointBalance;
    private Date pointRechargeDate;
    private Date pointSpendDate;

    public RegisteredPoints(Point aggregate) {
        super(aggregate);
        this.pointId = aggregate.getPointId();
        this.userId = aggregate.getUserId();
        this.pointBalance = aggregate.getPointBalance();
        this.pointRechargeDate = aggregate.getPointRechargeDate();
        this.pointSpendDate = aggregate.getPointSpendDate();
    }

    public RegisteredPoints() {
        super();
    }
}

