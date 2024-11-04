package lorgar.avrelian.javaconspectrus.services.implementations;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import lorgar.avrelian.javaconspectrus.repository.BookCoverRepository;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class BookCoverServiceImpl implements BookCoverService {
    @Value("${books.covers.dir.path}")
    private String coversDir;
    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    public BookCoverServiceImpl(@Qualifier("bookServiceImplDB") BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }

    @Override
    public BookCover uploadCover(long bookId, MultipartFile file) {
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        Path filePath;
        if (fileName != null && !fileName.isEmpty()) {
            filePath = Path.of(coversDir, bookId + "." + getExtension(fileName));
        } else {
            return null;
        }
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return null;
        }
        try (
                InputStream inputStream = file.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        } catch (IOException e) {
            return null;
        }
        BookCover bookCover = getBookCover(bookId);
        bookCover.setId(bookId);
        bookCover.setFilePath(filePath.toString());
        bookCover.setFileSize((int) file.getSize());
        bookCover.setMediaType(file.getContentType());
        bookCover.setImagePreview(generatePreview(filePath));
        bookCover.setBook(book);
        bookCover = saveBookCover(bookCover);
        if (bookCover.getId() == 0) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                return bookCover;
            }
        }
        return bookCover;
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public BookCover getBookCover(long id) {
        return bookCoverRepository.findByBookId(id).orElse(new BookCover());
    }

    private byte[] generatePreview(Path path) {
        try (
                InputStream inputStream = Files.newInputStream(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage preview;
            if (height > width) {
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 200);
                preview = new BufferedImage(200, height, bufferedImage.getType());
                width = 200;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 200);
                preview = new BufferedImage(width, 200, bufferedImage.getType());
                height = 200;
            }
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(preview, getExtension(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void downloadCover(long bookId, HttpServletResponse response) {
        BookCover bookCover = getBookCover(bookId);
        if (bookCover.getImagePreview() != null) {
            Path filePath = Path.of(bookCover.getFilePath());
            try (
                    InputStream inputStream = Files.newInputStream(filePath);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                    OutputStream outputStream = response.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
            ) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(bookCover.getMediaType());
                response.setContentLength(bookCover.getFileSize());
                bufferedInputStream.transferTo(bufferedOutputStream);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public BookCover saveBookCover(BookCover bookCover) {
        return bookCoverRepository.save(bookCover);
    }

    @Override
    public void deleteBookCover(long id) {
        bookCoverRepository.deleteById(id);
    }
}
