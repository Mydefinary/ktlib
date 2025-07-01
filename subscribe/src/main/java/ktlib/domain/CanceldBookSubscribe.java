package ktlib.domain;

import java.time.LocalDate;
import java.util.*;
import ktlib.domain.*;
import ktlib.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CanceldBookSubscribe extends AbstractEvent {

    private Long subscriptionId;
    private Long bookId;
    private Long userId;
    private Date subscribedAt;
    private Date expriedAt;
    private Date paymentAt;
    private Boolean paymentSuccess;
<<<<<<< HEAD

    public CanceldBookSubscribe(SubscribeSu aggregate) {
        super(aggregate);
=======
    private String reason;

    public CanceldBookSubscribe(SubscribeSu aggregate) {
        super(aggregate);
        this.subscriptionId = aggregate.getId();
        this.bookId = aggregate.getBookId();
        this.userId = aggregate.getUserId();
        this.subscribedAt = aggregate.getSubscribedAt();
        this.expiredAt = aggregate.getExpiredAt();
        this.paymentAt = aggregate.getPaymentAt();
        this.paymentSuccess = aggregate.getPaymentSuccess();
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
    }

    public CanceldBookSubscribe() {
        super();
    }
}
//>>> DDD / Domain Event
