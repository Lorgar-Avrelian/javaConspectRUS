package lorgar.avrelian.javaconspectrus.services;

import jakarta.servlet.http.HttpServletResponse;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface BookCoverService {
    BookCover uploadCover(long bookId, MultipartFile file);

    BookCover getBookCover(long id);

    void downloadCover(long bookId, HttpServletResponse response);
}
