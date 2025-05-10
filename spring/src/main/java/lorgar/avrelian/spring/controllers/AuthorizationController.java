package lorgar.avrelian.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import lorgar.avrelian.spring.dao.Login;
import lorgar.avrelian.spring.dto.BasicAuthDTO;
import lorgar.avrelian.spring.dto.LoginDTO;
import lorgar.avrelian.spring.dto.RegisterDTO;
import lorgar.avrelian.spring.models.Role;
import lorgar.avrelian.spring.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "1 Авторизация", description = "Контроллер для авторизации пользователей")
@Log
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(@Qualifier(value = "authorizationServiceImpl") AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping(path = "/login")                    // http://localhost:8080/login
    @Operation(
            summary = "Войти",
            description = "Пройти авторизацию",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody @Parameter(description = "Логин и пароль", required = true, schema = @Schema(implementation = BasicAuthDTO.class)) BasicAuthDTO basicAuthDTO) {
        Login login = authorizationService.login(basicAuthDTO);
        if (login != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(login.getLogin(), login.getPassword());
            return ResponseEntity.ok().headers(headers).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(path = "/register")                 // http://localhost:8080/register
    @Operation(
            summary = "Регистрация",
            description = "Пройти регистрацию",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<?> register(@RequestBody @Parameter(description = "Логин, пароль и подтверждение пароля", required = true, schema = @Schema(implementation = RegisterDTO.class)) RegisterDTO registerDTO) {
        Login login = authorizationService.register(registerDTO);
        if (login != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(login.getLogin(), login.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping(path = "/users")                     // http://localhost:8080/users
    @Operation(
            summary = "Пользователи",
            description = "Список зарегистрированных пользователей",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = LoginDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<Collection<LoginDTO>> getUsers(@RequestParam(required = false) @Parameter(description = "Номер страницы", schema = @Schema(implementation = Integer.class)) Integer page,
                                                         @RequestParam(required = false) @Parameter(description = "Количество пользователей на странице", schema = @Schema(implementation = Integer.class)) Integer size) {
        Collection<LoginDTO> users;
        if (page == null && size == null) {
            users = authorizationService.getAllUsers();
        } else if (page != null && size != null && size > 0 && page > 0) {
            users = authorizationService.getAllUsers(page, size);
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (users.isEmpty()) {
            return ResponseEntity.status(403).build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PatchMapping(path = "/set-role")                // http://localhost:8080/set-role
    @Operation(
            summary = "Права",
            description = "Смена прав для зарегистрированного пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<LoginDTO> setRole(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                            @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id,
                                            @RequestParam @Parameter(description = "Новая роль пользователя", required = true, schema = @Schema(implementation = Role.class)) Role role) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.setRole(userDetails, id, role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PatchMapping(path = "/set-password")            // http://localhost:8080/set-password
    @Operation(
            summary = "Пароль",
            description = "Смена пароля для зарегистрированного пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<LoginDTO> setPassword(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                                @RequestParam @Parameter(description = "ID пользователя", schema = @Schema(implementation = Long.class)) long id,
                                                @RequestParam @Parameter(description = "Новый пароль пользователя", required = true, schema = @Schema(implementation = String.class)) String password) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.setPassword(userDetails, id, password);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping(path = "/delete")                   // http://localhost:8080/delete
    @Operation(
            summary = "Удалить",
            description = "Удалить пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<LoginDTO> delete(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                           @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.delete(userDetails, id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(path = "/csrf")                       // http://localhost:8080/csrf
    @Operation(
            summary = "CSRF-токен",
            description = "Получить CSRF-токен",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = CsrfToken.class)
                            )
                    )
            }
    )
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }
}