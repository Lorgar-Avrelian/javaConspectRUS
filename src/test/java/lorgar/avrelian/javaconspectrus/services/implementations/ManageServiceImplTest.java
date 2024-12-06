package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.repository.ReaderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of ManageServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManageServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private Logger logger;
    @InjectMocks
    private ManageServiceImpl manageService;

    @Test
    @DisplayName(value = "giveBookToReader(long bookId, long readerId): return Reader")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void giveBookToReader1() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(BOOK_1));
        Reader reader = READER_1;
        reader.setBooks(new ArrayList<>(List.of(BOOK_1)));
        when(readerRepository.save(reader))
                .thenReturn(READER_1);
        //
        Reader actualReader = manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId());
        assertNotNull(actualReader);
        assertInstanceOf(Reader.class, actualReader);
        assertDoesNotThrow(() -> manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId()));
        assertEquals(READER_1, actualReader);
    }

    @Test
    @DisplayName(value = "giveBookToReader(long bookId, long readerId): return null")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void giveBookToReader2() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        Book book = BOOK_1;
        book.setReader(READER_4);
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(book));
        //
        Reader actualReader = manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "giveBookToReader(long bookId, long readerId): return null")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void giveBookToReader3() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        Reader actualReader = manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "giveBookToReader(long bookId, long readerId): return null")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void giveBookToReader4() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        Reader actualReader = manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "giveBookToReader(long bookId, long readerId): return null")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void giveBookToReader5() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(BOOK_1));
        //
        Reader actualReader = manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.giveBookToReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "findReaderBooks(long readerId): return Collection<Book>")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findReaderBooks1() {
        when(bookRepository.findByReaderId(READER_1.getId()))
                .thenReturn(BOOKS_COLLECTION);
        //
        Collection<Book> actualBooks = manageService.findReaderBooks(READER_1.getId());
        assertNotNull(actualBooks);
        assertDoesNotThrow(() -> manageService.findReaderBooks(READER_1.getId()));
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(BOOKS_COLLECTION, actualBooks);
    }

    @Test
    @DisplayName(value = "findReaderBooks(long readerId): return Collection<>")
    @Order(7)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findReaderBooks2() {
        when(bookRepository.findByReaderId(READER_1.getId()))
                .thenReturn(new ArrayList<>());
        //
        Collection<Book> actualBooks = manageService.findReaderBooks(READER_1.getId());
        assertNotNull(actualBooks);
        assertDoesNotThrow(() -> manageService.findReaderBooks(READER_1.getId()));
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(Collections.EMPTY_LIST, actualBooks);
    }

    @Test
    @DisplayName(value = "takeBookFromReader(long bookId, long readerId): return Reader")
    @Order(8)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void takeBookFromReader1() {
        Reader reader = READER_1;
        reader.setBooks(new ArrayList<>(List.of(BOOK_1)));
        Book book = BOOK_1;
        book.setReader(reader);
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(reader));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(book));
        when(readerRepository.save(eq(READER_1)))
                .thenReturn(READER_1);
        //
        Reader actualReader = manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId());
        assertNotNull(actualReader);
        assertInstanceOf(Reader.class, actualReader);
        assertDoesNotThrow(() -> manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId()));
        assertEquals(READER_1, actualReader);
    }

    @Test
    @DisplayName(value = "takeBookFromReader(long bookId, long readerId): return null")
    @Order(9)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void takeBookFromReader2() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        Book book = BOOK_1;
        book.setReader(READER_4);
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(book));
        //
        Reader actualReader = manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "takeBookFromReader(long bookId, long readerId): return null")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void takeBookFromReader3() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        Reader actualReader = manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "takeBookFromReader(long bookId, long readerId): return null")
    @Order(11)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void takeBookFromReader4() {
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(READER_1));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.ofNullable(null));
        //
        Reader actualReader = manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId()));
    }

    @Test
    @DisplayName(value = "takeBookFromReader(long bookId, long readerId): return null")
    @Order(12)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void takeBookFromReader5() {
        Reader reader = READER_1;
        reader.setBooks(new ArrayList<>(List.of(BOOK_1)));
        Book book = BOOK_1;
        book.setReader(reader);
        when(readerRepository.findById(READER_1.getId()))
                .thenReturn(Optional.of(reader));
        when(bookRepository.findById(BOOK_1.getId()))
                .thenReturn(Optional.of(book));
        //
        Reader actualReader = manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId());
        assertNull(actualReader);
        assertDoesNotThrow(() -> manageService.takeBookFromReader(BOOK_1.getId(), READER_1.getId()));
    }
}