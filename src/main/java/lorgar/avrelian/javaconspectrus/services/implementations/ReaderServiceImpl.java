package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewReaderDTO;
import lorgar.avrelian.javaconspectrus.dto.ReaderNoBooksDTO;
import lorgar.avrelian.javaconspectrus.mappers.BookMapper;
import lorgar.avrelian.javaconspectrus.mappers.ReaderMapper;
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
    private final BookMapper bookMapper;
    private final ReaderMapper readerMapper;

    public ReaderServiceImpl(ReaderRepository readerRepository, BookRepository bookRepository, BookMapper bookMapper, ReaderMapper readerMapper) {
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.readerMapper = readerMapper;
    }

    private ManageService setManageService() {
        return new ManageServiceImpl(bookRepository, readerRepository);
    }

    @Override
    public ReaderNoBooksDTO createReader(NewReaderDTO newReader) {
        Reader reader = readerMapper.newReaderDTOtoReader(newReader);
        return readerMapper.readerToNoBooksDTO(readerRepository.save(reader));
    }

    @Override
    public ReaderNoBooksDTO findReader(long id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        if (reader != null) {
            reader.setBooks(setManageService().findReaderBooks(reader.getId()));
            return readerMapper.readerToNoBooksDTO(reader);
        } else {
            return null;
        }
    }

    @Override
    public Reader findDBReader(long id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        if (reader != null) {
            reader.setBooks(setManageService().findReaderBooks(reader.getId()));
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public ReaderNoBooksDTO editReader(Reader reader) {
        if (readerRepository.existsById(reader.getId())) {
            reader = readerRepository.save(reader);
            reader.setBooks(setManageService().findReaderBooks(reader.getId()));
            return readerMapper.readerToNoBooksDTO(reader);
        } else {
            return null;
        }
    }

    @Override
    public ReaderNoBooksDTO deleteReader(long id) {
        if (readerRepository.existsById(id)) {
            ReaderNoBooksDTO reader = findReader(id);
            readerRepository.deleteById(id);
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public Collection<ReaderNoBooksDTO> getAllReaders() {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findAll());
    }

    @Override
    public Collection<Reader> getAllDBReaders() {
        return readerRepository.findAll();
    }

    @Override
    public Collection<ReaderNoBooksDTO> getAllReaders(String partOfNameSecondNameOrSurname) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderByName(String partOfName) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findByNameContainsIgnoreCase(partOfName));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderBySecondName(String partOfSecondName) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findBySecondNameContainsIgnoreCase(partOfSecondName));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderBySurname(String partOfSurname) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findBySurnameContainsIgnoreCase(partOfSurname));
    }

    @Override
    public Collection<BookDTO> getReaderBooks(long id) {
        return bookMapper.booksListToBookDTOList(setManageService().findReaderBooks(id));
    }
}
