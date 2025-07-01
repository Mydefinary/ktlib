package ktlib.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointPolicyTest {

    @Test
    public void testPointDeduction() {
        // 테스트할 로직 작성
        int before = 1000;
        int deduction = 200;
        int after = before - deduction;

        assertEquals(800, after);
    }
}