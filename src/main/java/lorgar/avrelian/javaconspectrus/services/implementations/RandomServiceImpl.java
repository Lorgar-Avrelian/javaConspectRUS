package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.services.RandomService;
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
