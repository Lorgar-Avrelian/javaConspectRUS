package lorgar.avrelian.spring.services.implementations;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DisplayName(value = "Test of RandomServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RandomServiceImplTest {
    private final Random random = new Random();
    @InjectMocks
    private RandomServiceImpl randomService;

    @Test
    @DisplayName(value = "getRandomNextInt(): return Integer")
    @Order(1)
    void getRandomNextInt() {
        randomService = new RandomServiceImpl(random);
        int actual;
        int previous = 0;
        for (int i = 0; i < 1_000; i++) {
            actual = randomService.getRandomNextInt();
            assertTrue(actual >= Integer.MIN_VALUE && actual <= Integer.MAX_VALUE);
            assertNotEquals(previous, actual);
            previous = actual;
        }
    }
}