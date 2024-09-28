package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.repository.ReaderRepository;
import lorgar.avrelian.javaconspectrus.services.ManageService;
import lorgar.avrelian.javaconspectrus.services.ReaderService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    public ReaderServiceImpl(ReaderRepository readerRepository, BookRepository bookRepository) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }
    private ManageService setManageService() {
        return new ManageServiceImpl(bookRepository, readerRepository);
    }

    @Override
    public Reader createReader(Reader reader) {
        reader.setId(0);
        return readerRepository.save(reader);
    }

    @Override
    public Reader findReader(long id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        if (reader != null) {
            reader.setBooks(setManageService().findReaderBooks(reader.getId()));
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public Reader editReader(Reader reader) {
        if (readerRepository.existsById(reader.getId())) {
            reader = readerRepository.save(reader);
            reader.setBooks(setManageService().findReaderBooks(reader.getId()));
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public Reader deleteReader(long id) {
        if (readerRepository.existsById(id)) {
            Reader reader = findReader(id);
            readerRepository.deleteById(id);
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public Collection<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    @Override
    public Collection<Reader> getAllReaders(String partOfNameSecondNameOrSurname) {
        return readerRepository.findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname);
    }

    @Override
    public Collection<Reader> getReaderByName(String partOfName) {
        return readerRepository.findByNameContainsIgnoreCase(partOfName);
    }

    @Override
    public Collection<Reader> getReaderBySecondName(String partOfSecondName) {
        return readerRepository.findBySecondNameContainsIgnoreCase(partOfSecondName);
    }

    @Override
    public Collection<Reader> getReaderBySurname(String partOfSurname) {
        return readerRepository.findBySurnameContainsIgnoreCase(partOfSurname);
    }

    @Override
    public Collection<Book> getReaderBooks(long id) {
        return setManageService().findReaderBooks(id);
    }
}
