package lorgar.avrelian.javaconspectrus.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Schema(title = "Обложка книги", description = "Сущность обложки для книг")
@Entity
@Table(name = "book_cover")
public class BookCover {
    @Schema(title = "ID", description = "ID обложки книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Путь", description = "Путь сохранения обложки книги", required = true)
    @Column(name = "file_path", nullable = false, unique = true)
    private String filePath;
    @Schema(title = "Размер", description = "Размер сохранённой обложки книги", required = true)
    @Column(name = "file_size", nullable = false)
    private int fileSize;
    @Schema(title = "Тип", description = "Тип сохранённой обложки книги", required = true)
    @Column(name = "media_type", nullable = false)
    private String mediaType;
    @Lob
    @Schema(title = "Превью", description = "Превью обложки книги", required = true)
    @Column(name = "image_preview", nullable = false)
    private byte[] imagePreview;
    @Schema(title = "ID книги", description = "ID книги", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public BookCover() {
    }

    public BookCover(long id, String filePath, int fileSize, String mediaType, byte[] imagePreview, Book book) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.imagePreview = imagePreview;
        this.book = book;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getImagePreview() {
        return imagePreview;
    }

    public void setImagePreview(byte[] imagePreview) {
        this.imagePreview = imagePreview;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCover bookCover = (BookCover) o;
        return id == bookCover.id && fileSize == bookCover.fileSize && Objects.equals(filePath, bookCover.filePath) && Objects.equals(mediaType, bookCover.mediaType) && Objects.deepEquals(imagePreview, bookCover.imagePreview) && Objects.equals(book, bookCover.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, filePath, fileSize, mediaType, Arrays.hashCode(imagePreview), book);
    }

    @Override
    public String toString() {
        return "BookCover{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", imagePreview=" + Arrays.toString(imagePreview) +
                ", book=" + book +
                '}';
    }
}
