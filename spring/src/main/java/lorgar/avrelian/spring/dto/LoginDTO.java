package lorgar.avrelian.spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lorgar.avrelian.spring.models.Role;

@Schema(title = "Логин", description = "Сущность логина пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @Schema(title = "ID", description = "ID пользователя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Роль", description = "Роль пользователя в системе", defaultValue = "USER", required = true, minLength = 3, maxLength = 30)
    private Role role;
}
