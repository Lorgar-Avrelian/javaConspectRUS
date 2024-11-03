package lorgar.avrelian.javaconspectrus.configurations;

import lorgar.avrelian.javaconspectrus.mappers.BookMapper;
import lorgar.avrelian.javaconspectrus.repository.BookCoverRepository;
import lorgar.avrelian.javaconspectrus.repository.BookRepository;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import lorgar.avrelian.javaconspectrus.services.implementations.BookCoverServiceImpl;
import lorgar.avrelian.javaconspectrus.services.implementations.BookServiceImplDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {
    @Autowired
    private BookMapper bookMapper;

    @Bean(name = "bookServiceImplDB")
    public BookService bookService(BookRepository bookRepository) {
        return new BookServiceImplDB(bookRepository, bookMapper);
    }

    @Bean(name = "bookCoverServiceImpl")
    public BookCoverService bookCoverService(BookService bookService, BookCoverRepository bookcoverRepository) {
        return new BookCoverServiceImpl(bookService, bookcoverRepository);
    }
}
