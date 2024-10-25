package lorgar.avrelian.javaconspectrus.services;

import jakarta.servlet.http.HttpServletResponse;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

@Service
public interface BookCoverService {
    BookCover uploadCover(long bookId, MultipartFile file);

    BookCover getBookCover(long id);

    byte[] generatePreview(Path path);

    BufferedImage getPreview(BufferedImage bufferedImage);

    void downloadCover(long bookId, HttpServletResponse response);

    BookCover saveBookCover(BookCover bookCover);
}
