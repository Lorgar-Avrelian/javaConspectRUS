package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.dto.BookDTO;
import lorgar.avrelian.spring.dto.NewBookDTO;
import lorgar.avrelian.spring.mappers.BookMapper;
import lorgar.avrelian.spring.models.Book;
import lorgar.avrelian.spring.repository.BookRepository;
import lorgar.avrelian.spring.services.BookService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * An implementation of {@link BookService}.<br>
 * Service for making CRUD operations with {@link Book} entities by using {@link JpaRepository} methods
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 * @see BookService
 */
@Service
public class BookServiceImplDB implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImplDB(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Method is saving new {@link Book} to DB and returning entity of the saved {@link Book} from DB.<br>
     * Using {@link JpaRepository#save(Object)} method
     *
     * @param newBookDTO that should be saved
     * @return {@code book} - that is saved
     * @throws RuntimeException when DB is not accessible
     * @see JpaRepository#save(Object)
     */
    @Override
    public Book createBook(NewBookDTO newBookDTO) {
        Book book = bookMapper.newBookDTOtoBook(newBookDTO);
        return bookRepository.save(book);
    }

    /**
     * Method is returning entity of the {@link Book} from DB by its ID.<br>
     * Using {@link JpaRepository#findById(Object)}
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is founded or {@link null} if {@link Book} is not founded
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Book findBook(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Method is saving edited {@link Book} to DB and returning entity of the saved {@link Book} from DB.<br>
     * Using {@link JpaRepository#save(Object)} and {@link JpaRepository#existsById(Object)} methods
     *
     * @param book that should be edited
     * @return {@code book} - that is saved or {@link null} if {@link Book} is not founded
     * @see JpaRepository#save(Object)
     * @see JpaRepository#existsById(Object)
     */
    @Override
    public Book editBook(BookDTO book) {
        if (bookRepository.existsById(book.getId())) {
            Book dbBook = bookRepository.findById(book.getId()).get();
            Book editedBook = bookMapper.bookDTOToBook(book);
            editedBook.setReader(dbBook.getReader());
            return bookRepository.save(editedBook);
        } else {
            return null;
        }
    }

    /**
     * Method is deleting {@link Book} from DB and returning entity of the deleted {@link Book}.<br>
     * Using {@link BookServiceImplDB#findBook(long)}, {@link JpaRepository#deleteById(Object)} and {@link JpaRepository#existsById(Object)} methods
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is deleted or {@link null} if {@link Book} is not founded
     * @throws RuntimeException when DB is not accessible
     * @see BookServiceImplDB#findBook(long)
     * @see JpaRepository#deleteById(Object)
     * @see JpaRepository#existsById(Object)
     */
    @Override
    public Book deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            Book book = findBook(id);
            bookRepository.deleteById(id);
            return book;
        } else {
            return null;
        }
    }

    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.<br>
     * Using {@link JpaRepository#findAll()} method
     *
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when DB is not accessible
     * @see JpaRepository#findAll()
     */
    @Override
    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.<br>
     * Using {@link BookRepository#findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String, String)} method
     *
     * @param authorOrTitle part of author name or book title
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when DB is not accessible
     * @see BookRepository#findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String, String)
     */
    @Override
    public Collection<Book> getAllBooks(String authorOrTitle) {
        return bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(authorOrTitle, authorOrTitle);
    }
}