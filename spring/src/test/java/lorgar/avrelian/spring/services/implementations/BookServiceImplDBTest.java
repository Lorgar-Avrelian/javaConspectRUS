package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.mappers.BookMapper;
import lorgar.avrelian.spring.models.Book;
import lorgar.avrelian.spring.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.spring.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of BookServiceImplDB")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplDBTest {
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImplDB bookService;

    @Test
    @DisplayName(value = "createBook(NewBookDTO newBookDTO): return Book")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createBook1() {
        when(bookRepository.save(BOOK_1))
                .thenReturn(BOOK_1);
        when(bookMapper.newBookDTOtoBook(eq(NEW_BOOK_DTO_1)))
                .thenReturn(BOOK_1);
        //
        Book actualBook = bookService.createBook(NEW_BOOK_DTO_1);
        assertNotNull(actualBook);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(Book.class, actualBook);
        assertEquals(BOOK_1, actualBook);
    }

    @Test
    @DisplayName(value = "createBook(NewBookDTO newBookDTO): throw RuntimeException")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createBook2() {
        when(bookRepository.save(BOOK_2))
                .thenThrow(new RuntimeException("Lost DB connection"));
        when(bookMapper.newBookDTOtoBook(eq(NEW_BOOK_DTO_2)))
                .thenReturn(BOOK_2);
        //
        assertThrows(RuntimeException.class, () -> bookService.createBook(NEW_BOOK_DTO_2));
    }


    @Test
    @DisplayName(value = "createBook(NewBookDTO newBookDTO): null")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createBook3() {
        when(bookRepository.save(BOOK_3))
                .thenReturn(null);
        when(bookMapper.newBookDTOtoBook(eq(NEW_BOOK_DTO_3)))
                .thenReturn(BOOK_3);
        //
        Book actualBook = bookService.createBook(NEW_BOOK_DTO_3);
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualBook);
    }

    @Test
    @DisplayName(value = "findBook(long id): Book")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findBook1() {
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(BOOK_1));
        //
        Book actualBook = bookService.findBook(BOOK_1.getId());
        assertNotNull(actualBook);
        assertInstanceOf(Book.class, actualBook);
        assertDoesNotThrow(() -> new RuntimeException());
        assertEquals(BOOK_1, actualBook);
    }

    @Test
    @DisplayName(value = "findBook(long id): null")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findBook2() {
        when(bookRepository.findById(BOOK_2.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        Book actualBook = bookService.findBook(BOOK_2.getId());
        assertNull(actualBook);
    }

    @Test
    @DisplayName(value = "editBook(BookDTO book): return Book")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void editBook1() {
        when(bookRepository.existsById(BOOK_1.getId()))
                .thenReturn(true);
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(BOOK_1));
        when(bookRepository.save(BOOK_1))
                .thenReturn(BOOK_1);
        when(bookMapper.bookDTOToBook(eq(BOOK_DTO_1)))
                .thenReturn(BOOK_1);
        //
        Book actualBook = bookService.editBook(BOOK_DTO_1);
        assertNotNull(actualBook);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(Book.class, actualBook);
        assertEquals(BOOK_1, actualBook);
    }

    @Test
    @DisplayName(value = "editBook(BookDTO book): throw RuntimeException")
    @Order(7)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void editBook2() {
        when(bookRepository.existsById(BOOK_2.getId()))
                .thenReturn(true);
        when(bookRepository.findById(BOOK_2.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        assertThrows(RuntimeException.class, () -> bookService.editBook(BOOK_DTO_2));
    }


    @Test
    @DisplayName(value = "editBook(BookDTO book): null")
    @Order(8)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void editBook3() {
        when(bookRepository.existsById(BOOK_3.getId()))
                .thenReturn(false);
        //
        Book actualBook = bookService.editBook(BOOK_DTO_3);
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualBook);
    }

    @Test
    @DisplayName(value = "deleteBook(long id): Book")
    @Order(9)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void deleteBook1() {
        when(bookRepository.existsById(BOOK_1.getId()))
                .thenReturn(true);
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(BOOK_1));
        //
        Book actualBook = bookService.deleteBook(BOOK_1.getId());
        assertNotNull(actualBook);
        assertInstanceOf(Book.class, actualBook);
        assertDoesNotThrow(() -> new RuntimeException());
        assertEquals(BOOK_1, actualBook);
    }

    @Test
    @DisplayName(value = "deleteBook(long id): null")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void deleteBook2() {
        when(bookRepository.existsById(BOOK_2.getId()))
                .thenReturn(false);
        //
        Book actualBook = bookService.deleteBook(BOOK_2.getId());
        assertNull(actualBook);
    }

    @Test
    @DisplayName(value = "getAllBooks(): Collection<Book>")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllBooks1() {
        when(bookRepository.findAll())
                .thenReturn(BOOKS_COLLECTION);
        //
        Collection<Book> actualBooks = bookService.getAllBooks();
        assertNotNull(actualBooks);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(BOOKS_COLLECTION, actualBooks);
    }

    @Test
    @DisplayName(value = "getAllBooks(String authorOrTitle): Collection<Book>")
    @Order(11)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllBooks2() {
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(BOOKS_COLLECTION);
        //
        Collection<Book> actualBooks = bookService.getAllBooks(BOOK_1.getAuthor());
        assertNotNull(actualBooks);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(BOOKS_COLLECTION, actualBooks);
    }
}