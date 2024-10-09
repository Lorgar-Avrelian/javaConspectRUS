package lorgar.avrelian.javaconspectrus.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Schema(title = "Расход", description = "Сущность затрат")
@Entity
@Table(name = "expense")
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

    public Expense() {
    }

    public Expense(int id, String title, LocalDate date, String category, float amount) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return id == expense.id && Float.compare(amount, expense.amount) == 0 && Objects.equals(title, expense.title) && Objects.equals(date, expense.date) && Objects.equals(category, expense.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date, category, amount);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                '}';
    }
}
