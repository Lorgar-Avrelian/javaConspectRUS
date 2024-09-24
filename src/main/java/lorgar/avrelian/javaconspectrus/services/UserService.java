package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);

    User getUserById(Long userId);

    User updateUser(Long userId, User user);

    User deleteUser(Long userId);
}
