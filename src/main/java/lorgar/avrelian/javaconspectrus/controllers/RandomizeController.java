package lorgar.avrelian.javaconspectrus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(path = "/random")
public class RandomizeController {
    private final Random random;

    public RandomizeController(Random random) {
        this.random = random;
    }

    @GetMapping(path = "/random/")
    public ResponseEntity<Integer> getRandomValue() {
        return ResponseEntity.status(200).body(random.nextInt());
    }
}
