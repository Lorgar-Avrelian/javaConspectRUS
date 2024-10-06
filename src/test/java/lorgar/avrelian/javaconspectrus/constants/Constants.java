package lorgar.avrelian.javaconspectrus.constants;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;

import java.nio.file.Path;

public class Constants {
    public static final Book TEST_BOOK_1 = new Book(1, "Test title of 1st book", "Test author of 1st book", 1970, null);
    public static final Book TEST_BOOK_2 = new Book(2, "Test title of 2nd book", "Test author of 2nd book", 1971, null);
    public static final Book TEST_BOOK_3 = new Book(3, "Test title of 3rd book", "Test author of 3rd book", 1972, null);
    public static final Book TEST_BOOK_4 = new Book(4, "Test title of 4th book", "Test author of 4th book", 1973, null);
    public static final Book TEST_BOOK_5 = new Book(5, "Test title of 5th book", "Test author of 5th book", 1974, null);
    public static final Reader TEST_READER_1 = new Reader(1, "1st name", "1st second name", "1st surname", 111, null);
    public static final Reader TEST_READER_2 = new Reader(2, "2nd name", "2nd second name", "2nd surname", 222, null);
    public static final Reader TEST_READER_3 = new Reader(3, "3rd name", "3rd second name", "3rd surname", 333, null);
    public static final Reader TEST_READER_4 = new Reader(4, "4th name with unsupported length, that will be ignored", "4th second name", "4th surname", 444, null);
    public static final Reader TEST_READER_5 = new Reader(5, "5th name", "5th second name", "5th surname", 555, null);
    public static final Path TEST_BOOK_IMAGE_PATH_1 = Path.of("src/test/resources/testImages/1.jpg");
    public static final Path TEST_BOOK_IMAGE_PATH_2 = Path.of("src/test/resources/testImages/2.jpg");
    public static final Path TEST_BOOK_IMAGE_PATH_3 = Path.of("src/test/resources/testImages/3.jpg");
    public static final Path TEST_BOOK_IMAGE_PATH_4 = Path.of("src/test/resources/testImages/4.jpg");
    public static final Path TEST_BOOK_IMAGE_PATH_5 = Path.of("src/test/resources/testImages/5.jpg");
    public static final Path TEST_BOOK_COVER_IMAGES_PATH = Path.of("src/test/resources/testImages/result");
}
