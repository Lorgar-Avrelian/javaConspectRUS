## Пример 1:

> [[_оглавление_]](../README.md/#36-spring-data)

> [[**3.6.1.3.1 Составление своих запросов**]](/conspect/3.md/#36131-составление-своих-запросов)

- добавление зависимостей в файл _pom.xml_:

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
    <version>42.7.4</version>
</dependency>
```

- добавление настроек подключения к БД в файл application.properties:

```properties
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

- настройка сохраняемой сущности:

```java

@Schema(title = "Книга", description = "Сущность книги")
@Entity
@Table(name = "book")
public class Book {
    @Schema(title = "ID", description = "ID книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    @Column(name = "title", nullable = false, unique = true, length = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    @Column(name = "author", nullable = false, length = 30)
    private String author;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    @Column(name = "year", nullable = false)
    private int year;
    @Schema(title = "ID читателя", description = "ID читателя", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    public Book() {
    }

    public Book(long id, String title, String author, int year, Reader reader) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.reader = reader;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @JsonBackReference
    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(reader, book.reader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, year, reader);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", reader=" + reader +
                '}';
    }
}
```

> **_ВНИМАНИЕ!_**  
> Необходимо не забыть отметить одно поле аннотацией `@Id`, а также добавить **_пустой_** конструктор!

- настройка репозитория _Spring Data JPA_:

```java

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String author, String title);
}
```

- внедрение репозитория в сервис и использование в сервисе методов репозитория:

```java

@Service
public class BookServiceImplDB implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImplDB(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) {
        book.setId(0);
        return bookRepository.save(book);
    }

    @Override
    public Book findBook(long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public Book editBook(Book book) {
        if (bookRepository.existsById(book.getId())) {
            return bookRepository.save(book);
        } else {
            return null;
        }
    }

    @Override
    public Book deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            Book book = findBook(id);
            bookRepository.deleteById(id);
            return book;
        } else {
            return null;
        }
    }

    @Override
    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Collection<Book> getAllBooks(String authorOrTitle) {
        return bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(authorOrTitle, authorOrTitle);
    }
}
```

- внедрение сервиса в контроллер и применение методов сервиса в контроллере:

```java

@RestController
@RequestMapping(path = "/books")
@Tag(name = "Контроллер для книг", description = "Контроллер для работы с книгами")
public class BooksController {
    private final BookService bookService;

    public BooksController(@Qualifier("bookServiceImplDB") BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping                    // http://localhost:8080/books      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о книге",
            tags = "Книги",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Book.class)
                            )
                    )
            }
    )
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @Operation(
            summary = "Найти",
            description = "Найти информацию о книге по ID",
            tags = "Книги",
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
    @GetMapping(path = "/{id}")     // http://localhost:8080/books/1    R - read
    public ResponseEntity<Book> readBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id) {
        Book findedBook = bookService.findBook(id);
        if (findedBook != null) {
            return ResponseEntity.ok(findedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о книге",
            tags = "Книги",
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
    @PutMapping                     // http://localhost:8080/books      U - update
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book updatedBook = bookService.editBook(book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о книге по ID",
            tags = "Книги",
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
    @DeleteMapping(path = "/{id}")  // http://localhost:8080/books/1    D - delete
    public ResponseEntity<Book> deleteBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id) {
        Book deletedBook = bookService.deleteBook(id);
        if (deletedBook != null) {
            return ResponseEntity.ok(deletedBook);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @Operation(
            summary = "Список",
            description = "Вывести список всех доступных книг",
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
    @GetMapping
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
}
```

## Пример 2:

> [[_оглавление_]](../README.md/#36-spring-data)

> [[**3.6.1.3.2 Пагинация**]](/conspect/3.md/#36132-пагинация)

- создание сущности для хранения данных в БД:

```java

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
```

- создание _JPA_-репозитория:

```java

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query(value = "SELECT category, SUM(amount) AS amount FROM expense GROUP BY category", nativeQuery = true)
    List<ExpensesByCategory> getExpenseByCategories();
}
```

- создание интерфейса для конвертации результата запроса, составленного с помощью аннотации `@Query`:

```java

@Schema(title = "Расходы по категориям", description = "Сущность расходов по категориям")
public interface ExpensesByCategory {
    @Schema(title = "Категория", description = "Название категории", defaultValue = "Транспорт")
    String getCategory();

    @Schema(title = "Количество", description = "Количество затрат", defaultValue = "1000", minimum = "0")
    float getAmount();
}
```

- создание сервиса:

```java

@Service
public interface ExpenseService {
    Collection<Expense> getAllExpenses();

    Expense getExpense(int id);

    Expense addExpense(Expense expense);

    Collection<ExpensesByCategory> getExpensesByCategories();

    Collection<Expense> getAllExpenses(int page, int size);
}
```

- создание реализации сервиса:

```java

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Collection<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Collection<Expense> getAllExpenses(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page resultPage = expenseRepository.findAll(pageRequest);
        return resultPage.getContent();
    }

    @Override
    public Expense getExpense(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Collection<ExpensesByCategory> getExpensesByCategories() {
        return expenseRepository.getExpenseByCategories();
    }
}
```

- создание контроллера:

```java

@RestController
@RequestMapping(path = "/expenses")
public class ExpensesController {
    private final ExpenseService expenseService;

    public ExpensesController(@Qualifier("expenseServiceImpl") ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @Operation(
            summary = "Все затраты",
            description = "Посмотреть все затраты",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Expense.class))
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
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Expense>> getExpenses(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        Collection<Expense> expenses;
        if (page == null && size == null) {
            expenses = expenseService.getAllExpenses();
        } else if (page != null && size != null && size > 0 && page > 0) {
            expenses = expenseService.getAllExpenses(page, size);
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть затрату по ID",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
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
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") int id) {
        Expense expense = expenseService.getExpense(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expense);
        }
    }

    @PostMapping
    @Operation(
            summary = "Внести",
            description = "Внести затрату",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
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
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense addedExpense = expenseService.addExpense(expense);
        if (addedExpense == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedExpense);
        }
    }

    @GetMapping(path = "/categories")
    @Operation(
            summary = "Затраты по категориям",
            description = "Посмотреть все затраты по категориям",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExpensesByCategory.class))
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
    public ResponseEntity<Collection<ExpensesByCategory>> getExpensesByCategories() {
        Collection<ExpensesByCategory> expenses = expenseService.getExpensesByCategories();
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }
}
```
