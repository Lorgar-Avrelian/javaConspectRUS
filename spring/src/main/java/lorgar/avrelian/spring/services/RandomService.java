package lorgar.avrelian.spring.services;

import org.springframework.stereotype.Service;

@Service
public interface RandomService {
    Integer getRandomNextInt();
}