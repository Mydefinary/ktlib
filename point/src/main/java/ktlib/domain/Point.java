package ktlib.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.*;

import ktlib.PointApplication;
import ktlib.domain.BookSubscribed;
import ktlib.domain.BoughtPoints;
import ktlib.domain.DecreasedPoints;
import ktlib.domain.NotDecreasedPoints;
import ktlib.domain.RegisteredPoints;
import ktlib.domain.RegisteredUser;
import lombok.Data;

@Entity
@Table(name = "Point_table")
@Data
//<<< DDD / Aggregate Root
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pointId;

    private Long pointBalance;

    private Date pointRechargeDate;

    private Date pointSpendDate;

    private Long userId;

    public static PointRepository repository() {
        return PointApplication.applicationContext.getBean(PointRepository.class);
    }

// 포인트 차감 처리
public static void decreasePoints(BookSubscribed bookSubscribed) {
    Long userId = bookSubscribed.getUserId();

    Optional<Point> optionalPoint = repository().findByUserId(userId);
    if (optionalPoint.isPresent()) {
        Point point = optionalPoint.get();

        // 포인트 차감 로직 (예시: 500 포인트 차감)
        Long cost = bookSubscribed.getReadCost();  

        if (point.getPointBalance() >= cost) {
            point.setPointBalance(point.getPointBalance() - cost);
            point.setPointSpendDate(new Date());
            repository().save(point);

            DecreasedPoints decreasedPoints = new DecreasedPoints(point);
            decreasedPoints.publishAfterCommit();
        } else {
            NotDecreasedPoints notDecreasedPoints = new NotDecreasedPoints(point, "포인트 부족");
            notDecreasedPoints.publishAfterCommit();
        }
    }
} // <- 여기서 메서드 종료

// 포인트 등록 처리
public static void registerPoints(RegisteredUser registeredUser) {
    Long initialPoints = 1000L;
    if ("KT".equalsIgnoreCase(registeredUser.getCarrier())) {
    initialPoints = 6000L;
}

    Point point = new Point();
    point.setUserId(registeredUser.getUserId());
    point.setPointBalance(initialPoints);
    point.setPointRechargeDate(new Date());

    repository().save(point);

    RegisteredPoints registeredPoints = new RegisteredPoints(point);
    registeredPoints.publishAfterCommit();
}
}

