package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(title = "Новая книга", description = "Сущность новой книги")
public class NewBookDTO {
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    private String authorFIO;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    private short year;

    public NewBookDTO() {
    }

    public NewBookDTO(String title, String authorFIO, short year) {
        this.title = title;
        this.authorFIO = authorFIO;
        this.year = year;
    }

    public String getAuthorFIO() {
        return authorFIO;
    }

    public void setAuthorFIO(String authorFIO) {
        this.authorFIO = authorFIO;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewBookDTO that = (NewBookDTO) o;
        return year == that.year && Objects.equals(title, that.title) && Objects.equals(authorFIO, that.authorFIO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, authorFIO, year);
    }

    @Override
    public String toString() {
        return "NewBookDTO{" +
                "title='" + title + '\'' +
                ", author='" + authorFIO + '\'' +
                ", year=" + year +
                '}';
    }
}
