package lorgar.avrelian.javaconspectrus.services.implementations;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import lorgar.avrelian.javaconspectrus.repository.BookCoverRepository;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
@Transactional
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
        // Ищем указанную книгу по ID. Если не находим возвращаем null в контроллер
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return null;
        }
        // Создаём путь, по которому будет сохранён файл обложки
        // При этом получаем расширение файла при помощи private метода getExtension()
        String fileName = file.getOriginalFilename();
        Path filePath;
        if (fileName != null && !fileName.isEmpty()) {
            filePath = Path.of(coversDir, bookId + "." + getExtension(fileName));
        } else {
            return null;
        }
        // При помощи методов класса Files создаём директорию (если она не существует) и удаляем старый файл (если он существует)
        // Методы класса Files могут выбросить IOException, поэтому заключаем их в блок try-catch
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return null;
        }
        // Запускаем входные и выходные потоки данных для сохранения файла обложки на жёсткий диск
        // При этом создаём буферизированные потоки из входного и выходного, чтобы ускорить процесс обработки
        // Выходной поток данных запускаем при помощи метода класса Files newOutputStream()
        // Методы класса Files могут выбросить IOException, поэтому заключаем их в блок try-catch-with-resources
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
        // Получаем сущность обложки из БД или создаём новую, если её в БД не существует при помощи private метода getBookCover()
        BookCover bookCover = getBookCover(bookId);
        // Вносим изменения в сущность обложки:
        // - заменяем ID обложки на ID книги
        bookCover.setId(bookId);
        // - заменяем путь до оригинального файла на жёстком диске, конвертируя filePath в строку
        bookCover.setFilePath(filePath.toString());
        // - заменяем размер файла: приводим его к значению int, поскольку метод getSize() возвращает значение типа long
        bookCover.setFileSize((int) file.getSize());
        // - заменяем тип файла, получая его строчное значение благодаря методу getContentType()
        bookCover.setMediaType(file.getContentType());
        // - заменяем превью, генерируя новое с помощью статического метода generatePreview()
        bookCover.setImagePreview(generatePreview(filePath));
        // - заменяем ссылку на книгу в БД
        bookCover.setBook(book);
        // - сохраняем обновлённую (или новую) сущность обложки в БД
        bookCoverRepository.save(bookCover);
        // Возвращаем превью обложки (в случае, если она корректно сохранилась)
        return getBookCover(bookId);
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
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
                preview = new BufferedImage(100, height, bufferedImage.getType());
                width = 100;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 100);
                preview = new BufferedImage(width, 100, bufferedImage.getType());
                height = 100;
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
}
