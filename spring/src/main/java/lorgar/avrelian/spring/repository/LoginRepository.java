package lorgar.avrelian.spring.repository;

import lorgar.avrelian.spring.dao.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByLoginEqualsIgnoreCase(String login);
}
