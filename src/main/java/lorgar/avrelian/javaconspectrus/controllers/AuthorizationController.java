package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import lorgar.avrelian.javaconspectrus.dto.BasicAuthDTO;
import lorgar.avrelian.javaconspectrus.dto.LoginDTO;
import lorgar.avrelian.javaconspectrus.dto.RegisterDTO;
import lorgar.avrelian.javaconspectrus.models.Role;
import lorgar.avrelian.javaconspectrus.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Tag(name = "Контроллер авторизации", description = "Контроллер для авторизации пользователей")
@Log
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
        LoginDTO loginDTO = authorizationService.login(basicAuthDTO);
        if (loginDTO != null) {
            return ResponseEntity.ok().build();
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
        LoginDTO loginDTO = authorizationService.register(registerDTO);
        if (loginDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @PostMapping(path = "/logout")                   // http://localhost:8080/logout
    @Operation(
            summary = "Выйти",
            description = "Выйти из системы",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
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
    public ResponseEntity<?> logout(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails) {
        return null;
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
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<LoginDTO>> getUsers() {
        return null;
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
    public ResponseEntity<?> setRole(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                     @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id,
                                     @RequestParam @Parameter(description = "Новая роль пользователя", required = true, schema = @Schema(implementation = Role.class)) Role role) {
        return null;
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
    public ResponseEntity<?> setPassword(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                         @RequestParam @Parameter(description = "ID пользователя", schema = @Schema(implementation = Long.class)) long id,
                                         @RequestParam @Parameter(description = "Новая роль пользователя", required = true, schema = @Schema(implementation = String.class)) String password) {
        return null;
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
                                    schema = @Schema(implementation = Void.class)
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
    public ResponseEntity<?> delete(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                    @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id) {
        return null;
    }
}
