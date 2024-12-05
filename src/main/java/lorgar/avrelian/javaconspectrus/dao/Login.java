package lorgar.avrelian.javaconspectrus.dao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lorgar.avrelian.javaconspectrus.models.Role;

@Schema(title = "Логин", description = "Сущность логина пользователя")
@Entity
@Table(name = "login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Schema(title = "ID", description = "ID пользователя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user", required = true, minLength = 3, maxLength = 30)
    @Column(name = "login", nullable = false, unique = true, length = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "{noop}123", required = true, minLength = 3)
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;
    @Schema(title = "Роль", description = "Роль пользователя в системе", defaultValue = "USER", required = true, minLength = 3, maxLength = 30)
    @Column(name = "role", nullable = false)
    private Role role;
}
