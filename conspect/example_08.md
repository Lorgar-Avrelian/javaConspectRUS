## Пример 1:

> [[_оглавление_]](../README.md/#92-работа-с-файлами)

> [[**9.2 Работа с файлами**]](/conspect/08.md/#92-работа-с-файлами)

- определение пути для сохранения файлов в файле _application.properties_:

```properties
# Application properties
spring.application.name=Java conspectus RUS
server.port=8080
# Specific parameter for RandomizeConfig class
rand.diapazon=1000
# Specific parameter for BookCoverService implementations
books.covers.dir.path=books/covers
# Spring Data JPA parameters for DB connection
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=library_user
spring.datasource.password=123
# Hibernate settings
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=update
# Settings of showing SQL-requests Spring Data JPA
spring.jpa.show-sql=true
```

- создание сущности файла для сохранения в базу данных:

```java

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
```

- создание репозитория для работы с базой данных:

```java
@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    Optional<BookCover> findByBookId(Long bookId);
}
```

- создание сервиса для работы с файлами:

```java

@Service
@Transactional
public class BookCoverServiceImpl implements BookCoverService {
    @Value("${books.covers.dir.path}")
    private String coversDir;
    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    public BookCoverServiceImpl(@Qualifier("bookServiceImplDB") BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }

    @Override
    public BookCover uploadCover(long bookId, MultipartFile file) {
        // Ищем указанную книгу по ID. Если не находим возвращаем null в контроллер
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return null;
        }
        // Создаём путь, по которому будет сохранён файл обложки
        // При этом получаем расширение файла при помощи private метода getExtension()
        String fileName = file.getOriginalFilename();
        Path filePath;
        if (fileName != null && !fileName.isEmpty()) {
            filePath = Path.of(coversDir, bookId + "." + getExtension(fileName));
        } else {
            return null;
        }
        // При помощи методов класса Files создаём директорию (если она не существует) и удаляем старый файл (если он существует)
        // Методы класса Files могут выбросить IOException, поэтому заключаем их в блок try-catch
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return null;
        }
        // Запускаем входные и выходные потоки данных для сохранения файла обложки на жёсткий диск
        // При этом создаём буферизированные потоки из входного и выходного, чтобы ускорить процесс обработки
        // Выходной поток данных запускаем при помощи метода класса Files newOutputStream()
        // Методы класса Files могут выбросить IOException, поэтому заключаем их в блок try-catch-with-resources
        try (
                InputStream inputStream = file.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        } catch (IOException e) {
            return null;
        }
        // Получаем сущность обложки из БД или создаём новую, если её в БД не существует при помощи private метода getBookCover()
        BookCover bookCover = getBookCover(bookId);
        // Вносим изменения в сущность обложки:
        // - заменяем ID обложки на ID книги
        bookCover.setId(bookId);
        // - заменяем путь до оригинального файла на жёстком диске, конвертируя filePath в строку
        bookCover.setFilePath(filePath.toString());
        // - заменяем размер файла: приводим его к значению int, поскольку метод getSize() возвращает значение типа long
        bookCover.setFileSize((int) file.getSize());
        // - заменяем тип файла, получая его строчное значение благодаря методу getContentType()
        bookCover.setMediaType(file.getContentType());
        // - заменяем превью, генерируя новое с помощью статического метода generatePreview()
        bookCover.setImagePreview(generatePreview(filePath));
        // - заменяем ссылку на книгу в БД
        bookCover.setBook(book);
        // - сохраняем обновлённую (или новую) сущность обложки в БД
        bookCoverRepository.save(bookCover);
        // Возвращаем превью обложки (в случае, если она корректно сохранилась)
        return getBookCover(bookId);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public BookCover getBookCover(long id) {
        return bookCoverRepository.findByBookId(id).orElse(new BookCover());
    }

    private byte[] generatePreview(Path path) {
        try (
                InputStream inputStream = Files.newInputStream(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage preview;
            if (height > width) {
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
                preview = new BufferedImage(100, height, bufferedImage.getType());
                width = 100;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 100);
                preview = new BufferedImage(width, 100, bufferedImage.getType());
                height = 100;
            }
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(preview, getExtension(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void downloadCover(long bookId, HttpServletResponse response) {
        BookCover bookCover = getBookCover(bookId);
        if (bookCover.getImagePreview() != null) {
            Path filePath = Path.of(bookCover.getFilePath());
            try (
                    InputStream inputStream = Files.newInputStream(filePath);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                    OutputStream outputStream = response.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
            ) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(bookCover.getMediaType());
                response.setContentLength(bookCover.getFileSize());
                bufferedInputStream.transferTo(bufferedOutputStream);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
```

- внедрение сервиса в контроллер и создание API для взаимодействия:

```java

@RestController
@RequestMapping(path = "/books")
@Tag(name = "Контроллер для книг", description = "Контроллер для работы с книгами")
public class BooksController {
    private final BookService bookService;
    private final BookCoverService bookCoverService;

    public BooksController(@Qualifier("bookServiceImplDB") BookService bookService, @Qualifier("bookCoverServiceImpl") BookCoverService bookCoverService) {
        this.bookService = bookService;
        this.bookCoverService = bookCoverService;
    }

    @PostMapping                    // http://localhost:8080/books      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Book.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method Not Allowed",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.createBook(book));
        } catch (RuntimeException e) {
            return ResponseEntity.status(405).build();
        }
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/books/1    R - read
    @Operation(
            summary = "Найти",
            description = "Найти информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Book.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Book> readBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id) {
        Book findedBook = bookService.findBook(id);
        if (findedBook != null) {
            return ResponseEntity.ok(findedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/books      U - update
    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Book.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book updatedBook = bookService.editBook(book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/books/1    D - delete
    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Book.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Book> deleteBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id) {
        Book deletedBook = bookService.deleteBook(id);
        if (deletedBook != null) {
            return ResponseEntity.ok(deletedBook);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список книг",
            description = "Вывести список всех доступных книг",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Book.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Book>> getAllBooks(@RequestParam(required = false) @Parameter(description = "Часть ФИО автора или названия книги", schema = @Schema(implementation = String.class)) String authorOrTitle) {
        Collection<Book> books;
        if (authorOrTitle == null) {
            books = bookService.getAllBooks();
        } else {
            books = bookService.getAllBooks(authorOrTitle);
        }
        if (!books.isEmpty()) {
            return ResponseEntity.status(200).body(books);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить",
            description = "Загрузить обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> uploadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id, @RequestParam @Parameter(description = "Изображение обложки книги", required = true, schema = @Schema(implementation = MultipartFile.class)) MultipartFile file) {
        if (file.getSize() > 1024 * 1000) {
            return ResponseEntity.badRequest().build();
        }
        BookCover bookCover = bookCoverService.uploadCover(id, file);
        if (bookCover != null) {
            return getCover(bookCover);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{id}/cover/preview")
    @Operation(
            summary = "Посмотреть превью",
            description = "Посмотреть превью обложки для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Byte.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id) {
        BookCover bookCover = bookCoverService.getBookCover(id);
        if (bookCover != null) {
            return getCover(bookCover);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private ResponseEntity<byte[]> getCover(BookCover bookCover) {
        byte[] preview = bookCover.getImagePreview();
        if (preview != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
            httpHeaders.setContentLength(preview.length);
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(preview);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(path = "/{id}/cover")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "Bad Gateway",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public void downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id, HttpServletResponse response) {
        bookCoverService.downloadCover(id, response);
    }
}
```

## Пример 2:

> [[_оглавление_]](../README.md/#92-работа-с-файлами)

> [[**9.2 Работа с файлами**]](/conspect/08.md/#92-работа-с-файлами)

- подключение зависимостей в файле _pom.xml_:

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

```xml

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

```xml

<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <scope>runtime</scope>
</dependency>
```

- определение директории для скриптов _Liquibase_ в файле _/resources/liquibase/changelog-master.yml_:

```yaml
databaseChangeLog:
  - include:
      file: liquibase/scripts/magazine.sql
```

- создание таблиц базы данных с помощью скрипта _Liquibase_ в файле _/resources/liquibase/scripts/magazine.sql_:

```sql
-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE users
(
    id         INTEGER      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NULL,
    last_name  VARCHAR(255) NULL,
    phone      VARCHAR(12)  NULL,
    role       INTEGER      NULL,
    image      TEXT         NULL,
    password   VARCHAR(100) NOT NULL,
    ads        INTEGER      NULL,
    comments   INTEGER      NULL,
    PRIMARY KEY (id)
);

-- changeset tokovenko:2
CREATE TABLE ad
(
    pk          INTEGER      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    author_id   INTEGER      NOT NULL,
    image       TEXT         NULL,
    price       INTEGER      NULL,
    title       VARCHAR(255) NULL,
    description TEXT         NULL,
    PRIMARY KEY (pk),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

-- changeset tokovenko:3
CREATE TABLE comment
(
    pk                INTEGER      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    author_id         INTEGER      NULL,
    author_image      TEXT         NULL,
    author_first_name VARCHAR(255) NULL,
    created_at        BIGINT       NULL,
    text              TEXT         NULL,
    ad_pk             INTEGER      NOT NULL,
    PRIMARY KEY (pk),
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (ad_pk) REFERENCES ad (pk)
);
```

- определение пути для сохранения файлов в файле _application.properties_:

```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:1988/magazine_db
spring.datasource.username=
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.open-in-view=true
spring.liquibase.change-log=classpath:liquibase/changelog-master.yml
spring.jackson.serialization.fail-on-empty-beans=false
ad.image.dir.path=adds
user.image.dir.path=users
#max file and request size
spring.http.multipart.max-file-size=10MB
spring.http.multipart.max-request-size=11MB
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

- создание сущности файла для сохранения в базу данных:

```java
/**
 * DAO <br>
 * <hr>
 * <br>
 * Ad { <br><br>
 * Integer pk <br>
 * id автора объявления <br><br>
 * {@link User} author <br>
 * автор объявления <br><br>
 * String image <br>
 * ссылка на картинку объявления <br><br>
 * int price <br>
 * цена объявления <br><br>
 * String title <br>
 * заголовок объявления <br><br>
 * String description <br>
 * описание объявления <br>
 * }
 */
@Entity
@Table(name = "ad")
public class Ad {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer pk;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
    private String image;
    private int price;
    private String title;
    private String description;

    public Ad() {
    }

    public Ad(Integer pk, User author, String image, int price, String title, String description) {
        this.pk = pk;
        this.author = author;
        this.image = image;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    @JsonBackReference
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ad ad = (Ad) o;
        return price == ad.price && Objects.equals(pk, ad.pk) && Objects.equals(author, ad.author) && Objects.equals(image, ad.image) && Objects.equals(title, ad.title) && Objects.equals(description, ad.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, author, image, price, title, description);
    }

    @Override
    public String toString() {
        return "Ad{" +
                "pk=" + pk +
                ", author=" + author +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
```

```java
/**
 * DAO <br>
 * <hr>
 * <br>
 * User { <br><br>
 * Integer id <br>
 * id пользователя <br><br>
 * String email <br>
 * логин пользователя <br><br>
 * String firstName <br>
 * имя пользователя <br><br>
 * String lastName <br>
 * фамилия пользователя <br><br>
 * String phone <br>
 * телефон пользователя <br><br>
 * {@link Role} role <br>
 * роль пользователя <br><br>
 * String image <br>
 * ссылка на аватар пользователя <br><br>
 * String password <br>
 * minLength: 8 <br>
 * maxLength: 16 <br>
 * текущий пароль <br>
 * }
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private String image;
    private String password;

    public User() {
    }

    public User(Integer id, String email, String firstName, String lastName, String phone, Role role, String image, String password) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(phone, user.phone) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", image='" + image + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```

- создание репозиториев для работы с базой данных:

```java
/**
 * {@link JpaRepository} for storage DAO {@link Ad} and operating with it. <br>
 * <br>
 * <hr>
 * <br>
 * {@link JpaRepository} для хранения DAO {@link Ad} и работы с ней. <br>
 * <br>
 *
 * @see JpaRepository
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
    List<Ad> findByAuthor(User user);

    Optional<Ad> findByPk(Integer pk);
}
```

```java
/**
 * {@link JpaRepository} for storage DAO {@link User} and operating with it. <br>
 * <br>
 * <hr>
 * <br>
 * {@link JpaRepository} для хранения DAO {@link User} и работы с ней. <br>
 * <br>
 *
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
```

- создание сервисов для работы с файлами:

```java
/**
 * An implementation of the service for processing ads and ad commits requests {@link AdsService}. <br>
 * <br>
 * <hr>
 * <br>
 * Реализация сервиса для обработки запросов объявлений и комментариев к объявлениям {@link AdsService}. <br>
 * <br>
 *
 * @see AdsService
 */
@Service
@Transactional
public class AdsServiceImpl implements AdsService {
    @Value("${ad.image.dir.path}")
    private String imageDir;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final CommentMapper commentMapper;
    private final ClockConfig clock;
    private static final Logger log = Logger.getLogger(AdsServiceImpl.class);

    public AdsServiceImpl(AdRepository adRepository, CommentRepository commentRepository, UserRepository userRepository, AdMapper adMapper, CommentMapper commentMapper, ClockConfig clock) {
        this.adRepository = adRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.commentMapper = commentMapper;
        this.clock = clock;
    }

    /**
     * A method of the service for getting DTO with list of all ads. <br>
     * Used repository method {@link AdRepository#findAll()} and {@link Mapper} method {@link AdMapper#adsListToAdsDto(List)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения DTO со списком всех объявлений. <br>
     * Использованы метод репозитория {@link AdRepository#findAll()} и {@link Mapper} метод {@link AdMapper#adsListToAdsDto(List)}. <br>
     * <br>
     *
     * @return {@link AdsDTO}
     * @see AdRepository#findAll()
     * @see AdMapper#adsListToAdsDto(List)
     */
    @Override
    public AdsDTO getAll() {
        List<Ad> adsList;
        try {
            adsList = adRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        if (!adsList.isEmpty()) {
            return adMapper.adsListToAdsDto(adsList);
        } else {
            return null;
        }
    }

    /**
     * A method of the service for processing new ad in form of DTO {@link CreateOrUpdateAdDTO} with params and {@link MultipartFile} with image. <br>
     * Used repository method {@link AdRepository#save(Object)} and {@link Mapper} methods {@link AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)} and {@link AdMapper#adToAdDto(Ad)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обработки нового объявления в форме DTO {@link CreateOrUpdateAdDTO} с параметрами и {@link MultipartFile} с изображением. <br>
     * Использованы метод репозитория {@link AdRepository#save(Object)} и {@link Mapper} методы {@link AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)} и {@link AdMapper#adToAdDto(Ad)}. <br>
     * <br>
     *
     * @param adDTO
     * @param image
     * @return {@link AdDTO}
     * @see AdRepository#save(Object)
     * @see AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)
     * @see AdMapper#adToAdDto(Ad)
     */
    @Override
    public AdDTO addAd(CreateOrUpdateAdDTO adDTO, MultipartFile image) {
        User user = getUser();
        if (user == null) {
            return null;
        }
        Ad ad = adMapper.createOrUpdateAdDtoToAd(adDTO);
        ad.setAuthor(user);
        ad = adRepository.save(ad);
        Path filePath = null;
        String fileName = "user_" + user.getId() + "_ad_" + ad.getPk() + "." + getExtension(image.getOriginalFilename());
        try {
            filePath = Path.of(imageDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        ad.setImage(fileName);
        ad = adRepository.save(ad);
        return adMapper.adToAdDto(ad);
    }

    /**
     * An auxiliary method for getting username (email) of authorized {@link User} from {@link SecurityContextHolder#getContext()}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения username (email) авторизованного {@link User} из {@link SecurityContextHolder#getContext()}. <br>
     * <br>
     *
     * @return {@link String} or null
     */
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * An auxiliary method for getting file extension from filename. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения расширения файла из его названия. <br>
     * <br>
     *
     * @return {@link String} or null
     */
    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * A method of the service for getting DTO {@link ExtendedAdDTO} with the ad having this id. <br>
     * Used repository method {@link AdRepository#findByPk(Integer)} and {@link Mapper} method {@link AdMapper#adToExtendedAd(Ad, User)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения DTO {@link ExtendedAdDTO} c объявлением, имеющим данный id. <br>
     * Использованы метод репозитория {@link AdRepository#findByPk(Integer)} и {@link Mapper} метод {@link AdMapper#adToExtendedAd(Ad, User)}. <br>
     * <br>
     *
     * @param pk
     * @return {@link ExtendedAdDTO}
     * @see AdRepository#findByPk(Integer)
     * @see AdMapper#adToExtendedAd(Ad, User)
     */
    @Override
    public ExtendedAdDTO getAd(Integer pk) {
        Ad ad = null;
        try {
            ad = adRepository.findByPk(pk).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        if (ad != null) {
            return adMapper.adToExtendedAd(ad, ad.getAuthor());
        } else {
            return null;
        }
    }

    /**
     * A method of the service for deleting ad having this id. <br>
     * Used repository method {@link AdRepository#delete(Object)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для удаления объявления, имеющего данный id. <br>
     * Использован метод репозитория {@link AdRepository#delete(Object)}. <br>
     * <br>
     *
     * @param pk
     * @return {@link Boolean}
     * @see AdRepository#delete(Object)
     */
    @Override
    public boolean deleteAd(Integer pk) {
        User user = getUser();
        Ad ad = getThatAd(pk);
        if (user != null && ad != null) {
            user = userCheck(user, ad);
        } else {
            return false;
        }
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't have such rights!");
        }
        try {
            adRepository.delete(ad);
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * A method of the service for updating params of the ad having this id by processing DTO {@link CreateOrUpdateAdDTO}. <br>
     * Used repository method {@link AdRepository#save(Object)} and {@link Mapper} methods {@link AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)} and {@link AdMapper#adToAdDto(Ad)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обновления параметров объявления, имеющего данный id, путём обработки DTO {@link CreateOrUpdateAdDTO}. <br>
     * Использованы метод репозитория {@link AdRepository#save(Object)} и {@link Mapper} методы {@link AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)} и {@link AdMapper#adToAdDto(Ad)}. <br>
     * <br>
     *
     * @param pk
     * @param createOrUpdateAdDTO
     * @return {@link AdDTO}
     * @see AdRepository#save(Object)
     * @see AdMapper#createOrUpdateAdDtoToAd(CreateOrUpdateAdDTO)
     */
    @Override
    public AdDTO updateAd(Integer pk, CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        User user = getUser();
        Ad adUpdate = adMapper.createOrUpdateAdDtoToAd(createOrUpdateAdDTO);
        Ad ad = getThatAd(pk);
        if (user != null && ad != null) {
            user = userCheck(user, ad);
        } else {
            return null;
        }
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't have such rights!");
        }
        ad.setTitle(adUpdate.getTitle());
        ad.setPrice(adUpdate.getPrice());
        ad.setDescription(adUpdate.getDescription());
        ad = adRepository.save(ad);
        return adMapper.adToAdDto(ad);
    }

    /**
     * A method of the service for getting list with all ads of authorized user in form DTO {@link AdsDTO}. <br>
     * Used repository method {@link AdRepository#findByAuthor(User)} and {@link Mapper} method {@link AdMapper#adsListToAdsDto(List)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения списка со всеми объявлениями авторизованного пользователя в форме DTO {@link AdsDTO}. <br>
     * Использованы метод репозитория {@link AdRepository#findByAuthor(User)} и {@link Mapper} метод {@link AdMapper#adsListToAdsDto(List)}. <br>
     * <br>
     *
     * @return {@link AdsDTO}
     * @see AdRepository#findByAuthor(User)
     * @see AdMapper#adsListToAdsDto(List)
     */
    @Override
    public AdsDTO getAllMine() {
        User user = getUser();
        if (user == null) {
            return null;
        }
        List<Ad> adsList;
        try {
            adsList = adRepository.findByAuthor(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return adMapper.adsListToAdsDto(adsList);
    }

    /**
     * A method of the service for updating image of the ad having this id by sending {@link MultipartFile}. <br>
     * Used repository method {@link AdRepository#save(Object)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обновления изображения объявления, имеющего данный id, путём отправки {@link MultipartFile}. <br>
     * Использован метод репозитория {@link  AdRepository#save(Object)}. <br>
     * <br>
     *
     * @param pk
     * @param image
     * @return {@link String}
     * @see AdRepository#save(Object)
     */
    @Override
    public String updateAdImage(Integer pk, MultipartFile image) throws FileNotFoundException {
        User user = getUser();
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't have such rights!");
        }
        Ad ad = getThatAd(pk);
        if (ad == null) {
            throw new FileNotFoundException("File does not exist!");
        }
        user = userCheck(user, ad);
        if (user == null) {
            return null;
        }
        Path filePath = null;
        String fileName = "user_" + ad.getAuthor().getId() + "_ad_" + ad.getPk() + "." + getExtension(image.getOriginalFilename());
        try {
            filePath = Path.of(imageDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        ad.setImage(fileName);
        adRepository.save(ad);
        return String.valueOf(filePath);
    }

    /**
     * A method of the service for getting list of comments to the ad having this id in form DTO {@link CommentsDTO}. <br>
     * Used repository method {@link CommentRepository#findByPk(Integer)} and {@link Mapper} method {@link CommentMapper#commentListToCommentsDto(List)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения списка комментариев к объявлению, имеющему данный id, в форме DTO {@link CommentsDTO}. <br>
     * Использованы метод репозитория {@link CommentRepository#findByPk(Integer)} и {@link Mapper} метод {@link CommentMapper#commentListToCommentsDto(List)}. <br>
     * <br>
     *
     * @param pk
     * @return {@link CommentsDTO}
     * @see CommentRepository#findByPk(Integer)
     * @see CommentMapper#commentListToCommentsDto(List)
     */
    @Override
    public CommentsDTO getAdComments(Integer pk) {
        Ad ad = getThatAd(pk);
        if (ad == null) {
            return new CommentsDTO();
        }
        List<Comment> commentsList = commentRepository.findByAd(ad);
        if (commentsList.isEmpty()) {
            return null;
        } else {
            return commentMapper.commentListToCommentsDto(commentsList);
        }
    }

    /**
     * A method of the service for processing new comment or updating old comment to the ad having this id in form DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Used repository method {@link CommentRepository#save(Object)} and {@link Mapper} method {@link CommentMapper#commentToCommentDto(Comment)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обработки нового комментария или обновления старого комментария к объявлению, имеющему данный id, в форме DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Использованы метод репозитория {@link CommentRepository#save(Object)} и {@link Mapper} метод {@link CommentMapper#commentToCommentDto(Comment)}. <br>
     * <br>
     *
     * @param pk
     * @param createOrUpdateCommentDTO
     * @return {@link CommentsDTO}
     * @see CommentRepository#save(Object)
     * @see CommentMapper#commentToCommentDto(Comment)
     */
    @Override
    public CommentDTO addComment(Integer pk, CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        User user = getUser();
        Ad ad = getThatAd(pk);
        if (user == null || ad == null) {
            return null;
        }
        Comment comment = new Comment(null, user, user.getImage(), user.getFirstName(), clock.clock().millis(), createOrUpdateCommentDTO.getText(), ad);
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * A method of the service for deleting comment having this comment id to the ad having this ad id. <br>
     * Used repository method {@link CommentRepository#delete(Object)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для удаления комментария, имеющего данный id комментария, к объявлению, имеющему данный id объявления. <br>
     * Использован метод репозитория {@link CommentRepository#delete(Object)}. <br>
     * <br>
     *
     * @param adPk
     * @param commentPk
     * @return {@link Boolean}
     * @see CommentRepository#delete(Object)
     */
    @Override
    public boolean deleteComment(Integer adPk, Integer commentPk) {
        User user = getUser();
        Ad ad = getThatAd(adPk);
        if (user == null || ad == null) {
            return false;
        }
        Comment comment = getComment(commentPk);
        if (comment != null) {
            user = userCheck(user, comment);
        } else {
            return false;
        }
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't have such rights!");
        }
        if (comment.getAd().equals(ad)) {
            commentRepository.delete(comment);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method of the service for updating comment having this comment id to the ad having this ad id by processing form DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Used repository methods {@link CommentRepository#findByPk(Integer)} and {@link CommentRepository#save(Object)} and {@link Mapper} method {@link CommentMapper#commentToCommentDto(Comment)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обновления комментария, имеющего данный id комментария, к объявлению, имеющему данный id объявления, путём обработки DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Использованы методы репозитория {@link CommentRepository#findByPk(Integer)} и {@link CommentRepository#save(Object)} и {@link Mapper} метод {@link CommentMapper#commentToCommentDto(Comment)}. <br>
     * <br>
     *
     * @param adPk
     * @param commentPk
     * @param createOrUpdateCommentDTO
     * @return {@link CommentDTO}
     * @see CommentRepository#findByPk(Integer)
     * @see CommentRepository#save(Object)
     * @see CommentMapper#commentToCommentDto(Comment)
     */
    @Override
    public CommentDTO updateComment(Integer adPk, Integer commentPk, CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        User user = getUser();
        Ad ad = getThatAd(adPk);
        if (user == null || ad == null) {
            return null;
        }
        Comment comment = getComment(commentPk);
        if (comment == null || !comment.getAd().equals(ad)) {
            return null;
        } else {
            user = userCheck(user, comment);
            if (user == null) {
                throw new UsernameNotFoundException("User doesn't have such rights!");
            }
            comment.setText(createOrUpdateCommentDTO.getText());
        }
        comment = commentRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }

    /**
     * An auxiliary method for getting entity of authorized {@link User} from DB. <br>
     * Used repository method {@link UserRepository#findByEmail(String)}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения сущности авторизованного {@link User} из БД. <br>
     * Использован методы репозитория {@link UserRepository#findByEmail(String)}. <br>
     * <br>
     *
     * @return {@link User} or null
     * @see UserRepository#findByEmail(String)
     */
    private User getUser() {
        User user = null;
        try {
            user = userRepository.findByEmail(getCurrentUsername()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        return user;
    }

    /**
     * An auxiliary method for getting entity {@link Ad} from DB by it's id. <br>
     * Used repository method {@link AdRepository#findByPk(Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения сущности объявления {@link Ad} из БД по его id. <br>
     * Использован методы репозитория {@link AdRepository#findByPk(Integer)}. <br>
     * <br>
     *
     * @return {@link Ad} or null
     * @see AdRepository#findByPk(Integer)
     */
    private Ad getThatAd(Integer pk) {
        Ad ad;
        try {
            ad = adRepository.findByPk(pk).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        }
        return ad;
    }

    /**
     * An auxiliary method for getting entity {@link Comment} from DB by it's id. <br>
     * Used repository method {@link CommentRepository#findByPk(Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения сущности объявления {@link Comment} из БД по его id. <br>
     * Использован методы репозитория {@link CommentRepository#findByPk(Integer)}. <br>
     * <br>
     *
     * @return {@link Comment} or null
     * @see CommentRepository#findByPk(Integer)
     */
    private Comment getComment(Integer pk) {
        Comment comment;
        try {
            comment = commentRepository.findByPk(pk).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
            return null;
        }
        return comment;
    }

    /**
     * An auxiliary method for check that authorized {@link User} is owner of {@link Ad} or has {@link Role#ADMIN}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для проверки, что авторизованный {@link User} является автором {@link Ad} или имеет {@link Role#ADMIN}. <br>
     * <br>
     *
     * @return {@link User} or null
     */
    private User userCheck(User user, Ad ad) {
        if (user.getRole().equals(Role.USER) && ad.getAuthor().equals(user) || user.getRole().equals(Role.ADMIN)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * An auxiliary method for check that authorized {@link User} is owner of {@link Comment} or has {@link Role#ADMIN}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для проверки, что авторизованный {@link User} является автором {@link Comment} или имеет {@link Role#ADMIN}. <br>
     * <br>
     *
     * @return {@link User} or null
     */
    private User userCheck(User user, Comment comment) {
        if (user.getRole().equals(Role.USER) && comment.getAuthor().equals(user) || user.getRole().equals(Role.ADMIN)) {
            return user;
        } else {
            return null;
        }
    }
}
```

```java
/**
 * An implementation of the service for processing ads and ad commits requests {@link UsersService}. <br>
 * <br>
 * <hr>
 * <br>
 * Реализация сервиса для обработки запросов объявлений и комментариев к объявлениям {@link UsersService}. <br>
 * <br>
 *
 * @see UsersService
 */
@Service
@Transactional
public class UsersServiceImpl implements UsersService {
    @Value("${user.image.dir.path}")
    private String imageDir;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private static final Logger log = Logger.getLogger(UsersServiceImpl.class);

    public UsersServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoderConfig passwordEncoderConfig) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoderConfig = passwordEncoderConfig;
    }

    /**
     * A method of the service for changing password of the user by processing DTO {@link NewPasswordDTO}. <br>
     * Used repository methods {@link UserRepository#findByEmail(String)} and {@link UserRepository#save(Object)} and {@link PasswordEncoder} method {@link PasswordEncoder#encode(CharSequence)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для изменения пароля пользователя путём обработки DTO {@link NewPasswordDTO}. <br>
     * Использованы методы репозитория {@link UserRepository#findByEmail(String)} и {@link UserRepository#save(Object)} и {@link PasswordEncoder} метод {@link PasswordEncoder#encode(CharSequence)}. <br>
     * <br>
     *
     * @param newPassword
     * @see UserRepository#findByEmail(String)
     * @see UserRepository#save(Object)
     * @see PasswordEncoder#encode(CharSequence)
     */
    @Override
    public void setPassword(NewPasswordDTO newPassword) {
        User user = null;
        try {
            user = userRepository.findByEmail(getCurrentUsername()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        if (user != null && passwordEncoderConfig.passwordEncoder().matches(newPassword.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoderConfig.passwordEncoder().encode(newPassword.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
    }

    /**
     * A method of the service for getting info about user in form of DTO {@link UserDTO}. <br>
     * Used repository method {@link UserRepository#findByEmail(String)} and {@link Mapper} method {@link UserMapper#userToUserDto(User)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения информации о пользователе в форме DTO {@link UserDTO}. <br>
     * Использованы метод репозитория {@link UserRepository#findByEmail(String)} и {@link Mapper} метод {@link UserMapper#userToUserDto(User)}. <br>
     * <br>
     *
     * @return {@link UserDTO}
     * @see UserRepository#findByEmail(String)
     * @see UserMapper#userToUserDto(User)
     */
    @Override
    public UserDTO getUser() {
        User user = null;
        try {
            user = userRepository.findByEmail(getCurrentUsername()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        UserDTO userDto;
        if (user != null) {
            userDto = userMapper.userToUserDto(user);
        } else {
            return null;
        }
        return userDto;
    }

    /**
     * A method of the service for updating info about user by processing DTO {@link UpdateUserDTO}. <br>
     * Used repository methods {@link UserRepository#findByEmail(String)} and {@link UserRepository#save(Object)}, {@link Mapper} methods {@link UserMapper#updateUserDtoToUser(UpdateUserDTO)} and {@link UserMapper#userToUpdateUserDto(User)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обновления информации о пользователе путём обработки DTO {@link UpdateUserDTO}. <br>
     * Использованы методы репозитория {@link UserRepository#findByEmail(String)} и {@link UserRepository#save(Object)}, {@link Mapper} методы {@link UserMapper#updateUserDtoToUser(UpdateUserDTO)} и {@link UserMapper#userToUpdateUserDto(User)}. <br>
     * <br>
     *
     * @param updateUserDto
     * @return {@link UpdateUserDTO}
     * @see UserRepository#findByEmail(String)
     * @see UserRepository#save(Object)
     * @see UserMapper#updateUserDtoToUser(UpdateUserDTO)
     * @see UserMapper#userToUpdateUserDto(User)
     */
    @Override
    public UpdateUserDTO updateUser(UpdateUserDTO updateUserDto) {
        User user = null;
        try {
            user = userRepository.findByEmail(getCurrentUsername()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            User userUpdate = userMapper.updateUserDtoToUser(updateUserDto);
            user.setFirstName(userUpdate.getFirstName());
            user.setLastName(userUpdate.getLastName());
            user.setPhone(userUpdate.getPhone());
            userRepository.save(user);
        } else {
            return null;
        }
        return userMapper.userToUpdateUserDto(user);
    }

    /**
     * A method of the service for updating user image by processing {@link MultipartFile}. <br>
     * Used repository methods {@link UserRepository#findByEmail(String)} and {@link UserRepository#save(Object)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для обновления изображения пользователе путём обработки {@link MultipartFile}. <br>
     * Использованы методы репозитория {@link UserRepository#findByEmail(String)} и {@link UserRepository#save(Object)}. <br>
     * <br>
     *
     * @param image
     * @see UserRepository#findByEmail(String)
     * @see UserRepository#save(Object)
     */
    @Override
    public void setImage(MultipartFile image) {
        User user = null;
        try {
            user = userRepository.findByEmail(getCurrentUsername()).get();
        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
        if (user != null) {
            Path filePath = null;
            String fileName = "user_" + user.getId() + "." + getExtension(image.getOriginalFilename());
            try {
                filePath = Path.of(imageDir, fileName);
                Files.createDirectories(filePath.getParent());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            try (
                    InputStream is = image.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
            ) {
                bis.transferTo(bos);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            user.setImage(fileName);
            userRepository.save(user);
        }
    }

    /**
     * An auxiliary method for getting username (email) of authorized {@link User} from {@link SecurityContextHolder#getContext()}. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения username (email) авторизованного {@link User} из {@link SecurityContextHolder#getContext()}. <br>
     * <br>
     *
     * @return {@link String} or null
     */
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * An auxiliary method for getting file extension from filename. <br>
     * <br>
     * <hr>
     * <br>
     * Вспомогательный метод для получения расширения файла из его названия. <br>
     * <br>
     *
     * @return {@link String} or null
     */
    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
```

```java
/**
 * An implementation of the service for processing ads and ad commits requests {@link ImageService}. <br>
 * <br>
 * <hr>
 * <br>
 * Реализация сервиса для обработки запросов объявлений и комментариев к объявлениям {@link ImageService}. <br>
 * <br>
 *
 * @see ImageService
 */
@Service
public class ImageServiceImpl implements ImageService {
    @Value("${user.image.dir.path}")
    private String userImageDir;
    @Value("${ad.image.dir.path}")
    private String addsImageDir;
    private static final Logger log = Logger.getLogger(ImageServiceImpl.class);

    /**
     * A method of the service for getting {@link Array} of {@link Byte} with image of user or ad. <br>
     * <br>
     * <hr>
     * <br>
     * Метод сервиса для получения массива {@link Array} байтов {@link Byte} с изображением пользователя или объявления. <br>
     * <br>
     *
     * @param imageName
     * @param response
     * @return {@link HttpStatus#OK} and {@link Array} of {@link Byte}
     * @throws IOException
     * @see ImageService#getImage(String, HttpServletResponse)
     */
    @Override
    public ResponseEntity<byte[]> getImage(String imageName, HttpServletResponse response) throws IOException {
        Path imagePath = Path.of(userImageDir, imageName);
        if (!Files.isExecutable(imagePath)) {
            imagePath = Path.of(addsImageDir, imageName);
        }
        if (!Files.isExecutable(imagePath)) {
            throw new IOException("File doesn't exist");
        }
        Tika tika = new Tika();
        String mimeType = tika.detect(imagePath);
        MediaType mediaType = MediaType.parseMediaType(mimeType);
        byte[] imageInBytes = new byte[(int) Files.size(imagePath)];
        try (
                InputStream is = Files.newInputStream(imagePath)
        ) {
            IOUtils.readFully(is, imageInBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setContentLength(imageInBytes.length);
        headers.setContentDispositionFormData(imageName, imageName);
        return ResponseEntity.ok().headers(headers).body(imageInBytes);
    }
}
```

- внедрение сервисов в контроллеры и создание API для взаимодействия:

```java
/**
 * A controller for ads and ad commits requests. <br>
 * <br>
 * <hr>
 * <br>
 * Контроллер для запросов объявлений и комментариев к объявлениям. <br>
 * <br>
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/ads")
public class AdsController {
    private final AdsServiceImpl adsService;
    private static final Logger log = Logger.getLogger(AdsController.class);

    public AdsController(AdsServiceImpl adsService) {
        this.adsService = adsService;
    }

    /**
     * A method of the controller for getting DTO with list of all ads. <br>
     * Used service method {@link AdsService#getAll()}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения DTO со списком всех объявлений. <br>
     * Использован метод сервиса {@link AdsService#getAll()}. <br>
     * <br>
     *
     * @return {@link HttpStatus#OK} and {@link AdsDTO}
     * @see AdsService#getAll()
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AdsDTO.class
                                    )
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<AdsDTO> getAll() {
        return ResponseEntity.ok().body(adsService.getAll());
    }

    /**
     * A method of the controller for posting new ad in form of DTO {@link CreateOrUpdateAdDTO} with params and {@link MultipartFile} with image. <br>
     * Used service method {@link AdsService#addAd(CreateOrUpdateAdDTO, MultipartFile)} and annotation {@link Valid} for validation of the parts of request. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для отправки нового объявления в форме DTO {@link CreateOrUpdateAdDTO} с параметрами и {@link MultipartFile} с изображением. <br>
     * Использованы метод сервиса {@link AdsService#addAd(CreateOrUpdateAdDTO, MultipartFile)} и аннотация {@link Valid} для валидации частей запроса. <br>
     * <br>
     *
     * @param ad
     * @param image
     * @return {@link HttpStatus#CREATED} and {@link AdDTO}
     * @see AdsService#addAd(CreateOrUpdateAdDTO, MultipartFile)
     * @see Valid
     */
    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AdDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AdDTO> postAd(@RequestPart("properties") @Valid CreateOrUpdateAdDTO ad, @RequestPart("image") MultipartFile image) {
        AdDTO newAd = adsService.addAd(ad, image);
        if (newAd != null) {
            return ResponseEntity.status(201).body(newAd);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * A method of the controller for getting DTO {@link ExtendedAdDTO} with the ad having this id. <br>
     * Used service method {@link AdsService#getAd(Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения DTO {@link ExtendedAdDTO} c объявлением, имеющим данный id. <br>
     * Использован метод сервиса {@link AdsService#getAd(Integer)}. <br>
     * <br>
     *
     * @param id
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link ExtendedAdDTO}
     * @see AdsService#getAd(Integer)
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ExtendedAdDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAdDTO> getAd(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer id) {
        ExtendedAdDTO extendedAdDTO = adsService.getAd(id);
        if (extendedAdDTO != null) {
            return ResponseEntity.status(200).body(extendedAdDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * A method of the controller for deleting ad having this id. <br>
     * Used service method {@link AdsService#deleteAd(Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для удаления объявления, имеющего данный id. <br>
     * Использован метод сервиса {@link AdsService#deleteAd(Integer)}. <br>
     * <br>
     *
     * @param id
     * @return {@link HttpStatus#NO_CONTENT} or {@link HttpStatus#NOT_FOUND}
     * @see AdsService#deleteAd(Integer)
     */
    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "No Content",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteAd(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer id) {
        boolean delete;
        try {
            delete = adsService.deleteAd(id);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(403).build();
        }
        if (delete) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * A method of the controller for updating params of the ad having this id by sending DTO {@link CreateOrUpdateAdDTO}. <br>
     * Used service method {@link AdsService#updateAd(Integer, CreateOrUpdateAdDTO)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для обновления параметров объявления, имеющего данный id, путём отправки DTO {@link CreateOrUpdateAdDTO}. <br>
     * Использован метод сервиса {@link AdsService#updateAd(Integer, CreateOrUpdateAdDTO)}. <br>
     * <br>
     *
     * @param id
     * @param createOrUpdateAdDTO
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link AdDTO}
     * @see AdsService#updateAd(Integer, CreateOrUpdateAdDTO)
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = CreateOrUpdateAdDTO.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AdDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping(path = "/{id}")
    public ResponseEntity<AdDTO> updateAd(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer id, @RequestBody CreateOrUpdateAdDTO createOrUpdateAdDTO) {
        AdDTO adDTO;
        try {
            adDTO = adsService.updateAd(id, createOrUpdateAdDTO);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(403).build();
        }
        if (adDTO == null) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.ok(adDTO);
        }
    }

    /**
     * A method of the controller for getting list with all ads of authorized user in form DTO {@link AdsDTO}. <br>
     * Used service method {@link AdsService#getAllMine()}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения списка со всеми объявлениями авторизованного пользователя в форме DTO {@link AdsDTO}. <br>
     * Использован метод сервиса {@link AdsService#getAllMine()}. <br>
     * <br>
     *
     * @return {@link HttpStatus#OK} and {@link AdsDTO}
     * @see AdsService#getAllMine()
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = AdsDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/me")
    public ResponseEntity<AdsDTO> getMe() {
        return ResponseEntity.ok(adsService.getAllMine());
    }

    /**
     * A method of the controller for updating image of the ad having this id by sending {@link MultipartFile}. <br>
     * Used service method {@link AdsService#updateAdImage(Integer, MultipartFile)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для обновления изображения объявления, имеющего данный id, путём отправки {@link MultipartFile}. <br>
     * Использован метод сервиса {@link AdsService#updateAdImage(Integer, MultipartFile)}. <br>
     * <br>
     *
     * @param id
     * @param image
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link String}
     * @see AdsService#updateAdImage(Integer, MultipartFile)
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления",
            parameters = @Parameter(
                    description = "ID объявления",
                    example = "1",
                    required = true,
                    name = "id"
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(
                                    implementation = MultipartFile.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = String.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping(path = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> patchAdImage(@PathVariable Integer id, @RequestBody MultipartFile image) {
        String imageUrl;
        try {
            imageUrl = adsService.updateAdImage(id, image);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(401).build();
        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).build();
        }
        if (imageUrl != null) {
            return ResponseEntity.ok(imageUrl);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * A method of the controller for getting list of comments to the ad having this id in form DTO {@link CommentsDTO}. <br>
     * Used service method {@link AdsService#getAdComments(Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения списка комментариев к объявлению, имеющему данный id, в форме DTO {@link CommentsDTO}. <br>
     * Использован метод сервиса {@link AdsService#getAdComments(Integer)}. <br>
     * <br>
     *
     * @param id
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link CommentsDTO}
     * @see AdsService#getAdComments(Integer)
     */
    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = CommentsDTO.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<CommentsDTO> getAdComments(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer id) {
        CommentsDTO commentsDTO = adsService.getAdComments(id);
        if (commentsDTO != null) {
            return ResponseEntity.ok(commentsDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * A method of the controller for posting new comment or updating old comment to the ad having this id in form DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Used service method {@link AdsService#addComment(Integer, CreateOrUpdateCommentDTO)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для отправки нового комментария или обновления старого комментария к объявлению, имеющему данный id, в форме DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Использован метод сервиса {@link AdsService#addComment(Integer, CreateOrUpdateCommentDTO)}. <br>
     * <br>
     *
     * @param id
     * @param createOrUpdateCommentDTO
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link CommentsDTO}
     * @see AdsService#addComment(Integer, CreateOrUpdateCommentDTO)
     */
    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = CreateOrUpdateCommentDTO.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = CommentDTO.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<CommentDTO> postAdComment(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer id, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        CommentDTO commentDTO = adsService.addComment(id, createOrUpdateCommentDTO);
        if (commentDTO != null) {
            return ResponseEntity.ok(commentDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * A method of the controller for deleting comment having this comment id to the ad having this ad id. <br>
     * Used service method {@link AdsService#deleteComment(Integer, Integer)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для удаления комментария, имеющего данный id комментария, к объявлению, имеющему данный id объявления. <br>
     * Использован метод сервиса {@link AdsService#deleteComment(Integer, Integer)}. <br>
     * <br>
     *
     * @param adId
     * @param commentId
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK}
     * @see AdsService#deleteComment(Integer, Integer)
     */
    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Void.class
                                            )
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @DeleteMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteAdComment(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer adId, @Parameter(description = "ID комментария", example = "1") @PathVariable Integer commentId) {
        boolean delete;
        try {
            delete = adsService.deleteComment(adId, commentId);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(403).build();
        }
        if (delete) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * A method of the controller for updating comment having this comment id to the ad having this ad id by sending form DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Used service method {@link AdsService#updateComment(Integer, Integer, CreateOrUpdateCommentDTO)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для обновления комментария, имеющего данный id комментария, к объявлению, имеющему данный id объявления, путём отправки DTO {@link CreateOrUpdateCommentDTO}. <br>
     * Использован метод сервиса {@link AdsService#updateComment(Integer, Integer, CreateOrUpdateCommentDTO)}. <br>
     * <br>
     *
     * @param adId
     * @param commentId
     * @param createOrUpdateCommentDTO
     * @return {@link HttpStatus#NOT_FOUND} or {@link HttpStatus#OK} and {@link CommentDTO}
     * @see AdsService#updateComment(Integer, Integer, CreateOrUpdateCommentDTO)
     */
    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = CreateOrUpdateCommentDTO.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = CommentDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateAdComment(@Parameter(description = "ID объявления", example = "1") @PathVariable Integer adId, @Parameter(description = "ID комментария", example = "1") @PathVariable Integer commentId, @RequestBody CreateOrUpdateCommentDTO createOrUpdateCommentDTO) {
        CommentDTO commentDTO;
        try {
            commentDTO = adsService.updateComment(adId, commentId, createOrUpdateCommentDTO);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(403).build();
        }
        if (commentDTO != null) {
            return ResponseEntity.ok(commentDTO);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}
```

```java
/**
 * A controller for requests info about users. <br>
 * <br>
 * <hr>
 * <br>
 * Контроллер для запросов информации о пользователях. <br>
 * <br>
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping(path = "/users")
public class UsersController {
    private final UsersServiceImpl usersService;

    public UsersController(UsersServiceImpl usersService) {
        this.usersService = usersService;
    }

    /**
     * A method of the controller for changing password of the user by sending DTO {@link NewPasswordDTO}. <br>
     * Used service method {@link UsersService#setPassword(NewPasswordDTO)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для изменения пароля пользователя путём отправки DTO {@link NewPasswordDTO}. <br>
     * Использован метод сервиса {@link UsersService#setPassword(NewPasswordDTO)}. <br>
     * <br>
     *
     * @param newPassword
     * @return {@link HttpStatus#OK}
     * @see UsersService#setPassword(NewPasswordDTO)
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление пароля",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = NewPasswordDTO.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PostMapping(path = "/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPasswordDTO newPassword) {
        try {
            usersService.setPassword(newPassword);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * A method of the controller for getting info about user in form of DTO {@link UserDTO}. <br>
     * Used service method {@link UsersService#getUser()}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения информации о пользователе в форме DTO {@link UserDTO}. <br>
     * Использован метод сервиса {@link UsersService#getUser()}. <br>
     * <br>
     *
     * @return {@link HttpStatus#OK} and {@link UserDTO}
     * @see UsersService#getUser()
     */
    @Operation(
            tags = "Пользователи",
            summary = "Получение информации об авторизованном пользователе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            schema = @Schema(
                                    implementation = Void.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/me")
    public ResponseEntity<UserDTO> me() {
        return ResponseEntity.ok().body(usersService.getUser());
    }

    /**
     * A method of the controller for updating info about user by sending DTO {@link UpdateUserDTO}. <br>
     * Used service method {@link UsersService#updateUser(UpdateUserDTO)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для обновления информации о пользователе путём отправки DTO {@link UpdateUserDTO}. <br>
     * Использован метод сервиса {@link UsersService#updateUser(UpdateUserDTO)}. <br>
     * <br>
     *
     * @param updateUser
     * @return {@link HttpStatus#OK} and {@link UpdateUserDTO}
     * @see UsersService#updateUser(UpdateUserDTO)
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление информации об авторизованном пользователе",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = UpdateUserDTO.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UpdateUserDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUserDTO> meUpdate(@RequestBody UpdateUserDTO updateUser) {
        return ResponseEntity.ok().body(usersService.updateUser(updateUser));
    }

    /**
     * A method of the controller for updating user image by sending {@link MultipartFile}. <br>
     * Used service method {@link UsersService#setImage(MultipartFile)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для обновления изображения пользователе путём отправки {@link MultipartFile}. <br>
     * Использован метод сервиса {@link UsersService#setImage(MultipartFile)}. <br>
     * <br>
     *
     * @param image
     * @return {@link HttpStatus#OK}
     * @see UsersService#setImage(MultipartFile)
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление аватара авторизованного пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(
                                    implementation = MultipartFile.class
                            )
                    ),
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PatchMapping(path = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> meImage(@RequestBody MultipartFile image) {
        usersService.setImage(image);
        return ResponseEntity.ok().build();
    }
}
```

```java
/**
 * A controller for users and ads image requests. <br>
 * <br>
 * <hr>
 * <br>
 * Контроллер для запросов изображений пользователей и объявлений. <br>
 * <br>
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class ImageController {
    private final ImageServiceImpl imageService;
    private static final Logger log = Logger.getLogger(ImageController.class);

    public ImageController(ImageServiceImpl imageService) {
        this.imageService = imageService;
    }

    /**
     * A method of the controller for getting {@link Array} of {@link Byte} with image of user or ad. <br>
     * Used service method {@link ImageService#getImage(String, HttpServletResponse)}. <br>
     * <br>
     * <hr>
     * <br>
     * Метод контроллера для получения массива {@link Array} байтов {@link Byte} с изображением пользователя или объявления. <br>
     * Использован метод сервиса {@link ImageService#getImage(String, HttpServletResponse)}. <br>
     * <br>
     *
     * @param imageName
     * @param response
     * @return {@link HttpStatus#UNAUTHORIZED} or {@link HttpStatus#OK} and {@link Array} of {@link Byte}
     * @see ImageService#getImage(String, HttpServletResponse)
     */
    @Operation(
            tags = "Изображения",
            summary = "Получение изображения пользователя или объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @GetMapping(path = "/{imageName}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<byte[]> downloadImage(@Parameter(description = "Ссылка на изображение в файловой системе", example = "user_1.png") @PathVariable String imageName, HttpServletResponse response) {
        try {
            return imageService.getImage(imageName, response);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).build();
        }
    }
}
```