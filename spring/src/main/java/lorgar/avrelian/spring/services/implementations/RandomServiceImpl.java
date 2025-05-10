package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.services.RandomService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomServiceImpl implements RandomService {
    private final Random random;

    public RandomServiceImpl(Random random) {
        this.random = random;
    }

    @Override
    public Integer getRandomNextInt() {
        return random.nextInt();
    }
}