package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewBookDTO;import lorgar.avrelian.javaconspectrus.mappers.BookMapper;import lorgar.avrelian.javaconspectrus.mappers.BookMapperImpl;import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.services.BookService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Profile("!test")
public class BookServiceImplHashMap implements BookService {
    private final HashMap<Long, Book> books = new HashMap<>();
    private long lastId = 0;
    private final BookMapper bookMapper;

    public BookServiceImplHashMap(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public Book createBook(NewBookDTO newBookDTO) {
        Book book = bookMapper.newBookDTOtoBook(newBookDTO);
        book.setId(++lastId);
        books.put(lastId, book);
        return book;
    }

    @Override
    public Book findBook(long id) {
        return books.get(id);
    }

    @Override
    public Book editBook(BookDTO book) {
        if (books.get(book.getId()) != null) {
            books.put(book.getId(), bookMapper.bookDTOToBook(book));
            return bookMapper.bookDTOToBook(book);
        } else {
            return null;
        }
    }

    @Override
    public Book deleteBook(long id) {
        if (books.get(id) != null) {
            return books.remove(id);
        } else {
            return null;
        }
    }

    @Override
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @Override
    public Collection<Book> getAllBooks(String authorOrTitle) {
        Set<Book> booksSet = new HashSet<>();
        for (Map.Entry<Long, Book> longBookEntry : books.entrySet()) {
            if (longBookEntry.getValue().getAuthor().toLowerCase().contains(authorOrTitle.toLowerCase()) || longBookEntry.getValue().getTitle().toLowerCase().contains(authorOrTitle.toLowerCase())) {
                booksSet.add(longBookEntry.getValue());
            }
        }
        return booksSet;
    }
}
