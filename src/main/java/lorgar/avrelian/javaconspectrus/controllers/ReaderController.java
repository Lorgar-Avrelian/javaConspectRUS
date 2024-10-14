package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import lorgar.avrelian.javaconspectrus.services.ReaderService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/readers")
@Tag(name = "Контроллер для читателей", description = "Контроллер для работы с читателями")
public class ReaderController {
    private final ReaderService readerService;

    public ReaderController(@Qualifier("readerServiceImpl") ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping                    // http://localhost:8080/readers      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о читателе",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
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
    public ResponseEntity<Reader> createReader(@RequestBody Reader reader) {
        try {
            return ResponseEntity.ok(readerService.createReader(reader));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/readers/1    R - read
    @Operation(
            summary = "Найти",
            description = "Найти информацию о читателе по ID",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
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
    public ResponseEntity<Reader> readReader(@PathVariable @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Reader findedReader = readerService.findReader(id);
        if (findedReader != null) {
            return ResponseEntity.ok(findedReader);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/readers      U - update
    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о читателе",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
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
    public ResponseEntity<Reader> updateReader(@RequestBody Reader reader) {
        Reader updatedReader = readerService.editReader(reader);
        if (updatedReader != null) {
            return ResponseEntity.ok(updatedReader);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/readers/1    D - delete
    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о читателе по ID",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
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
    public ResponseEntity<Reader> deleteBook(@PathVariable @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Reader deletedReader = readerService.deleteReader(id);
        if (deletedReader != null) {
            return ResponseEntity.ok(deletedReader);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список читателей",
            description = "Вывести список всех зарегистрированных читателей",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Reader.class))
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
    public ResponseEntity<Collection<Reader>> getAllReaders(@RequestParam(required = false) @Parameter(description = "Часть ФИО читателя", schema = @Schema(implementation = String.class), example = "Иван") String partOfNameSecondNameOrSurname) {
        Collection<Reader> readers;
        if (partOfNameSecondNameOrSurname == null) {
            readers = readerService.getAllReaders();
        } else {
            readers = readerService.getAllReaders(partOfNameSecondNameOrSurname);
        }
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/name")
    @Operation(
            summary = "Список по имени",
            description = "Вывести список всех зарегистрированных читателей с указанным именем",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Reader.class))
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
    public ResponseEntity<Collection<Reader>> getReadersByName(@RequestParam @Parameter(description = "Часть имени читателя", schema = @Schema(implementation = String.class), example = "Иван") String partOfName) {
        Collection<Reader> readers = readerService.getReaderByName(partOfName);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/secondName")
    @Operation(
            summary = "Список по отчеству",
            description = "Вывести список всех зарегистрированных читателей с указанным отчеством",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Reader.class))
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
    public ResponseEntity<Collection<Reader>> getReadersBySecondName(@RequestParam @Parameter(description = "Часть отчества читателя", schema = @Schema(implementation = String.class), example = "Иванович") String partOfSecondName) {
        Collection<Reader> readers = readerService.getReaderBySecondName(partOfSecondName);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/surname")
    @Operation(
            summary = "Список по фамилии",
            description = "Вывести список всех зарегистрированных читателей с указанной фамилией",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Reader.class))
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
    public ResponseEntity<Collection<Reader>> getReadersBySurname(@RequestParam @Parameter(description = "Часть фамилии читателя", schema = @Schema(implementation = String.class), example = "Иванов") String partOfSurname) {
        Collection<Reader> readers = readerService.getReaderBySurname(partOfSurname);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/books")
    @Operation(
            summary = "Список книг",
            description = "Вывести список всех книг данного читателя",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
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
    public ResponseEntity<Collection<Book>> getReaderBooks(@RequestParam @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Collection<Book> books = readerService.getReaderBooks(id);
        if (!books.isEmpty()) {
            return ResponseEntity.status(200).body(books);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
