package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.javaconspectrus.services.CounterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/counter")
@Tag(name = "7 Счётчик", description = "Контроллер для работы со счётчиком")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class CounterController {
    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping
    @Operation(
            summary = "Получить",
            description = "Получить значение счётчика",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
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
    public ResponseEntity<String> getCounter() {
        return ResponseEntity.ok(String.valueOf(counterService.getCounter()));
    }

    @GetMapping("/change")
    @Operation(
            summary = "Изменить - вариант 1",
            description = "Изменить значение счётчика - вариант 1",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
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
    public ResponseEntity<Integer> changeCounter1(@RequestParam(name = "counter") int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }

    @GetMapping("/change/{counter}")
    @Operation(
            summary = "Изменить - вариант 2",
            description = "Изменить значение счётчика - вариант 2",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
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
    public ResponseEntity<Integer> changeCounter2(@PathVariable int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }
}
