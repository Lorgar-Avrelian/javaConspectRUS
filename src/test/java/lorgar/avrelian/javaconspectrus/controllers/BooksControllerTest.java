package lorgar.avrelian.javaconspectrus.controllers;

import lorgar.avrelian.javaconspectrus.mappers.BookMapper;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import lorgar.avrelian.javaconspectrus.repository.BookCoverRepository;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import lorgar.avrelian.javaconspectrus.services.implementations.BookCoverServiceImpl;
import lorgar.avrelian.javaconspectrus.services.implementations.BookServiceImplDB;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BooksController.class, properties = "books.covers.dir.path=src/test/resources/testImages/result")
@DisplayName(value = "http://localhost:8080/books")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BooksController controller;
    @SpyBean
    private BookService bookService;
    @SpyBean
    private BookCoverService bookCoverService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCoverRepository bookcoverRepository;
    private final String sourceImageDir = "src/test/resources/testImages/result";

    @TestConfiguration
    public static class BooksControllerTestConfiguration {
        @Autowired
        private BookMapper bookMapper;
        @Bean(name = "bookServiceImplDB")
        public BookService bookService(BookRepository bookRepository) {
            return new BookServiceImplDB(bookRepository, bookMapper);
        }

        @Bean(name = "bookCoverServiceImpl")
        public BookCoverService bookCoverService(BookService bookService, BookCoverRepository bookcoverRepository) {
            return new BookCoverServiceImpl(bookService, bookcoverRepository);
        }
    }

    @Test
    @DisplayName(value = "POST http://localhost:8080/books")
    @Order(1)
    void createBook() throws Exception {
        // 1. Подготовительный этап
        // 1.1. получение сущности из констант
        Book expectedBook = TEST_BOOK_1;
        // 1.2. получение JSON-объекта из сущности при помощи статического метода toJsonObject() (в конце класса)
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        // 1.3. приведение значений сущности в соответствии с логикой сервиса
        expectedBook.setId(0);
        // 1.4. изменение поведения репозитория при вызове метода save() и получении на вход изменённой в соответствии
        //      с логикой сервиса сущности
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        //
        // 2. Проведение теста
        // 2.1. формирование запроса
        mockMvc.perform(
                       // 2.1.1. определение типа и адреса запроса
                       MockMvcRequestBuilders.post("/books")
                                             // 2.1.2. добавление контента запроса
                                             .content(expectedBookJson.toString())
                                             // 2.1.3. добавление типа контента запроса
                                             .contentType(MediaType.APPLICATION_JSON)
                                             // 2.1.3. добавление типа контента ответа
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               // 2.2. проверка ответа
               // 2.2.1. проверка статуса ответа
               .andExpect(MockMvcResultMatchers.status().isOk())
               // 2.2.2. проверка содержимого ответа
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook.setId(1);
        expectedBook = TEST_BOOK_2;
        expectedBookJson = toJsonObject(expectedBook);
        expectedBook.setId(0);
        when(bookRepository.save(eq(expectedBook))).thenThrow(new RuntimeException());
        mockMvc.perform(
                       MockMvcRequestBuilders.post("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        expectedBook.setId(2);
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}")
    @Order(2)
    void readBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.findById(eq(expectedBook.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_3;
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(null);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/books")
    @Order(3)
    void updateBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_4;
        expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/books/{id}")
    @Order(4)
    void deleteBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_5;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books")
    @Order(5)
    void getAllBooks() throws Exception {
        List<Book> expectedBookCollection = new ArrayList<>(List.of(TEST_BOOK_1, TEST_BOOK_2, TEST_BOOK_3, TEST_BOOK_4, TEST_BOOK_5));
        when(bookRepository.findAll()).thenReturn(expectedBookCollection);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(TEST_BOOK_2.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(TEST_BOOK_2.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value(TEST_BOOK_2.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(TEST_BOOK_2.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(TEST_BOOK_3.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value(TEST_BOOK_3.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value(TEST_BOOK_3.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].year").value(TEST_BOOK_3.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].year").value(TEST_BOOK_4.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].id").value(TEST_BOOK_5.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].title").value(TEST_BOOK_5.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].author").value(TEST_BOOK_5.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].year").value(TEST_BOOK_5.getYear()));
        List<Book> bookCollection1 = new ArrayList<>(List.of(TEST_BOOK_1));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_1.getAuthor()), eq(TEST_BOOK_1.getAuthor()))).thenReturn(bookCollection1);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_1.getAuthor())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()));
        List<Book> bookCollection2 = new ArrayList<>(List.of(TEST_BOOK_4));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_4.getTitle()), eq(TEST_BOOK_4.getTitle()))).thenReturn(bookCollection2);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_4.getTitle())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_4.getYear()));
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(anyString(), anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=random string")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test // файл передаётся с помощью аннотации @RequestParam
    @DisplayName(value = "POST http://localhost:8080/books/{id}/cover")
    @Order(6)
    void uploadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Задание поведения репозитория родительского сервиса (bookRepository) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        // 2. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                                                "1.jpg",                   // оригинальное название файла
                                                                MediaType.IMAGE_JPEG_VALUE,// тип файла
                                                                inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        // 7. Создание массива байт из загруженного изображения и сравнение его с оригиналом
        byte[] outputImage = Files.readAllBytes(Path.of(sourceImageDir + "/1.jpg"));
        Assertions.assertArrayEquals(inputImage, outputImage);
        //
        //                          Проверка запроса при отсутствующем родительском объекте
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookService.findBook(eq(TEST_BOOK_2.getId()))).thenReturn(null);
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                              sourceImageDir + "/2.jpg",
                              inputImage.length,
                              MediaType.IMAGE_JPEG.toString(),
                              generatePreview(TEST_BOOK_IMAGE_PATH_2),
                              TEST_BOOK_2);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "2.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/2.jpg")));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_3.getId()))).thenReturn(Optional.of(TEST_BOOK_3));
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_3);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(null);
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "3.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_3.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/3.jpg")));
        // 8. Удаление ненужной директории с результатами теста, после его завершения
        FileUtils.deleteDirectory(new File(sourceImageDir));
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover/preview")
    @Order(7)
    void downloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(new BookCover()));
        // 2. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_3.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover")
    @Order(8)
    void testDownloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        TEST_BOOK_IMAGE_PATH_1.toString(),
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1)));
        //
        //                          Проверка запроса при отсутствующем файле
        // 1. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                                        TEST_BOOK_IMAGE_PATH_2 + "random",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_2),
                                        TEST_BOOK_2);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadGateway())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 2. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 3. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    private static JSONObject toJsonObject(Book book) throws JSONException {
        JSONObject bookJson = new JSONObject();
        bookJson.put("id", book.getId());
        bookJson.put("title", book.getTitle());
        bookJson.put("author", book.getAuthor());
        bookJson.put("year", book.getYear());
        bookJson.put("reader", book.getReader());
        return bookJson;
    }

    private static byte[] generatePreview(Path path) {
        try (
                InputStream inputStream = Files.newInputStream(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage preview;
            if (height > width) {
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
                preview = new BufferedImage(100, height, bufferedImage.getType());
                width = 100;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 100);
                preview = new BufferedImage(width, 100, bufferedImage.getType());
                height = 100;
            }
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(preview, getExtension(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}