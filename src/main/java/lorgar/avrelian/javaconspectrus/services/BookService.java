package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.models.Book;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface BookService {
    Book createBook(Book book);

    Book findBook(long id);

    Book editBook(Book book);

    Book deleteBook(long id);

    Collection<Book> getAllBooks();
}
