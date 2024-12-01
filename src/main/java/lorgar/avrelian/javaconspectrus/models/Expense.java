package lorgar.avrelian.javaconspectrus.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(title = "Расход", description = "Сущность затрат")
@Entity
@Table(name = "expense")
@Data
@NoArgsConstructor
public class Expense {
    @Schema(title = "ID", description = "ID траты", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private int id;
    @Schema(title = "Название", description = "Название траты", defaultValue = "Поездка на транспорте", required = true, minLength = 3, maxLength = 30)
    @Column(name = "title", nullable = false, length = 30)
    private String title;
    @Schema(title = "Дата", description = "Дата траты", defaultValue = "2024-10-09", required = true)
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Schema(title = "Категория", description = "Категория затрат", defaultValue = "Транспорт", required = true, minLength = 3, maxLength = 30)
    @Column(name = "category", nullable = false, length = 30)
    private String category;
    @Schema(title = "Количество", description = "Количество затрат", defaultValue = "1000", required = true, minimum = "0")
    @Column(name = "amount", nullable = false)
    private float amount;
}
