package lorgar.avrelian.javaconspectrus.services.implementations;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

@DisplayName(value = "Test of CounterServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CounterServiceImplTest {
    @InjectMocks
    private CounterServiceImpl counterService;

    @Test
    @DisplayName(value = "getCounter(): return int")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCounter1() {
        int actual = counterService.getCounter();
        Assertions.assertEquals(1, actual);
        actual = counterService.getCounter();
        Assertions.assertEquals(2, actual);
    }

    @Test
    @DisplayName(value = "setCounter(int counter): return int")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setCounter1() {
        counterService.getCounter();
        counterService.getCounter();
        counterService.getCounter();
        int actual = counterService.getCounter();
        Assertions.assertEquals(6, actual);
        int expected = 1;
        counterService.setCounter(expected - 1);
        actual = counterService.getCounter();
        Assertions.assertEquals(expected, actual);
    }
}