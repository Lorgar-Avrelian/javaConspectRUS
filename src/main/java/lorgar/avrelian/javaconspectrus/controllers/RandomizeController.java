package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.javaconspectrus.services.RandomService;
import lorgar.avrelian.javaconspectrus.services.implementations.RandomServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/random")
@Tag(name = "8 Рандом", description = "Контроллер для получения случайных чисел")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class RandomizeController {
    private final RandomService randomService;

    public RandomizeController(RandomServiceImpl randomService) {
        this.randomService = randomService;
    }

    @GetMapping
    public ResponseEntity<Integer> getRandomValue() {
        return ResponseEntity.status(200).body(randomService.getRandomNextInt());
    }
}