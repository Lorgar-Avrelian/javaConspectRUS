package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.services.CounterService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

@DisplayName(value = "Test of CounterServiceImpl")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CounterServiceImplTest {
    private final CounterService counterService = new CounterServiceImpl();

    @Test
    @DisplayName(value = "Get counter test")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getCounter() {
        counterService.setCounter(0);
        int counterValue = counterService.getCounter();
        int expectedValue = 1;
        Assertions.assertEquals(expectedValue, counterValue);
        counterValue = counterService.getCounter();
        expectedValue = 2;
        Assertions.assertEquals(expectedValue, counterValue);
        counterValue = counterService.getCounter();
        expectedValue = 3;
        Assertions.assertEquals(expectedValue, counterValue);
    }

    @Test
    @DisplayName(value = "Set counter test")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setCounter() {
        counterService.setCounter(0);
        int counterValue = counterService.getCounter();
        int expectedValue = 1;
        Assertions.assertEquals(expectedValue, counterValue);
        counterService.getCounter();
        counterService.getCounter();
        counterService.getCounter();
        counterService.getCounter();
        counterService.setCounter(0);
        counterValue = counterService.getCounter();
        expectedValue = 1;
        Assertions.assertEquals(expectedValue, counterValue);
    }

    @Test
    @DisplayName(value = "Set counter test 2")
    @Disabled("Test off")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void setCounter2() {
        counterService.setCounter(0);
        int counterValue = counterService.getCounter();
        int expectedValue = 1;
        Assertions.assertEquals(expectedValue, counterValue);
        counterService.getCounter();
        counterService.getCounter();
        counterService.getCounter();
        counterService.getCounter();
        counterService.setCounter(0);
        counterValue = counterService.getCounter();
        expectedValue = 1;
        Assertions.assertEquals(expectedValue, counterValue);
    }
}