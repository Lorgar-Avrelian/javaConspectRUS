package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.services.WhetherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/whether")
@Tag(name = "Контроллер погоды", description = "Контроллер для получения прогнозов погоды")
public class WhetherController {
    private final WhetherService whetherService;

    public WhetherController(@Qualifier(value = "whetherServiceImpl") WhetherService whetherService) {
        this.whetherService = whetherService;
    }

    @GetMapping(path = "/city-info")                    // http://localhost:8080/whether/city-info
    @Operation(
            summary = "Гео",
            description = "Получить гео данные города",
            tags = "Погода",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = City.class))
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
    public ResponseEntity<List<City>> getCityInfo(@RequestParam(name = "city") @Parameter(description = "Название города", required = true, schema = @Schema(implementation = String.class)) String city) {
        List<City> cities = whetherService.getCityInfo(city);
        if (cities != null) {
            return ResponseEntity.ok(cities);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
