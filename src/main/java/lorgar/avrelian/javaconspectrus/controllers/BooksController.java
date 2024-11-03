package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lorgar.avrelian.javaconspectrus.dto.BookDTO;import lorgar.avrelian.javaconspectrus.dto.NewBookDTO;import lorgar.avrelian.javaconspectrus.mappers.BookMapper;import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@RequestMapping(path = "/books")
@Tag(name = "Контроллер для книг", description = "Контроллер для работы с книгами")
@CacheConfig(cacheNames = "book")
public class BooksController {
    private final BookService bookService;
    private final BookCoverService bookCoverService;
    private final BookMapper bookMapper;

    public BooksController(@Qualifier("bookServiceImplDB") BookService bookService, @Qualifier("bookCoverServiceImpl") BookCoverService bookCoverService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookCoverService = bookCoverService;
        this.bookMapper = bookMapper;
    }

    @PostMapping                    // http://localhost:8080/books      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method Not Allowed",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<BookDTO> createBook(@RequestBody NewBookDTO book) {
        try {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(bookService.createBook(book)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(405).build();
        }
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/books/1    R - read
    @Operation(
            summary = "Найти",
            description = "Найти информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @Cacheable(value = "book")
    public ResponseEntity<BookDTO> readBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Book findedBook = bookService.findBook(id);
        if (findedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(findedBook));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/books      U - update
    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @CachePut(value = "book", key = "#book.id", unless = "#result.body.id > 100")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO book) {
        Book updatedBook = bookService.editBook(book);
        if (updatedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(updatedBook));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/books/1    D - delete
    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @CacheEvict(value = "book")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Book deletedBook = bookService.deleteBook(id);
        if (deletedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(deletedBook));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список книг",
            description = "Вывести список всех доступных книг",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<BookDTO>> getAllBooks(@RequestParam(required = false) @Parameter(description = "Часть ФИО автора или названия книги", schema = @Schema(implementation = String.class), example = "Пушкин") String authorOrTitle) {
        Collection<Book> books;
        if (authorOrTitle == null) {
            books = bookService.getAllBooks();
        } else {
            books = bookService.getAllBooks(authorOrTitle);
        }
        if (!books.isEmpty()) {
            return ResponseEntity.status(200).body(bookMapper.booksListToBookDTOList(books));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить",
            description = "Загрузить обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> uploadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id, @RequestParam @Parameter(description = "Изображение обложки книги", required = true, schema = @Schema(implementation = MultipartFile.class)) MultipartFile file) {
        if (file.getSize() > 1024 * 1000) {
            return ResponseEntity.badRequest().build();
        }
        BookCover bookCover = bookCoverService.uploadCover(id, file);
        if (bookCover != null) {
            return getCover(bookCover);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{id}/cover/preview")
    @Operation(
            summary = "Посмотреть превью",
            description = "Посмотреть превью обложки для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Byte.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        BookCover bookCover = bookCoverService.getBookCover(id);
        return getCover(bookCover);
    }

    private ResponseEntity<byte[]> getCover(BookCover bookCover) {
        byte[] preview = bookCover.getImagePreview();
        if (preview != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
            httpHeaders.setContentLength(preview.length);
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(preview);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(path = "/{id}/cover")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "Bad Gateway",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public void downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id, HttpServletResponse response) {
        bookCoverService.downloadCover(id, response);
    }
}
