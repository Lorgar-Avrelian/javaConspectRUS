package lorgar.avrelian.javaconspectrus.controllers;

import lorgar.avrelian.javaconspectrus.services.CounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/controller")
public class SpringWebIntroductionController {
    private final CounterService counterService;

    public SpringWebIntroductionController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Hello world!");
    }
    @GetMapping(path = "/counter")
    public ResponseEntity<String> getCounter() {
        return ResponseEntity.ok(String.valueOf(counterService.getCounter()));
    }
    @GetMapping("/counter/change")
    public ResponseEntity<Integer> changeCounter1(@RequestParam(name = "counter") int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }
    @GetMapping("/counter/change/{counter}")
    public ResponseEntity<Integer> changeCounter2(@PathVariable int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }
}
