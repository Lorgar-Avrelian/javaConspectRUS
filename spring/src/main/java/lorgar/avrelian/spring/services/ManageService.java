package lorgar.avrelian.spring.services;

import lorgar.avrelian.spring.models.Book;
import lorgar.avrelian.spring.models.Reader;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ManageService {
    Reader giveBookToReader(long bookId, long readerId);

    Collection<Book> findReaderBooks(long readerId);

    Reader takeBookFromReader(long bookId, long readerId);
}