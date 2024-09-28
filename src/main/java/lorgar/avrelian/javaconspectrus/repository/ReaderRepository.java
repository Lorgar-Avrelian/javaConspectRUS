package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader, Long> {
    List<Reader> findByNameContainsIgnoreCase(String namePart);

    List<Reader> findBySecondNameContainsIgnoreCase(String secondNamePart);

    List<Reader> findBySurnameContainsIgnoreCase(String surnamePart);

    List<Reader> findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(String namePart, String secondNamePart, String surnamePart);
}
