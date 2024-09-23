package lorgar.avrelian.javaconspectrus.services;

import org.springframework.stereotype.Service;

@Service
public interface RandomService {
    Integer getRandomNextInt();
}
