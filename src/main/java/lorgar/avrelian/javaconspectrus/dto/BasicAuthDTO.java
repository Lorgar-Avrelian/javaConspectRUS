package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Basic-аутентификация", description = "Логин и пароль пользователя")
@NoArgsConstructor
@Data
public class BasicAuthDTO {
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "123", required = true, minLength = 3)
    private String password;
}