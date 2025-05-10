package lorgar.avrelian.spring.configurations;

import lorgar.avrelian.spring.mappers.BookMapper;
import lorgar.avrelian.spring.repository.BookCoverRepository;
import lorgar.avrelian.spring.repository.BookRepository;
import lorgar.avrelian.spring.services.BookCoverService;
import lorgar.avrelian.spring.services.BookService;
import lorgar.avrelian.spring.services.implementations.BookCoverServiceImpl;
import lorgar.avrelian.spring.services.implementations.BookServiceImplDB;
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