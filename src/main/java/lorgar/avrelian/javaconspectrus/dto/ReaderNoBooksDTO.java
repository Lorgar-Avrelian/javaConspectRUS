package lorgar.avrelian.javaconspectrus.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(title = "Читатель", description = "Сущность читателя")
public class ReaderNoBooksDTO {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    private int personalNumber;

    public ReaderNoBooksDTO() {
    }

    public ReaderNoBooksDTO(long id, String name, String secondName, String surname, int personalNumber) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.surname = surname;
        this.personalNumber = personalNumber;
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

    public int getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(int personalNumber) {
        this.personalNumber = personalNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReaderNoBooksDTO that = (ReaderNoBooksDTO) o;
        return id == that.id && personalNumber == that.personalNumber && Objects.equals(name, that.name) && Objects.equals(secondName, that.secondName) && Objects.equals(surname, that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, secondName, surname, personalNumber);
    }

    @Override
    public String toString() {
        return "ReaderNoBooksDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", surname='" + surname + '\'' +
                ", personalNumber=" + personalNumber +
                '}';
    }
}
