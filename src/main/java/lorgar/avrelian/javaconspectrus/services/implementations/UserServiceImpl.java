package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.User;
import lorgar.avrelian.javaconspectrus.services.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private Map<Long, User> users = new HashMap<>();
    private Long generatedUserId = 1L;

    @Override
    public User createUser(User user) {
        users.put(generatedUserId, user);
        generatedUserId++;
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public User updateUser(Long userId, User user) {
        users.put(generatedUserId, user);
        return user;
    }

    @Override
    public User deleteUser(Long userId) {
        return users.remove(userId);
    }
}
