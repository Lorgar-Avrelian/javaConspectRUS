package lorgar.avrelian.javaconspectrus.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, length = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, length = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, length = 30)
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
