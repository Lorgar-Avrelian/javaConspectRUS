package lorgar.avrelian.javaconspectrus.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, length = 12)
    @EqualsAndHashCode.Exclude
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, length = 16)
    @EqualsAndHashCode.Exclude
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, length = 30)
    @EqualsAndHashCode.Exclude
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    @Column(name = "personal_number", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int personalNumber;
    @Schema(title = "Книги", description = "Книги  читателя", defaultValue = "null", hidden = true)
    @OneToMany(mappedBy = "reader")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Book> books;
}
