package lorgar.avrelian.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.spring.models.Reader;
import lorgar.avrelian.spring.services.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping(path = "/manage")
@Tag(name = "4 Приём/выдача книг", description = "Контроллер для приёма/выдачи книг")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class ManageController {
    private Logger logger = LoggerFactory.getLogger(ManageController.class);
    private final ManageService manageService;

    public ManageController(@Qualifier("manageServiceImpl") ManageService manageService) {
        this.manageService = manageService;
    }

    @PostMapping
    @Operation(
            summary = "Выдать",
            description = "Выдать книгу читателю",
            tags = "Приём/выдача",
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
    public ResponseEntity<Reader> giveBook(@RequestParam @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long bookId, @RequestParam @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long readerId) {
        logger.debug(MessageFormat.format("Give to reader {0} book {1}", readerId, bookId));
        Reader reader = manageService.giveBookToReader(bookId, readerId);
        if (reader == null) {
            logger.error("Give to reader %d book %d failed".formatted(readerId, bookId));
            return ResponseEntity.badRequest().build();
        } else {
            logger.info("Give to reader %d book %d success".formatted(readerId, bookId));
            return ResponseEntity.ok(reader);
        }
    }

    @GetMapping
    @Operation(
            summary = "Принять",
            description = "Принять книгу от читателя",
            tags = "Приём/выдача",
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
    public ResponseEntity<Reader> takeBook(@RequestParam(name = "book") @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long bookId, @RequestParam(name = "reader") @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long readerId) {
        logger.debug(MessageFormat.format("Take from reader {0} book {1}", readerId, bookId));
        Reader reader = manageService.takeBookFromReader(bookId, readerId);
        if (reader == null) {
            logger.error("Take from reader %d book %d failed".formatted(readerId, bookId));
            return ResponseEntity.badRequest().build();
        } else {
            logger.info("Take from reader %d book %d success".formatted(readerId, bookId));
            return ResponseEntity.ok(reader);
        }
    }
}