package lorgar.avrelian.spring.exceptionHandlers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        System.out.printf("RuntimeException: %s%n", e.getMessage());
    }
}