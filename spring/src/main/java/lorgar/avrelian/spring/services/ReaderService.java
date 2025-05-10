package lorgar.avrelian.spring.services;

import lorgar.avrelian.spring.dto.BookDTO;
import lorgar.avrelian.spring.dto.NewReaderDTO;
import lorgar.avrelian.spring.dto.ReaderNoBooksDTO;
import lorgar.avrelian.spring.models.Reader;
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