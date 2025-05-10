package lorgar.avrelian.spring.services.implementations;

import lorgar.avrelian.spring.models.Book;
import lorgar.avrelian.spring.models.Reader;
import lorgar.avrelian.spring.repository.BookRepository;
import lorgar.avrelian.spring.repository.ReaderRepository;
import lorgar.avrelian.spring.services.ManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class ManageServiceImpl implements ManageService {
    private Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public ManageServiceImpl(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public Reader giveBookToReader(long bookId, long readerId) {
        logger.debug("Give to reader " + readerId + " book " + bookId);
        Reader reader;
        Book book;
        try {
            reader = readerRepository.findById(readerId).get();
            book = bookRepository.findById(bookId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (book.getReader() == null) {
            book.setReader(reader);
        } else {
            logger.error("Book " + bookId + " or reader " + readerId + " not found");
            return null;
        }
        return setReaderBooks(book, reader);
    }

    @Override
    public Collection<Book> findReaderBooks(long readerId) {
        logger.debug("Find reader " + readerId);
        return bookRepository.findByReaderId(readerId);
    }

    @Override
    public Reader takeBookFromReader(long bookId, long readerId) {
        logger.trace("Take book " + bookId + " from reader " + readerId);
        Reader reader;
        Book book;
        try {
            reader = readerRepository.findById(readerId).get();
            book = bookRepository.findById(bookId).get();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (reader.equals(book.getReader())) {
            book.setReader(null);
            Collection<Book> books = reader.getBooks();
            books.remove(book);
            reader.setBooks(books);
        } else {
            logger.error("Book " + bookId + " or reader " + readerId + " not found");
            return null;
        }
        return setReaderBooks(book, reader);
    }

    private Reader setReaderBooks(Book book, Reader reader) {
        bookRepository.save(book);
        Reader result = readerRepository.save(reader);
        logger.info("Book " + book.getId() + " has been edited");
        logger.trace("Reader " + reader.getId() + " had been edited");
        return result;
    }
}