package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dto.BasicAuthDTO;
import lorgar.avrelian.javaconspectrus.dto.LoginDTO;
import lorgar.avrelian.javaconspectrus.dto.RegisterDTO;
import lorgar.avrelian.javaconspectrus.services.implementations.AuthorizationServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления данными авторизованных пользователей.<br>
 * Имеет следующие реализации:<br>
 * - {@link AuthorizationServiceImpl};
 *
 * @author Victor Tokovenko
 * @see AuthorizationServiceImpl
 */
@Service
public interface AuthorizationService {
    LoginDTO login(BasicAuthDTO basicAuthDTO);

    LoginDTO register(RegisterDTO registerDTO);
}
