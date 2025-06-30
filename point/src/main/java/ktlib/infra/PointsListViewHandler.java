package ktlib.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import ktlib.config.kafka.KafkaProcessor;
import ktlib.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PointsListViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private PointsListRepository pointsListRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegisteredPoints_then_CREATE_1(
        @Payload RegisteredPoints registeredPoints
    ) {
        try {
            if (!registeredPoints.validate()) return;

            // view 객체 생성
            PointsList pointsList = new PointsList();
            // view 객체에 이벤트의 Value 를 set 함
            pointsList.setPointId(registeredPoints.getPointId());
            pointsList.setUserId(registeredPoints.getUserId());
            pointsList.setPointBalance(registeredPoints.getPointBalance());
            // view 레파지 토리에 save
            pointsListRepository.save(pointsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBoughtPoints_then_UPDATE_1(
        @Payload BoughtPoints boughtPoints
    ) {
        try {
            if (!boughtPoints.validate()) return;
            // view 객체 조회
            Optional<PointsList> pointsListOptional = pointsListRepository.findFirstByPointId(
                boughtPoints.getPointId()
            );

            if (pointsListOptional.isPresent()) {
                PointsList pointsList = pointsListOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                pointsList.setPointBalance(boughtPoints.getPointBalance());
                pointsList.setPointRechargeDate(
                    boughtPoints.getPointRechargeDate()
                );
                // view 레파지 토리에 save
                pointsListRepository.save(pointsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDecreasedPoints_then_UPDATE_2(
        @Payload DecreasedPoints decreasedPoints
    ) {
        try {
            if (!decreasedPoints.validate()) return;
            // view 객체 조회
            Optional<PointsList> pointsListOptional = pointsListRepository.findFirstByPointId(
                decreasedPoints.getPointId()
            );

            if (pointsListOptional.isPresent()) {
                PointsList pointsList = pointsListOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                pointsList.setPointSpendDate(
                    decreasedPoints.getPointSpendDate()
                );
                pointsList.setPointBalance(decreasedPoints.getPointBalance());
                // view 레파지 토리에 save
                pointsListRepository.save(pointsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenNotDecreasedPoints_then_UPDATE_3(
        @Payload NotDecreasedPoints notDecreasedPoints
    ) {
        try {
            if (!notDecreasedPoints.validate()) return;
            // view 객체 조회
            Optional<PointsList> pointsListOptional = pointsListRepository.findFirstByPointId(
                notDecreasedPoints.getPointId()
            );

            if (pointsListOptional.isPresent()) {
                PointsList pointsList = pointsListOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                pointsList.setPointBalance(
                    notDecreasedPoints.getPointBalance()
                );
                // view 레파지 토리에 save
                pointsListRepository.save(pointsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
