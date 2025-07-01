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

//<<< DDD / Domain Event
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
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
<<<<<<< HEAD
        this.pointId = aggregate.getPointId();
        this.userId = aggregate.getUserId();
        this.pointBalance = aggregate.getPointBalance();
        this.pointRechargeDate = aggregate.getPointRechargeDate();
        this.pointSpendDate = aggregate.getPointSpendDate();
=======
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    }

    public RegisteredPoints() {
        super();
    }
}
<<<<<<< HEAD

=======
//>>> DDD / Domain Event
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
