package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.models.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    Optional<BookCover> findByBookId(Long bookId);
}
