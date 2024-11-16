package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import lorgar.avrelian.javaconspectrus.services.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Hidden
public class SpringWebIntroductionController {
    private final CounterService counterService;

    public SpringWebIntroductionController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping
    public ModelAndView getHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
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
