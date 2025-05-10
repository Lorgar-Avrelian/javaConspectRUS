package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.services.CounterService;
import org.springframework.stereotype.Service;

@Service
public class CounterServiceImpl implements CounterService {
    private static int counter = 0;

    public CounterServiceImpl() {
    }

    @Override
    public int getCounter() {
        return ++counter;
    }

    @Override
    public int setCounter(int counter) {
        this.counter = counter;
        return this.counter;
    }
}