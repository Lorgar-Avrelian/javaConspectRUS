package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewBookDTO;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.services.implementations.BookServiceImplDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service for making CRUD operations with {@link Book} entities by using {@link JpaRepository} methods.
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 * @see BookServiceImplDB
 */
@Service
public interface BookService {
    /**
     * Method is saving new {@link Book} to DB and returning entity of the saved {@link Book}.
     *
     * @param newBookDTO that should be saved
     * @return {@code book} - that is saved
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#createBook(NewBookDTO)
     */
    Book createBook(NewBookDTO newBookDTO);

    /**
     * Method is returning entity of the {@link Book} by its ID.
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is founded or {@link null} if {@link Book} is not founded
     * @see BookServiceImplDB#findBook(long)
     */
    Book findBook(long id);

    /**
     * Method is saving edited {@link BookDTO} and returning entity of the saved {@link Book}.
     *
     * @param book that should be edited
     * @return {@code book} - that is saved or {@link null} if {@link Book} is not founded
     * @see BookServiceImplDB#editBook(BookDTO)
     */
    Book editBook(BookDTO book);

    /**
     * Method is deleting {@link Book} and returning entity of the deleted {@link Book}.
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is deleted or {@link null} if {@link Book} is not founded
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#deleteBook(long)
     */
    Book deleteBook(long id);

    /**
     * Method is returning {@link Collection} of all {@link Book}.
     *
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#getAllBooks()
     */
    Collection<Book> getAllBooks();

    /**
     * Method is returning {@link Collection} of all {@link Book}.
     *
     * @param authorOrTitle part of author name or book title
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#getAllBooks(String)
     */
    Collection<Book> getAllBooks(String authorOrTitle);
}