## Пример 1:

> [[_оглавление_]](../README.md/#36-spring-data)

> [[**3.6.1.2.1 Связность сущностей**]](/conspect/3.md/#36121-связность-сущностей)

- аннотации `@OneToMany` и `@ManyToOne`:

```java

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, unique = true, length = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, unique = true, length = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, unique = true, length = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    @Column(name = "personal_number", nullable = false, unique = true)
    private int personalNumber;
    @Schema(title = "Книги", description = "Книги  читателя", defaultValue = "null", hidden = true)
    @OneToMany(mappedBy = "reader")
    private Collection<Book> books;

    public Reader() {
    }

    public Reader(long id, String name, String secondName, String surname, int personalNumber, Collection<Book> books) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.surname = surname;
        this.personalNumber = personalNumber;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

    @JsonManagedReference
    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && personalNumber == reader.personalNumber && Objects.equals(name, reader.name) && Objects.equals(secondName, reader.secondName) && Objects.equals(surname, reader.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secondName, surname, personalNumber);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surname='" + surname + '\'' +
                ", personalNumber=" + personalNumber +
                ", books=" + books +
                '}';
    }
}
```

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

- аннотация `@OneToOne`:

```java

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    Faculty faculty;

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", age=" + getAge() +
                '}';
    }
}
```

```java

@Entity
@Table(name = "avatar")
public class Avatar {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] data;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    public Avatar(Long id, String filePath, long fileSize, String mediaType, byte[] data) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }

    public Avatar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return Objects.equals(id, avatar.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", data=" + Arrays.toString(data) +
                ", student=" + student +
                '}';
    }
}
```

- аннотация `@ManyToMany`:

```java

@Entity
class Student {

    @Id
    Long id;

    @ManyToMany
    Set<Course> likedCourses;
    // дополнительные свойства
    // стандартные конструкторы, геттеры и сеттеры
}
```

```java

@Entity
class Course {

    @Id
    Long id;

    @ManyToMany(mappedBy = "likedCourses") // название соответствующего поля в классе Student
    Set<Student> likes;
    // дополнительные свойства
    // стандартные конструкторы, геттеры и сеттеры
}
```

## Пример 2:

> [[_оглавление_]](../README.md/#36-spring-data)

> [[**3.6.1.2.1 Связность сущностей**]](/conspect/3.md/#36121-связность-сущностей)

- аннотации `@JsonManagedReference` и `@JsonBackReference`:

```java

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, unique = true, length = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, unique = true, length = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, unique = true, length = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    @Column(name = "personal_number", nullable = false, unique = true)
    private int personalNumber;
    @Schema(title = "Книги", description = "Книги  читателя", defaultValue = "null", hidden = true)
    @OneToMany(mappedBy = "reader")
    private Collection<Book> books;

    public Reader() {
    }

    public Reader(long id, String name, String secondName, String surname, int personalNumber, Collection<Book> books) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.surname = surname;
        this.personalNumber = personalNumber;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

    @JsonManagedReference
    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && personalNumber == reader.personalNumber && Objects.equals(name, reader.name) && Objects.equals(secondName, reader.secondName) && Objects.equals(surname, reader.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secondName, surname, personalNumber);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surname='" + surname + '\'' +
                ", personalNumber=" + personalNumber +
                ", books=" + books +
                '}';
    }
}
```

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

- аннотация `@JsonIgnore`:

```java

import com.fasterxml.jackson.annotation.JsonIgnore;

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, unique = true, length = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, unique = true, length = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, unique = true, length = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    @Column(name = "personal_number", nullable = false, unique = true)
    private int personalNumber;
    @Schema(title = "Книги", description = "Книги  читателя", defaultValue = "null", hidden = true)
    @OneToMany(mappedBy = "reader")
    @JsonIgnore
    private Collection<Book> books;

    public Reader() {
    }

    public Reader(long id, String name, String secondName, String surname, int personalNumber, Collection<Book> books) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.surname = surname;
        this.personalNumber = personalNumber;
        this.books = books;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && personalNumber == reader.personalNumber && Objects.equals(name, reader.name) && Objects.equals(secondName, reader.secondName) && Objects.equals(surname, reader.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secondName, surname, personalNumber);
    }

    @Override
    public String toString() {
        return "Reader{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surname='" + surname + '\'' +
                ", personalNumber=" + personalNumber +
                ", books=" + books +
                '}';
    }
}
```

```java

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
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