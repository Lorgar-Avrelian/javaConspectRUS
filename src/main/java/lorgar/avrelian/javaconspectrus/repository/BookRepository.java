package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * An implementation of the {@link JpaRepository}.<br>
 * Repository for CRUD operations with {@link Book} entities
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.
     *
     * @param author part of author name
     * @param title  part of book title
     * @return {@code Collection<Book>} of founded books
     */
    List<Book> findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String author, String title);

    /**
     * Method is returning {@link Collection} of all {@link Book} of {@link Reader} with this ID from DB.
     *
     * @param readerId ID of reader
     * @return {@code Collection<Book>} of founded books
     */
    List<Book> findByReaderId(long readerId);
}
