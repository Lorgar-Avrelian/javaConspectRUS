package lorgar.avrelian.spring.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Расходы по категориям", description = "Сущность расходов по категориям")
public interface ExpensesByCategory {
    @Schema(title = "Категория", description = "Название категории", defaultValue = "Транспорт")
    String getCategory();
    @Schema(title = "Количество", description = "Количество затрат", defaultValue = "1000", minimum = "0")
    float getAmount();
}
