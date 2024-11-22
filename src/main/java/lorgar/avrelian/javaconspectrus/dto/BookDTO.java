package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Книга", description = "Сущность книги")
@NoArgsConstructor
@Data
public class BookDTO {
    @Schema(title = "ID", description = "ID книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    private String author;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    private short year;
    @Schema(title = "ID читателя", description = "ID читателя", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    private ReaderNoBooksDTO reader;
}
