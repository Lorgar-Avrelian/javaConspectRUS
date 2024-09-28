package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.repository.ReaderRepository;
import lorgar.avrelian.javaconspectrus.services.BookService;
import lorgar.avrelian.javaconspectrus.services.ManageService;
import lorgar.avrelian.javaconspectrus.services.ReaderService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ManageServiceImpl implements ManageService {
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public ManageServiceImpl(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    private ReaderService setReaderService() {
        return new ReaderServiceImpl(readerRepository, bookRepository);
    }

    private BookService setBookService() {
        return new BookServiceImplDB(bookRepository);
    }

    @Override
    public Reader giveBookToReader(long bookId, long readerId) {
        Reader reader = setReaderService().findReader(readerId);
        Book book = setBookService().findBook(bookId);
        if (book != null && reader != null && book.getReader() == null) {
            book.setReader(reader);
        } else {
            return null;
        }
        return setReaderBooks(book, reader);
    }

    @Override
    public Collection<Book> findReaderBooks(long readerId) {
        return bookRepository.findByReaderId(readerId);
    }

    @Override
    public Reader takeBookFromReader(long bookId, long readerId) {
        Reader reader = setReaderService().findReader(readerId);
        Book book = setBookService().findBook(bookId);
        if (book != null && reader != null && reader.equals(book.getReader())) {
            book.setReader(null);
        } else {
            return null;
        }
        return setReaderBooks(book, reader);
    }

    private Reader setReaderBooks(Book book, Reader reader) {
        setBookService().editBook(book);
        reader.setBooks(findReaderBooks(reader.getId()));
        return reader;
    }
}
