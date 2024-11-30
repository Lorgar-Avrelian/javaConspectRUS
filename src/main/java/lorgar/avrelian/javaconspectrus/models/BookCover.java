package lorgar.avrelian.javaconspectrus.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Обложка книги", description = "Сущность обложки для книг")
@Entity
@Table(name = "book_cover")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
