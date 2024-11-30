package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewReaderDTO;
import lorgar.avrelian.javaconspectrus.dto.ReaderNoBooksDTO;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ReaderService {
    ReaderNoBooksDTO createReader(NewReaderDTO newReader);

    ReaderNoBooksDTO findReader(long id);

    Reader findDBReader(long id);

    ReaderNoBooksDTO editReader(Reader reader);

    ReaderNoBooksDTO deleteReader(long id);

    Collection<ReaderNoBooksDTO> getAllReaders();

    Collection<Reader> getAllDBReaders();

    Collection<ReaderNoBooksDTO> getAllReaders(String partOfNameSecondNameOrSurname);

    Collection<ReaderNoBooksDTO> getReaderByName(String partOfName);

    Collection<ReaderNoBooksDTO> getReaderBySecondName(String partOfSecondName);

    Collection<ReaderNoBooksDTO> getReaderBySurname(String partOfSurname);

    Collection<BookDTO> getReaderBooks(long id);
}
