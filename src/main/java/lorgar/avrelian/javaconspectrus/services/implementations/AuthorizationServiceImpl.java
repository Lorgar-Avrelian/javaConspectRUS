package lorgar.avrelian.javaconspectrus.services.implementations;

import lombok.extern.java.Log;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * Реализация сервиса {@link AuthorizationService}.<br>
 * Сервис для управления учётными данными авторизованных пользователей.
 *
 * @author Victor Tokovenko
 * @see AuthorizationService
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class AuthorizationServiceImpl implements AuthorizationService {
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
    public Login login(BasicAuthDTO basicAuthDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(basicAuthDTO.getLogin()).orElse(null);
        if (login != null) {
            if (passwordEncoder.matches(basicAuthDTO.getPassword(), login.getPassword())) {
                userDetailsService.loadUserByUsername(basicAuthDTO.getLogin());
                return login;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Login register(RegisterDTO registerDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(registerDTO.getLogin()).orElse(null);
        if (login != null || !registerDTO.getPassword().equals(registerDTO.getPasswordConfirmation())) {
            return null;
        }
        login = loginMapper.registerDTOToLogin(registerDTO);
        login.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        if (loginRepository.count() == 0) {
            login.setRole(Role.ROLE_OWNER);
        } else {
            login.setRole(Role.ROLE_USER);
        }
        login = loginRepository.save(login);
        return login;
    }

    @Override
    public List<LoginDTO> getAllUsers() {
        return loginMapper.loginListToLoginDTOList(loginRepository.findAll()).stream()
                          .sorted(Comparator.comparing(LoginDTO::getId))
                          .toList();
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"})
    public List<LoginDTO> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Login> logins = loginRepository.findAll(pageRequest);
        return loginMapper.loginListToLoginDTOList(logins.getContent());
    }

    @Override
    @PreAuthorize("hasRole('OWNER')")
    public LoginDTO setRole(UserDetails userDetails, long id, Role role) throws IllegalArgumentException {
        if (role == Role.ROLE_OWNER) {
            throw new IllegalArgumentException("Should be only one of ROLE_OWNER");
        }
        Login login = loginRepository.findById(id).orElse(null);
        if (login != null) {
            login.setRole(role);
            login = loginRepository.save(login);
            return loginMapper.loginToLoginDTO(login);
        } else {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"})
    public LoginDTO setPassword(UserDetails userDetails, long id, String password) throws IllegalArgumentException {
        Login login = loginRepository.findById(id).orElse(null);
        Login user = loginRepository.findByLoginEqualsIgnoreCase(userDetails.getUsername()).get();
        if (login != null && (login.getLogin().equals(user.getLogin()) || user.getRole().compareTo(login.getRole()) > 0)) {
            login.setPassword(passwordEncoder.encode(password));
            login = loginRepository.save(login);
            return loginMapper.loginToLoginDTO(login);
        } else {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public LoginDTO delete(UserDetails userDetails, long id) throws IllegalArgumentException {
        if (id == 1) {
            log.info("User " + userDetails.getUsername() + " tried to delete owner!");
            throw new IllegalArgumentException("Owner could not be deleted!");
        }
        Login user = loginRepository.findByLoginEqualsIgnoreCase(userDetails.getUsername()).get();
        Login login = loginRepository.findById(id).orElse(null);
        if (login == null) {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
        if ((user.getRole().compareTo(login.getRole())) > 0) {
            loginRepository.deleteById(id);
            return loginMapper.loginToLoginDTO(login);
        } else {
            return null;
        }
    }
}