package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import lorgar.avrelian.javaconspectrus.dto.RegisterDTO;
import lorgar.avrelian.javaconspectrus.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Контроллер безопасности", description = "Контроллер для работы с настройками безопасности")
@Log
public class SecurityController {
    private final JpaUserDetailsService userDetailsService;

    public SecurityController(@Qualifier(value = "jpaUserDetailsServiceImpl") JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @PostMapping(path = "/register")
    public ResponseEntity<Void> register(@RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok().build();
    }
}
