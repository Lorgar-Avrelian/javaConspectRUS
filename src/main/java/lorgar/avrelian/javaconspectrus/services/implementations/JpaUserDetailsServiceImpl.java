package lorgar.avrelian.javaconspectrus.services.implementations;

import lombok.extern.java.Log;
import lorgar.avrelian.javaconspectrus.repository.LoginRepository;
import lorgar.avrelian.javaconspectrus.services.JpaUserDetailsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Сервис-реализация интерфейса {@link JpaUserDetailsService} для работы с<br>
 * сущностями пользователей {@link UserDetails}, сохранёнными в БД,<br>
 * с использованием {@link JpaRepository} и {@link UserDetailsService}
 *
 * @see JpaRepository
 * @see JpaUserDetailsService
 * @see UserDetails
 * @see UserDetailsService
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class JpaUserDetailsServiceImpl implements JpaUserDetailsService {
    private final LoginRepository loginRepository;

    public JpaUserDetailsServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Метод для загрузки информации о пользователе по его логину
     *
     * @param username логин пользователя
     * @return информация о пользователе (объект класса {@link UserDetails})
     * @throws UsernameNotFoundException если пользователь с таким логином не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepository.findByLoginEqualsIgnoreCase(username)
                              .map(login -> {
                                  return new User(
                                          login.getLogin(),
                                          login.getPassword(),
                                          Collections.singleton(login.getRole())
                                  );
                              })
                              .orElseThrow(() -> {
                                  log.severe("No DB connection");
                                  return new UsernameNotFoundException(username);
                              });
    }
}