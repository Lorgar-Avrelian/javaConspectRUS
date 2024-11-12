package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.dao.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByLoginEqualsIgnoreCase(String login);
}
