package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
