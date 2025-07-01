package ktlib.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ktlib.domain.BookSubscribed;
import ktlib.domain.Point;
import ktlib.domain.PointRepository;
import ktlib.domain.DecreasedPoints;
import ktlib.domain.NotDecreasedPoints;

@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    public void decreasePoints(BookSubscribed event) {
        if (Boolean.FALSE.equals(event.getPaymentSuccess())) {
            return;
        }
        Long userId = event.getUserId();
        int cost = event.getReadCost() == null ? 0 : event.getReadCost().intValue();

        Optional<Point> optionalPoint = pointRepository.findByUserId(userId);
        if (optionalPoint.isPresent()) {
            Point point = optionalPoint.get();
            if (point.getPointBalance() >= cost) {
                point.setPointBalance(point.getPointBalance() - cost);
                point.setPointSpendDate(new Date());
                pointRepository.save(point);

                DecreasedPoints decreased = new DecreasedPoints(point);
                decreased.publishAfterCommit();
            } else {
                NotDecreasedPoints notDec = new NotDecreasedPoints(point, "포인트 부족");
                notDec.publishAfterCommit();
            }
        } else {
            NotDecreasedPoints notDec = new NotDecreasedPoints();
            notDec.setUserId(userId);
            notDec.setFailReason("사용자 포인트 정보 없음");
            notDec.publishAfterCommit();
        }
    }
}
