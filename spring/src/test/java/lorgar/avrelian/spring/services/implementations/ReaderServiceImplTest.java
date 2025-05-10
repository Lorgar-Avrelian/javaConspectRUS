package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.dto.BookDTO;
import lorgar.avrelian.spring.dto.ReaderNoBooksDTO;
import lorgar.avrelian.spring.mappers.BookMapper;
import lorgar.avrelian.spring.mappers.ReaderMapper;
import lorgar.avrelian.spring.models.Reader;
import lorgar.avrelian.spring.repository.BookRepository;
import lorgar.avrelian.spring.repository.ReaderRepository;
import lorgar.avrelian.spring.services.ManageService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.spring.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of ReaderServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReaderServiceImplTest {
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private ReaderMapper readerMapper;
    @Mock
    private ManageService manageService;
    @InjectMocks
    private ReaderServiceImpl readerService;

    @Test
    @DisplayName(value = "createReader(NewReaderDTO newReader): return ReaderNoBooksDTO")
    @Order(1)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createReader1() {
        when(readerMapper.newReaderDTOtoReader(eq(NEW_READER_DTO_1)))
                .thenReturn(READER_1);
        when(readerMapper.readerToNoBooksDTO(eq(READER_1)))
                .thenReturn(READER_NO_BOOKS_DTO_1);
        when(readerRepository.save(eq(READER_1)))
                .thenReturn(READER_1);
        //
        ReaderNoBooksDTO actualReader = readerService.createReader(NEW_READER_DTO_1);
        assertNotNull(actualReader);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(ReaderNoBooksDTO.class, actualReader);
        assertEquals(READER_NO_BOOKS_DTO_1, actualReader);
    }

    @Test
    @DisplayName(value = "createReader(NewReaderDTO newReader): throw RuntimeException")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createReader2() {
        when(readerMapper.newReaderDTOtoReader(eq(NEW_READER_DTO_2)))
                .thenReturn(READER_2);
        when(readerMapper.readerToNoBooksDTO(eq(READER_2)))
                .thenThrow(new RuntimeException());
        when(readerRepository.save(eq(READER_2)))
                .thenReturn(READER_2);
        //
        assertThrows(RuntimeException.class, () -> readerService.createReader(NEW_READER_DTO_2));
    }

    @Test
    @DisplayName(value = "createReader(NewReaderDTO newReader): return null")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void createReader3() {
        when(readerMapper.newReaderDTOtoReader(eq(NEW_READER_DTO_3)))
                .thenReturn(READER_3);
        when(readerRepository.save(eq(READER_3)))
                .thenReturn(null);
        //
        ReaderNoBooksDTO actualReader = readerService.createReader(NEW_READER_DTO_3);
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualReader);
    }

    @Test
    @DisplayName(value = "findReader(long id): return ReaderNoBooksDTO")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findReader1() {
        when(readerRepository.findById(eq(READER_1.getId())))
                .thenReturn(Optional.of(READER_1));
        when(manageService.findReaderBooks(READER_1.getId()))
                .thenReturn(BOOKS_COLLECTION);
        when(readerMapper.readerToNoBooksDTO(eq(READER_1)))
                .thenReturn(READER_NO_BOOKS_DTO_1);
        //
        ReaderNoBooksDTO actualReader = readerService.findReader(READER_1.getId());
        assertNotNull(actualReader);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(ReaderNoBooksDTO.class, actualReader);
        assertEquals(READER_NO_BOOKS_DTO_1, actualReader);
    }

    @Test
    @DisplayName(value = "findReader(long id): throw RuntimeException")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findReader2() {
        when(readerRepository.findById(eq(READER_2.getId())))
                .thenReturn(Optional.of(READER_2));
        when(manageService.findReaderBooks(READER_2.getId()))
                .thenReturn(BOOKS_COLLECTION);
        when(readerMapper.readerToNoBooksDTO(eq(READER_1)))
                .thenThrow(new RuntimeException());
        //
        assertThrows(RuntimeException.class, () -> readerService.findReader(READER_2.getId()));
    }

    @Test
    @DisplayName(value = "findReader(long id): return null")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findReader3() {
        when(readerRepository.findById(eq(READER_3.getId())))
                .thenReturn(Optional.ofNullable(null));
        //
        ReaderNoBooksDTO actualReader = readerService.findReader(READER_3.getId());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualReader);
    }

    @Test
    @DisplayName(value = "findDBReader(long id): return Reader")
    @Order(7)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findDBReader1() {
        when(readerRepository.findById(eq(READER_1.getId())))
                .thenReturn(Optional.of(READER_1));
        when(manageService.findReaderBooks(READER_1.getId()))
                .thenReturn(BOOKS_COLLECTION);
        //
        Reader actualReader = readerService.findDBReader(READER_1.getId());
        assertNotNull(actualReader);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(Reader.class, actualReader);
        assertEquals(READER_1, actualReader);
    }

    @Test
    @DisplayName(value = "findDBReader(long id): return null")
    @Order(8)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void findDBReader2() {
        when(readerRepository.findById(eq(READER_3.getId())))
                .thenReturn(Optional.ofNullable(null));
        //
        Reader actualReader = readerService.findDBReader(READER_3.getId());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualReader);
    }

    @Test
    @DisplayName(value = "editReader(Reader reader): return ReaderNoBooksDTO")
    @Order(9)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void editReader1() {
        when(readerRepository.existsById(READER_1.getId()))
                .thenReturn(true);
        when(readerRepository.save(eq(READER_1)))
                .thenReturn(READER_1);
        when(manageService.findReaderBooks(READER_1.getId()))
                .thenReturn(BOOKS_COLLECTION);
        when(readerMapper.readerToNoBooksDTO(eq(READER_1)))
                .thenReturn(READER_NO_BOOKS_DTO_1);
        //
        ReaderNoBooksDTO actualReader = readerService.editReader(READER_1);
        assertNotNull(actualReader);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(ReaderNoBooksDTO.class, actualReader);
        assertEquals(READER_NO_BOOKS_DTO_1, actualReader);
    }

    @Test
    @DisplayName(value = "editReader(Reader reader): return null")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void editReader2() {
        when(readerRepository.existsById(eq(READER_2.getId())))
                .thenReturn(false);
        //
        ReaderNoBooksDTO actualReader = readerService.editReader(READER_2);
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualReader);
    }

    @Test
    @DisplayName(value = "deleteReader(long id): return ReaderNoBooksDTO")
    @Order(11)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void deleteReader1() {
        when(readerRepository.existsById(READER_1.getId()))
                .thenReturn(true);
        when(readerRepository.findById(eq(READER_1.getId())))
                .thenReturn(Optional.of(READER_1));
        when(manageService.findReaderBooks(READER_1.getId()))
                .thenReturn(BOOKS_COLLECTION);
        when(readerMapper.readerToNoBooksDTO(eq(READER_1)))
                .thenReturn(READER_NO_BOOKS_DTO_1);
        //
        ReaderNoBooksDTO actualReader = readerService.deleteReader(READER_1.getId());
        assertNotNull(actualReader);
        assertDoesNotThrow(() -> new RuntimeException());
        assertInstanceOf(ReaderNoBooksDTO.class, actualReader);
        assertEquals(READER_NO_BOOKS_DTO_1, actualReader);
    }

    @Test
    @DisplayName(value = "deleteReader(long id): return null")
    @Order(12)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void deleteReader2() {
        when(readerRepository.existsById(eq(READER_2.getId())))
                .thenReturn(false);
        //
        ReaderNoBooksDTO actualReader = readerService.deleteReader(READER_2.getId());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNull(actualReader);
    }

    @Test
    @DisplayName(value = "getAllReaders(): return Collection<ReaderNoBooksDTO>")
    @Order(13)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllReaders1() {
        when(readerRepository.findAll())
                .thenReturn(READERS_COLLECTION);
        when(readerMapper.readersToNoBooksDTOs(eq(READERS_COLLECTION)))
                .thenReturn(READERS_NO_BOOKS_LIST);
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getAllReaders();
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_NO_BOOKS_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getAllReaders(): return Collection<>")
    @Order(14)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllReaders2() {
        when(readerRepository.findAll())
                .thenReturn(new ArrayList<>());
        when(readerMapper.readersToNoBooksDTOs(anyCollection()))
                .thenReturn(new ArrayList<>());
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getAllReaders();
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getAllReaders(): return Collection<ReaderNoBooksDTO>")
    @Order(15)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllDBReaders1() {
        when(readerRepository.findAll())
                .thenReturn(READERS_COLLECTION);
        //
        Collection<Reader> actualReaders = readerService.getAllDBReaders();
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_COLLECTION, actualReaders);
    }

    @Test
    @DisplayName(value = "getAllReaders(): return Collection<>")
    @Order(16)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllDBReaders2() {
        when(readerRepository.findAll())
                .thenReturn(new ArrayList<>());
        //
        Collection<Reader> actualReaders = readerService.getAllDBReaders();
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getAllReaders(String partOfNameSecondNameOrSurname): return Collection<ReaderNoBooksDTO>")
    @Order(17)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllReaders3() {
        when(readerRepository.findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(anyString(), anyString(), anyString()))
                .thenReturn(READERS_COLLECTION);
        when(readerMapper.readersToNoBooksDTOs(eq(READERS_COLLECTION)))
                .thenReturn(READERS_NO_BOOKS_LIST);
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getAllReaders(READER_1.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_NO_BOOKS_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getAllReaders(String partOfNameSecondNameOrSurname): return Collection<>")
    @Order(18)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllReaders4() {
        when(readerRepository.findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(anyString(), anyString(), anyString()))
                .thenReturn(new ArrayList<>());
        when(readerMapper.readersToNoBooksDTOs(anyCollection()))
                .thenReturn(new ArrayList<>());
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getAllReaders(READER_2.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderByName(String partOfName): return Collection<ReaderNoBooksDTO>")
    @Order(19)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderByName1() {
        when(readerRepository.findByNameContainsIgnoreCase(anyString()))
                .thenReturn(READERS_COLLECTION);
        when(readerMapper.readersToNoBooksDTOs(eq(READERS_COLLECTION)))
                .thenReturn(READERS_NO_BOOKS_LIST);
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderByName(READER_1.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_NO_BOOKS_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderByName(String partOfName): return Collection<>")
    @Order(20)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderByName2() {
        when(readerRepository.findByNameContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        when(readerMapper.readersToNoBooksDTOs(anyCollection()))
                .thenReturn(new ArrayList<>());
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderByName(READER_2.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderBySecondName(String partOfSecondName): return Collection<ReaderNoBooksDTO>")
    @Order(21)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBySecondName1() {
        when(readerRepository.findBySecondNameContainsIgnoreCase(anyString()))
                .thenReturn(READERS_COLLECTION);
        when(readerMapper.readersToNoBooksDTOs(eq(READERS_COLLECTION)))
                .thenReturn(READERS_NO_BOOKS_LIST);
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderBySecondName(READER_1.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_NO_BOOKS_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderBySecondName(String partOfSecondName): return Collection<>")
    @Order(22)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBySecondName2() {
        when(readerRepository.findBySecondNameContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        when(readerMapper.readersToNoBooksDTOs(anyCollection()))
                .thenReturn(new ArrayList<>());
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderBySecondName(READER_2.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderBySurname(String partOfSurname): return Collection<ReaderNoBooksDTO>")
    @Order(23)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBySurname1() {
        when(readerRepository.findBySurnameContainsIgnoreCase(anyString()))
                .thenReturn(READERS_COLLECTION);
        when(readerMapper.readersToNoBooksDTOs(eq(READERS_COLLECTION)))
                .thenReturn(READERS_NO_BOOKS_LIST);
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderBySurname(READER_1.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(READERS_NO_BOOKS_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderBySurname(String partOfSurname): return Collection<>")
    @Order(24)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBySurname2() {
        when(readerRepository.findBySurnameContainsIgnoreCase(anyString()))
                .thenReturn(new ArrayList<>());
        when(readerMapper.readersToNoBooksDTOs(anyCollection()))
                .thenReturn(new ArrayList<>());
        //
        Collection<ReaderNoBooksDTO> actualReaders = readerService.getReaderBySurname(READER_2.getName());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualReaders);
        assertInstanceOf(Collection.class, actualReaders);
        assertIterableEquals(Collections.EMPTY_LIST, actualReaders);
    }

    @Test
    @DisplayName(value = "getReaderBooks(long id): return Collection<BookDTO>")
    @Order(25)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBooks1() {
        when(manageService.findReaderBooks(eq(READER_1.getId())))
                .thenReturn(BOOKS_COLLECTION);
        when(bookMapper.booksListToBookDTOList(BOOKS_COLLECTION))
                .thenReturn(BOOKS_DTO_COLLECTION);
        //
        Collection<BookDTO> actualBooks = readerService.getReaderBooks(READER_1.getId());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualBooks);
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(BOOKS_DTO_COLLECTION, actualBooks);
    }

    @Test
    @DisplayName(value = "getReaderBooks(long id): return Collection<>")
    @Order(26)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getReaderBooks2() {
        when(manageService.findReaderBooks(eq(READER_2.getId())))
                .thenReturn(new ArrayList<>());
        when(bookMapper.booksListToBookDTOList(Collections.EMPTY_LIST))
                .thenReturn(new ArrayList<>());
        //
        Collection<BookDTO> actualBooks = readerService.getReaderBooks(READER_2.getId());
        assertDoesNotThrow(() -> new RuntimeException());
        assertNotNull(actualBooks);
        assertInstanceOf(Collection.class, actualBooks);
        assertIterableEquals(Collections.EMPTY_LIST, actualBooks);
    }
}