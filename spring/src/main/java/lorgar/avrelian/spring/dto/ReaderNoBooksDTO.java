package lorgar.avrelian.spring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Читатель", description = "Сущность читателя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReaderNoBooksDTO {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    private int personalNumber;
}
