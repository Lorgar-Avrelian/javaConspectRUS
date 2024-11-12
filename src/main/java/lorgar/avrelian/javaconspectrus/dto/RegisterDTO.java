package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Регистрационные данные", description = "Сущность данных пользователя при регистрации")
@NoArgsConstructor
@Data
public class RegisterDTO {
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "{noop}123", required = true, minLength = 3)
    private String password;
    @Schema(title = "Подтверждение пароля", description = "Подтверждение пароля пользователя", defaultValue = "{noop}123", required = true, minLength = 3)
    private String passwordConfirmation;
}
