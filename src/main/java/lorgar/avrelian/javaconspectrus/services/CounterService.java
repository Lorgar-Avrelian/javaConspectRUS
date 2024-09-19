package lorgar.avrelian.javaconspectrus.services;

import org.springframework.stereotype.Service;

@Service
public interface CounterService {
    int getCounter();

    int setCounter(int counter);
}
