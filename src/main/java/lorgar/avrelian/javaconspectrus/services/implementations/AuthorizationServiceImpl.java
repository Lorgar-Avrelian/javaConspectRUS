package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.dao.Login;
import lorgar.avrelian.javaconspectrus.dto.BasicAuthDTO;
import lorgar.avrelian.javaconspectrus.dto.LoginDTO;
import lorgar.avrelian.javaconspectrus.dto.RegisterDTO;
import lorgar.avrelian.javaconspectrus.mappers.LoginMapper;
import lorgar.avrelian.javaconspectrus.models.Role;
import lorgar.avrelian.javaconspectrus.repository.LoginRepository;
import lorgar.avrelian.javaconspectrus.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса {@link AuthorizationService}.<br>
 * Сервис для управления учётными данными авторизованных пользователей.
 *
 * @author Victor Tokovenko
 * @see AuthorizationService
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AuthorizationServiceImpl implements AuthorizationService {
    private static boolean initFlag;
    private final JpaUserDetailsServiceImpl userDetailsService;
    private final LoginRepository loginRepository;
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServiceImpl(@Qualifier(value = "jpaUserDetailsServiceImpl") JpaUserDetailsServiceImpl userDetailsService,
                                    LoginRepository loginRepository,
                                    LoginMapper loginMapper,
                                    @Lazy @Qualifier(value = "passwordEncoder") PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.loginRepository = loginRepository;
        this.loginMapper = loginMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginDTO login(BasicAuthDTO basicAuthDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(basicAuthDTO.getLogin()).orElse(null);
        if (login != null) {
            if (passwordEncoder.matches(login.getPassword(), passwordEncoder.encode(basicAuthDTO.getPassword()))) {
                userDetailsService.loadUserByUsername(basicAuthDTO.getLogin());
                return loginMapper.loginToLoginDTO(login);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public LoginDTO register(RegisterDTO registerDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(registerDTO.getLogin()).orElse(null);
        if (login != null || !registerDTO.getPassword().equals(registerDTO.getPasswordConfirmation())) {
            return null;
        }
        login = loginMapper.registerDTOToLogin(registerDTO);
        login.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        if (!initFlag) {
            long count = loginRepository.count();
            if (count > 0) {
                initFlag = true;
            } else {
                login.setRole(Role.OWNER);
            }
        }
        login.setRole(Role.USER);
        login = loginRepository.save(login);
        return loginMapper.loginToLoginDTO(login);
    }
}
