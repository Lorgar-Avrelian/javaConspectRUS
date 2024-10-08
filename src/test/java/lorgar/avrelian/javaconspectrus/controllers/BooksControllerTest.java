package lorgar.avrelian.javaconspectrus.controllers;

import lorgar.avrelian.javaconspectrus.repository.BookCoverRepository;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.repository.ReaderRepository;
import lorgar.avrelian.javaconspectrus.services.implementations.BookCoverServiceImpl;
import lorgar.avrelian.javaconspectrus.services.implementations.BookServiceImplDB;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

@WebMvcTest(controllers = BooksController.class)
@ComponentScan(basePackages = {"lorgar.avrelian.javaconspectrus.services.implementations"}) // аннотация использована по
    // причине возникновения UnsatisfiedDependencyException и IllegalStateException
@DisplayName(value = "http://localhost:8080/books")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BooksController controller;
    @SpyBean
    private BookServiceImplDB bookServiceImplDB;
    @SpyBean
    private BookCoverServiceImpl bookCoverServiceImpl;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCoverRepository bookcoverRepository;
    // ----------------------------------------------------------------------
    // добавленные макеты в результате использования аннотации @ComponentScan
    @MockBean
    private ReaderRepository readerRepository;
    @MockBean
    private Random random;
    // ----------------------------------------------------------------------

    @Test
    @DisplayName(value = "POST http://localhost:8080/books")
    @Order(1)
    void createBook() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}")
    @Order(2)
    void readBook() {
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/books")
    @Order(3)
    void updateBook() {
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/books/{id}")
    @Order(4)
    void deleteBook() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books")
    @Order(5)
    void getAllBooks() {
    }

    @Test
    @DisplayName(value = "POST http://localhost:8080/books/{id}/cover")
    @Order(6)
    void uploadCover() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover/preview")
    @Order(7)
    void downloadCover() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover")
    @Order(8)
    void testDownloadCover() {
    }
}