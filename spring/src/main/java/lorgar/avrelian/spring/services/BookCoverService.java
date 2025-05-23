package lorgar.avrelian.spring.services;

import jakarta.servlet.http.HttpServletResponse;
import lorgar.avrelian.spring.models.BookCover;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface BookCoverService {
    BookCover uploadCover(long bookId, MultipartFile file);

    BookCover getBookCover(long id);

    void downloadCover(long bookId, HttpServletResponse response);

    BookCover saveBookCover(BookCover bookCover);

    void deleteBookCover(long id);
}