package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ReaderService {
    Reader createReader(Reader reader);

    Reader findReader(long id);

    Reader editReader(Reader reader);

    Reader deleteReader(long id);

    Collection<Reader> getAllReaders();

    Collection<Reader> getAllReaders(String partOfNameSecondNameOrSurname);

    Collection<Reader> getReaderByName(String partOfName);

    Collection<Reader> getReaderBySecondName(String partOfSecondName);

    Collection<Reader> getReaderBySurname(String partOfSurname);

    Collection<Book> getReaderBooks(long id);
}
