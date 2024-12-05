package lorgar.avrelian.javaconspectrus.constants;

import lorgar.avrelian.javaconspectrus.dto.BookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewBookDTO;
import lorgar.avrelian.javaconspectrus.dto.NewReaderDTO;
import lorgar.avrelian.javaconspectrus.dto.ReaderNoBooksDTO;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final Book BOOK_1 = new Book(1,
                                               "Test title of 1st book",
                                               "Test author of 1st book",
                                               (short) 1970,
                                               null);
    public static final Book BOOK_2 = new Book(2,
                                               "Test title of 2nd book",
                                               "Test author of 2nd book",
                                               (short) 1971,
                                               null);
    public static final Book BOOK_3 = new Book(3,
                                               "Test title of 3rd book",
                                               "Test author of 3rd book",
                                               (short) 1972,
                                               null);
    public static final Book BOOK_4 = new Book(4,
                                               "Test title of 4th book",
                                               "Test author of 4th book",
                                               (short) 1973,
                                               null);
    public static final Book BOOK_5 = new Book(5,
                                               "Test title of 5th book",
                                               "Test author of 5th book",
                                               (short) 1974,
                                               null);
    public static final NewBookDTO NEW_BOOK_DTO_1 = new NewBookDTO(
            BOOK_1.getTitle(),
            BOOK_1.getAuthor(),
            BOOK_1.getYear());
    public static final NewBookDTO NEW_BOOK_DTO_2 = new NewBookDTO(
            BOOK_2.getTitle(),
            BOOK_2.getAuthor(),
            BOOK_2.getYear());
    public static final NewBookDTO NEW_BOOK_DTO_3 = new NewBookDTO(
            BOOK_3.getTitle(),
            BOOK_3.getAuthor(),
            BOOK_3.getYear());
    public static final NewBookDTO NEW_BOOK_DTO_4 = new NewBookDTO(
            BOOK_4.getTitle(),
            BOOK_4.getAuthor(),
            BOOK_4.getYear());
    public static final NewBookDTO NEW_BOOK_DTO_5 = new NewBookDTO(
            BOOK_5.getTitle(),
            BOOK_5.getAuthor(),
            BOOK_5.getYear());
    public static final BookDTO BOOK_DTO_1 = new BookDTO(
            BOOK_1.getId(),
            BOOK_1.getTitle(),
            BOOK_1.getAuthor(),
            BOOK_1.getYear(),
            null);
    public static final BookDTO BOOK_DTO_2 = new BookDTO(
            BOOK_2.getId(),
            BOOK_2.getTitle(),
            BOOK_2.getAuthor(),
            BOOK_2.getYear(),
            null);
    public static final BookDTO BOOK_DTO_3 = new BookDTO(
            BOOK_3.getId(),
            BOOK_3.getTitle(),
            BOOK_3.getAuthor(),
            BOOK_3.getYear(),
            null);
    public static final BookDTO BOOK_DTO_4 = new BookDTO(
            BOOK_4.getId(),
            BOOK_4.getTitle(),
            BOOK_4.getAuthor(),
            BOOK_4.getYear(),
            null);
    public static final BookDTO BOOK_DTO_5 = new BookDTO(
            BOOK_5.getId(),
            BOOK_5.getTitle(),
            BOOK_5.getAuthor(),
            BOOK_5.getYear(),
            null);
    public static final List<Book> BOOKS_COLLECTION = new ArrayList<>(
            List.of(BOOK_1, BOOK_2, BOOK_3, BOOK_4, BOOK_5)
    );
    public static final List<BookDTO> BOOKS_DTO_COLLECTION = new ArrayList<>(
            List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3, BOOK_DTO_4, BOOK_DTO_5)
    );
    //
    public static final Reader READER_1 = new Reader(
            1,
            "1st name",
            "1st second name",
            "1st surname",
            111,
            new ArrayList<>());
    public static final Reader READER_2 = new Reader(
            2,
            "2nd name",
            "2nd second name",
            "2nd surname",
            222,
            new ArrayList<>());
    public static final Reader READER_3 = new Reader(
            3,
            "3rd name",
            "3rd second name",
            "3rd surname",
            333,
            new ArrayList<>());
    public static final Reader READER_4 = new Reader(
            4,
            "4th name",
            "4th second name",
            "4th surname",
            444,
            new ArrayList<>());
    public static final NewReaderDTO NEW_READER_DTO_1 = new NewReaderDTO(
            READER_1.getName(),
            READER_1.getSecondName(),
            READER_1.getSurname(),
            READER_1.getPersonalNumber());
    public static final NewReaderDTO NEW_READER_DTO_2 = new NewReaderDTO(
            READER_2.getName(),
            READER_2.getSecondName(),
            READER_2.getSurname(),
            READER_2.getPersonalNumber());
    public static final NewReaderDTO NEW_READER_DTO_3 = new NewReaderDTO(
            READER_3.getName(),
            READER_3.getSecondName(),
            READER_3.getSurname(),
            READER_3.getPersonalNumber());
    public static final NewReaderDTO NEW_READER_DTO_4 = new NewReaderDTO(
            READER_4.getName(),
            READER_4.getSecondName(),
            READER_4.getSurname(),
            READER_4.getPersonalNumber());
    public static final ReaderNoBooksDTO READER_NO_BOOKS_DTO_1 = new ReaderNoBooksDTO(
            READER_1.getId(),
            READER_1.getName(),
            READER_1.getSecondName(),
            READER_1.getSurname(),
            READER_1.getPersonalNumber());
    public static final ReaderNoBooksDTO READER_NO_BOOKS_DTO_2 = new ReaderNoBooksDTO(
            READER_2.getId(),
            READER_2.getName(),
            READER_2.getSecondName(),
            READER_2.getSurname(),
            READER_2.getPersonalNumber());
    public static final ReaderNoBooksDTO READER_NO_BOOKS_DTO_3 = new ReaderNoBooksDTO(
            READER_3.getId(),
            READER_3.getName(),
            READER_3.getSecondName(),
            READER_3.getSurname(),
            READER_3.getPersonalNumber());
    public static final ReaderNoBooksDTO READER_NO_BOOKS_DTO_4 = new ReaderNoBooksDTO(
            READER_4.getId(),
            READER_4.getName(),
            READER_4.getSecondName(),
            READER_4.getSurname(),
            READER_4.getPersonalNumber());
    public static final List<Reader> READERS_COLLECTION = new ArrayList<>(
            List.of(READER_1, READER_2, READER_3, READER_4)
    );
    public static final List<ReaderNoBooksDTO> READERS_NO_BOOKS_LIST = new ArrayList<>(
            List.of(READER_NO_BOOKS_DTO_1, READER_NO_BOOKS_DTO_2, READER_NO_BOOKS_DTO_3, READER_NO_BOOKS_DTO_4)
    );
    //
    public static final Expense EXPENSE_1 = new Expense(
            1,
            "Test title 1",
            LocalDate.of(
                    2024,
                    Month.NOVEMBER,
                    30),
            "Test category 1",
            100f);
    public static final Expense EXPENSE_2 = new Expense(
            2,
            "Test title 2",
            LocalDate.of(
                    2024,
                    Month.NOVEMBER,
                    30),
            "Test category 1",
            200f);
    public static final Expense EXPENSE_3 = new Expense(
            3,
            "Test title 3",
            LocalDate.of(
                    2024,
                    Month.NOVEMBER,
                    30),
            "Test category 2",
            300f);
    public static final Expense EXPENSE_4 = new Expense(
            4,
            "Test title 4",
            LocalDate.of(
                    2024,
                    Month.NOVEMBER,
                    30),
            "Test category 2",
            400f);
    public static final Expense EXPENSE_5 = new Expense(
            5,
            "Test title 5",
            LocalDate.of(
                    2024,
                    Month.NOVEMBER,
                    30),
            "Test category 3",
            500f);
    public static final List<Expense> EXPENSES_COLLECTION = new ArrayList<>(
            List.of(EXPENSE_1, EXPENSE_2, EXPENSE_3, EXPENSE_4, EXPENSE_5)
    );
    public static final Page<Expense> EXPENSES_COLLECTION_PAGE = new PageImpl<>(
            EXPENSES_COLLECTION,
            PageRequest.of(
                    0,
                    EXPENSES_COLLECTION.size(),
                    Sort.by("id")),
                    EXPENSES_COLLECTION.size()
    );
    public static final List<ExpensesByCategory> EXPENSES_BY_CATEGORY_COLLECTION = new ArrayList<>(
            List.of(
                    new ExpensesByCategory() {

                        @Override
                        public String getCategory() {
                            return EXPENSE_1.getCategory();
                        }

                        @Override
                        public float getAmount() {
                            return 2f;
                        }
                    },
                    new ExpensesByCategory() {

                        @Override
                        public String getCategory() {
                            return EXPENSE_3.getCategory();
                        }

                        @Override
                        public float getAmount() {
                            return 2f;
                        }
                    },
                    new ExpensesByCategory() {

                        @Override
                        public String getCategory() {
                            return EXPENSE_5.getCategory();
                        }

                        @Override
                        public float getAmount() {
                            return 1f;
                        }
                    }
                   )
    );
    //
    public static final Reader READER_UNSUPPORTED = new Reader(4, "4th name with unsupported length, that will be ignored", "4th second name", "4th surname", 444, new ArrayList<>());
    public static final Path BOOK_IMAGE_PATH_1 = Path.of("src/test/resources/testImages/1.jpg");
    public static final Path BOOK_IMAGE_PATH_2 = Path.of("src/test/resources/testImages/2.jpg");
    public static final Path BOOK_IMAGE_PATH_3 = Path.of("src/test/resources/testImages/3.jpg");
}
