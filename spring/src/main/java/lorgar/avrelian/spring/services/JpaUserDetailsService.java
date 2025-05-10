package lorgar.avrelian.spring.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с сущностями пользователей {@link UserDetails}, сохранёнными в БД,<br>
 * с использованием {@link JpaRepository} и {@link UserDetailsService}
 */
@Service
public interface JpaUserDetailsService extends UserDetailsService {
}