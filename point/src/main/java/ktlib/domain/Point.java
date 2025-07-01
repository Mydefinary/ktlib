package ktlib.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlib.PointApplication;
import ktlib.domain.BoughtPoints;
import ktlib.domain.DecreasedPoints;
import ktlib.domain.NotDecreasedPoints;
import ktlib.domain.RegisteredPoints;
<<<<<<< HEAD
import ktlib.domain.BookSubscribed;
import ktlib.domain.DecreasedPoints;
import ktlib.domain.NotDecreasedPoints;
import org.springframework.context.ApplicationContext;
import java.util.Optional;

=======
import ktlib.domain.RegisteredUser;
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442
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
        PointRepository pointRepository = PointApplication.applicationContext.getBean(
            PointRepository.class
        );
        return pointRepository;
    }

    //<<< Clean Arch / Port Method
    public static void decreasePoints(BookSubscribed bookSubscribed) {
        //implement business logic here:

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        DecreasedPoints decreasedPoints = new DecreasedPoints(point);
        decreasedPoints.publishAfterCommit();
        NotDecreasedPoints notDecreasedPoints = new NotDecreasedPoints(point);
        notDecreasedPoints.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(bookSubscribed.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            DecreasedPoints decreasedPoints = new DecreasedPoints(point);
            decreasedPoints.publishAfterCommit();
            NotDecreasedPoints notDecreasedPoints = new NotDecreasedPoints(point);
            notDecreasedPoints.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void registerPoints(RegisteredUser registeredUser) {
        //implement business logic here:
<<<<<<< HEAD

        /** Example 1:  new item 
        Point point = new Point();
        repository().save(point);

        RegisteredPoints registeredPoints = new RegisteredPoints(point);
        registeredPoints.publishAfterCommit();
        */

        /** Example 2:  finding and process
        

        repository().findById(registeredUser.get???()).ifPresent(point->{
            
            point // do something
            repository().save(point);

            RegisteredPoints registeredPoints = new RegisteredPoints(point);
            registeredPoints.publishAfterCommit();

         });
        */
=======
        // 1. 통신사에 따라 초기 포인트 결정
        Long initialPoints = 1000L;
        if ("KT".equalsIgnoreCase(registeredUser.getCarrier())) {
            initialPoints = 1500L;
        }

        // 2. 포인트 엔티티 생성
        Point point = new Point();
        point.setUserId(registeredUser.getUserId()); // 회원 ID 설정
        point.setPointBalance(initialPoints);              // 초기 포인트 설정

        // 3. 저장
        repository().save(point);

        // 4. 이벤트 발행
        RegisteredPoints registeredPoints = new RegisteredPoints(point);
        registeredPoints.publishAfterCommit();
>>>>>>> cc51f632aa39de85878eeed3e45ae4baeaf95442

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
