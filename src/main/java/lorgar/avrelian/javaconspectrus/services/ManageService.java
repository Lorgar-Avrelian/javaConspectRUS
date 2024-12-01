package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ManageService {
    Reader giveBookToReader(long bookId, long readerId);

    Collection<Book> findReaderBooks(long readerId);

    Reader takeBookFromReader(long bookId, long readerId);
}