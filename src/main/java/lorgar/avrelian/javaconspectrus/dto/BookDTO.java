package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(title = "Книга", description = "Сущность книги")
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

    public BookDTO() {
    }

    public BookDTO(long id, String title, String author, short year, ReaderNoBooksDTO reader) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.reader = reader;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ReaderNoBooksDTO getReader() {
        return reader;
    }

    public void setReader(ReaderNoBooksDTO reader) {
        this.reader = reader;
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
        BookDTO bookDTO = (BookDTO) o;
        return id == bookDTO.id && year == bookDTO.year && Objects.equals(title, bookDTO.title) && Objects.equals(author, bookDTO.author) && Objects.equals(reader, bookDTO.reader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, year, reader);
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", reader=" + reader +
                '}';
    }
}
