package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dao.Login;
import lorgar.avrelian.javaconspectrus.dto.BasicAuthDTO;
import lorgar.avrelian.javaconspectrus.dto.LoginDTO;
import lorgar.avrelian.javaconspectrus.dto.RegisterDTO;
import lorgar.avrelian.javaconspectrus.models.Role;
import lorgar.avrelian.javaconspectrus.services.implementations.AuthorizationServiceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

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
    Login login(BasicAuthDTO basicAuthDTO);

    Login register(RegisterDTO registerDTO);

    List<LoginDTO> getAllUsers();

    List<LoginDTO> getAllUsers(int page, int size);

    LoginDTO setRole(UserDetails userDetails, long id, Role role) throws IllegalArgumentException;

    LoginDTO setPassword(UserDetails userDetails, long id, String password) throws IllegalArgumentException;

    LoginDTO delete(UserDetails userDetails, long id) throws IllegalArgumentException;
}