package lorgar.avrelian.javaconspectrus.constants;

import lorgar.avrelian.javaconspectrus.dao.City;
import lorgar.avrelian.javaconspectrus.dao.Login;
import lorgar.avrelian.javaconspectrus.dto.*;
import lorgar.avrelian.javaconspectrus.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Time;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final Login OWNER = new Login(
            1L,
            "OWNER",
            "123",
            Role.ROLE_OWNER
    );
    public static final Login ADMIN = new Login(
            2L,
            "ADMIN",
            "123",
            Role.ROLE_ADMIN
    );
    public static final Login USER = new Login(
            3L,
            "USER",
            "123",
            Role.ROLE_USER
    );
    public static final User OWNER_DETAILS = new User(
            OWNER.getLogin(),
            OWNER.getPassword(),
            new ArrayList<>(List.of(OWNER.getRole()))
    );
    public static final User ADMIN_DETAILS = new User(
            ADMIN.getLogin(),
            ADMIN.getPassword(),
            new ArrayList<>(List.of(ADMIN.getRole()))
    );
    public static final User USER_DETAILS = new User(
            USER.getLogin(),
            USER.getPassword(),
            new ArrayList<>(List.of(USER.getRole()))
    );
    //
    public static final City MOSCOW = new City(
            1L,
            "Moscow",
            new BigDecimal("55.75"),
            new BigDecimal("37.62"),
            "RU",
            "Moscow",
            "{\"hi\":\"मास्को\",\"ps\":\"مسکو\",\"pt\":\"Moscou\",\"hr\":\"Moskva\",\"ht\":\"Moskou\",\"hu\":\"Moszkva\",\"yi\":\"מאסקווע\",\"hy\":\"Մոսկվա\",\"yo\":\"Mọsko\",\"ia\":\"Moscova\",\"id\":\"Moskwa\",\"ie\":\"Moskwa\",\"ab\":\"Москва\",\"feature_name\":\"Moscow\",\"qu\":\"Moskwa\",\"af\":\"Moskou\",\"io\":\"Moskva\",\"za\":\"Moscow\",\"is\":\"Moskva\",\"ak\":\"Moscow\",\"it\":\"Mosca\",\"am\":\"ሞስኮ\",\"iu\":\"ᒨᔅᑯ\",\"an\":\"Moscú\",\"zh\":\"莫斯科\",\"ar\":\"موسكو\",\"av\":\"Москва\",\"ja\":\"モスクワ\",\"ay\":\"Mosku\",\"az\":\"Moskva\",\"zu\":\"IMoskwa\",\"ro\":\"Moscova\",\"ba\":\"Мәскәү\",\"be\":\"Масква\",\"ru\":\"Москва\",\"bg\":\"Москва\",\"bi\":\"Moskow\",\"jv\":\"Moskwa\",\"bn\":\"মস্কো\",\"bo\":\"མོ་སི་ཁོ།\",\"sc\":\"Mosca\",\"br\":\"Moskov\",\"bs\":\"Moskva\",\"se\":\"Moskva\",\"sg\":\"Moscow\",\"sh\":\"Moskva\",\"ka\":\"მოსკოვი\",\"sk\":\"Moskva\",\"sl\":\"Moskva\",\"sm\":\"Moscow\",\"kg\":\"Moskva\",\"so\":\"Moskow\",\"ca\":\"Moscou\",\"sq\":\"Moska\",\"sr\":\"Москва\",\"kk\":\"Мәскеу\",\"ss\":\"Moscow\",\"st\":\"Moscow\",\"kl\":\"Moskva\",\"ce\":\"Москох\",\"su\":\"Moskwa\",\"sv\":\"Moskva\",\"kn\":\"ಮಾಸ್ಕೋ\",\"ko\":\"모스크바\",\"sw\":\"Moscow\",\"ch\":\"Moscow\",\"ku\":\"Moskow\",\"kv\":\"Мӧскуа\",\"kw\":\"Moskva\",\"co\":\"Moscù\",\"ta\":\"மாஸ்கோ\",\"ky\":\"Москва\",\"cs\":\"Moskva\",\"te\":\"మాస్కో\",\"cu\":\"Москъва\",\"cv\":\"Мускав\",\"tg\":\"Маскав\",\"th\":\"มอสโก\",\"la\":\"Moscua\",\"cy\":\"Moscfa\",\"tk\":\"Moskwa\",\"tl\":\"Moscow\",\"lg\":\"Moosko\",\"li\":\"Moskou\",\"da\":\"Moskva\",\"tr\":\"Moskova\",\"tt\":\"Мәскәү\",\"de\":\"Moskau\",\"ln\":\"Moskú\",\"ty\":\"Moscou\",\"lt\":\"Maskva\",\"lv\":\"Maskava\",\"ug\":\"Moskwa\",\"dv\":\"މޮސްކޯ\",\"dz\":\"མོསི་ཀོ\",\"uk\":\"Москва\",\"mg\":\"Moskva\",\"mi\":\"Mohikau\",\"ur\":\"ماسکو\",\"mk\":\"Москва\",\"ml\":\"മോസ്കോ\",\"mn\":\"Москва\",\"mr\":\"मॉस्को\",\"uz\":\"Moskva\",\"ms\":\"Moscow\",\"el\":\"Μόσχα\",\"mt\":\"Moska\",\"en\":\"Moscow\",\"eo\":\"Moskvo\",\"my\":\"မော်စကိုမြို့\",\"es\":\"Moscú\",\"et\":\"Moskva\",\"eu\":\"Mosku\",\"na\":\"Moscow\",\"vi\":\"Mát-xcơ-va\",\"nb\":\"Moskva\",\"vo\":\"Moskva\",\"fa\":\"مسکو\",\"nl\":\"Moskou\",\"nn\":\"Moskva\",\"no\":\"Moskva\",\"fi\":\"Moskova\",\"fo\":\"Moskva\",\"wa\":\"Moscou\",\"fr\":\"Moscou\",\"fy\":\"Moskou\",\"oc\":\"Moscòu\",\"wo\":\"Mosku\",\"ga\":\"Moscó\",\"gd\":\"Moscobha\",\"ascii\":\"Moscow\",\"os\":\"Мæскуы\",\"gl\":\"Moscova - Москва\",\"gn\":\"Mosku\",\"gv\":\"Moscow\",\"pl\":\"Moskwa\",\"he\":\"מוסקווה\"}"
    );
    public static final City NOGINSK = new City(
            2L,
            "Noginsk",
            new BigDecimal("55.86"),
            new BigDecimal("38.44"),
            "RU",
            "Moscow Oblast",
            "{\"nn\":\"Noginsk\",\"de\":\"Noginsk\",\"no\":\"Noginsk\",\"be\":\"Нагінск\",\"ru\":\"Ногинск\",\"fi\":\"Noginsk\",\"pt\":\"Noguinsk\",\"lt\":\"Noginskas\",\"hr\":\"Noginsk\",\"lv\":\"Noginska\",\"fr\":\"Noguinsk\",\"hu\":\"Noginszk\",\"uk\":\"Ногінськ\",\"ca\":\"Noguinsk\",\"sr\":\"Ногинск\",\"sv\":\"Noginsk\",\"ko\":\"노긴스크\",\"en\":\"Noginsk\",\"eo\":\"Noginsk\",\"it\":\"Noginsk\",\"es\":\"Noguinsk\",\"zh\":\"諾金斯克\",\"et\":\"Noginsk\",\"cs\":\"Noginsk\",\"ar\":\"نوجينسك\",\"vi\":\"Noginsk\",\"ja\":\"ノギヌスク\",\"tl\":\"Noginsk\",\"az\":\"Noqinsk\",\"fa\":\"نوگینسک\",\"pl\":\"Nogińsk\",\"ro\":\"Noghinsk\",\"nl\":\"Noginsk\",\"tr\":\"Noginsk\"}"
    );
    public static final City LONDON = new City(
            3L,
            "London",
            new BigDecimal("51.51"),
            new BigDecimal("-0.13"),
            "GB",
            "England",
            "{\"hi\":\"लंदन\",\"ps\":\"لندن\",\"pt\":\"Londres\",\"hr\":\"London\",\"ht\":\"Lonn\",\"hu\":\"London\",\"yi\":\"לאנדאן\",\"hy\":\"Լոնդոն\",\"yo\":\"Lọndọnu\",\"ia\":\"London\",\"id\":\"London\",\"ie\":\"London\",\"ig\":\"London\",\"ab\":\"Лондон\",\"feature_name\":\"London\",\"qu\":\"London\",\"af\":\"Londen\",\"io\":\"London\",\"is\":\"London\",\"it\":\"Londra\",\"am\":\"ለንደን\",\"an\":\"Londres\",\"zh\":\"伦敦\",\"ar\":\"لندن\",\"av\":\"Лондон\",\"ja\":\"ロンドン\",\"ay\":\"London\",\"az\":\"London\",\"zu\":\"ILondon\",\"rm\":\"Londra\",\"ro\":\"Londra\",\"ba\":\"Лондон\",\"be\":\"Лондан\",\"ru\":\"Лондон\",\"bg\":\"Лондон\",\"bh\":\"लंदन\",\"bi\":\"London\",\"bm\":\"London\",\"bn\":\"লন্ডন\",\"jv\":\"London\",\"bo\":\"ལོན་ཊོན།\",\"sa\":\"लन्डन्\",\"br\":\"Londrez\",\"sc\":\"Londra\",\"bs\":\"London\",\"sd\":\"لنڊن\",\"se\":\"London\",\"sh\":\"London\",\"ka\":\"ლონდონი\",\"si\":\"ලන්ඩන්\",\"sk\":\"Londýn\",\"sl\":\"London\",\"sm\":\"Lonetona\",\"sn\":\"London\",\"so\":\"London\",\"ca\":\"Londres\",\"sq\":\"Londra\",\"sr\":\"Лондон\",\"kk\":\"Лондон\",\"kl\":\"London\",\"st\":\"London\",\"km\":\"ឡុងដ៍\",\"su\":\"London\",\"ce\":\"Лондон\",\"sv\":\"London\",\"kn\":\"ಲಂಡನ್\",\"ko\":\"런던\",\"sw\":\"London\",\"ku\":\"London\",\"kv\":\"Лондон\",\"kw\":\"Loundres\",\"co\":\"Londra\",\"ta\":\"இலண்டன்\",\"ky\":\"Лондон\",\"cs\":\"Londýn\",\"te\":\"లండన్\",\"cu\":\"Лондонъ\",\"tg\":\"Лондон\",\"cv\":\"Лондон\",\"th\":\"ลอนดอน\",\"cy\":\"Llundain\",\"lb\":\"London\",\"tk\":\"London\",\"tl\":\"Londres\",\"to\":\"Lonitoni\",\"li\":\"Londe\",\"da\":\"London\",\"tr\":\"Londra\",\"tt\":\"Лондон\",\"de\":\"London\",\"ln\":\"Lóndɛlɛ\",\"lo\":\"ລອນດອນ\",\"tw\":\"London\",\"lt\":\"Londonas\",\"lv\":\"Londona\",\"ug\":\"لوندۇن\",\"uk\":\"Лондон\",\"mg\":\"Lôndôna\",\"mi\":\"Rānana\",\"ur\":\"علاقہ لندن\",\"mk\":\"Лондон\",\"ml\":\"ലണ്ടൻ\",\"ee\":\"London\",\"mn\":\"Лондон\",\"uz\":\"London\",\"mr\":\"लंडन\",\"ms\":\"London\",\"el\":\"Λονδίνο\",\"mt\":\"Londra\",\"en\":\"London\",\"eo\":\"Londono\",\"my\":\"လန်ဒန်မြို့\",\"es\":\"Londres\",\"et\":\"London\",\"eu\":\"Londres\",\"na\":\"London\",\"vi\":\"Luân Đôn\",\"ne\":\"लन्डन\",\"vo\":\"London\",\"fa\":\"لندن\",\"nl\":\"Londen\",\"nn\":\"London\",\"ff\":\"London\",\"no\":\"London\",\"fi\":\"Lontoo\",\"fj\":\"Lodoni\",\"nv\":\"Tooh Dineʼé Bikin Haalʼá\",\"fo\":\"London\",\"wa\":\"Londe\",\"ny\":\"London\",\"fr\":\"Londres\",\"fy\":\"Londen\",\"oc\":\"Londres\",\"wo\":\"Londar\",\"ga\":\"Londain\",\"ascii\":\"London\",\"gd\":\"Lunnainn\",\"om\":\"Landan\",\"or\":\"ଲଣ୍ଡନ\",\"os\":\"Лондон\",\"gl\":\"Londres\",\"gn\":\"Lóndyre\",\"gu\":\"લંડન\",\"gv\":\"Lunnin\",\"pa\":\"ਲੰਡਨ\",\"ha\":\"Landan\",\"pl\":\"Londyn\",\"he\":\"לונדון\"}"
    );
    public static final List<City> CITIES = new ArrayList<>(
            List.of(MOSCOW, NOGINSK, LONDON)
    );
    public static final CityDTO MOSCOW_DTO = new CityDTO(
            MOSCOW.getName(),
            new HashMap<>(
                    Map.of(
                            "Moscow", "{\"hi\":\"मास्को\",\"ps\":\"مسکو\",\"pt\":\"Moscou\",\"hr\":\"Moskva\",\"ht\":\"Moskou\",\"hu\":\"Moszkva\",\"yi\":\"מאסקווע\",\"hy\":\"Մոսկվա\",\"yo\":\"Mọsko\",\"ia\":\"Moscova\",\"id\":\"Moskwa\",\"ie\":\"Moskwa\",\"ab\":\"Москва\",\"feature_name\":\"Moscow\",\"qu\":\"Moskwa\",\"af\":\"Moskou\",\"io\":\"Moskva\",\"za\":\"Moscow\",\"is\":\"Moskva\",\"ak\":\"Moscow\",\"it\":\"Mosca\",\"am\":\"ሞስኮ\",\"iu\":\"ᒨᔅᑯ\",\"an\":\"Moscú\",\"zh\":\"莫斯科\",\"ar\":\"موسكو\",\"av\":\"Москва\",\"ja\":\"モスクワ\",\"ay\":\"Mosku\",\"az\":\"Moskva\",\"zu\":\"IMoskwa\",\"ro\":\"Moscova\",\"ba\":\"Мәскәү\",\"be\":\"Масква\",\"ru\":\"Москва\",\"bg\":\"Москва\",\"bi\":\"Moskow\",\"jv\":\"Moskwa\",\"bn\":\"মস্কো\",\"bo\":\"མོ་སི་ཁོ།\",\"sc\":\"Mosca\",\"br\":\"Moskov\",\"bs\":\"Moskva\",\"se\":\"Moskva\",\"sg\":\"Moscow\",\"sh\":\"Moskva\",\"ka\":\"მოსკოვი\",\"sk\":\"Moskva\",\"sl\":\"Moskva\",\"sm\":\"Moscow\",\"kg\":\"Moskva\",\"so\":\"Moskow\",\"ca\":\"Moscou\",\"sq\":\"Moska\",\"sr\":\"Москва\",\"kk\":\"Мәскеу\",\"ss\":\"Moscow\",\"st\":\"Moscow\",\"kl\":\"Moskva\",\"ce\":\"Москох\",\"su\":\"Moskwa\",\"sv\":\"Moskva\",\"kn\":\"ಮಾಸ್ಕೋ\",\"ko\":\"모스크바\",\"sw\":\"Moscow\",\"ch\":\"Moscow\",\"ku\":\"Moskow\",\"kv\":\"Мӧскуа\",\"kw\":\"Moskva\",\"co\":\"Moscù\",\"ta\":\"மாஸ்கோ\",\"ky\":\"Москва\",\"cs\":\"Moskva\",\"te\":\"మాస్కో\",\"cu\":\"Москъва\",\"cv\":\"Мускав\",\"tg\":\"Маскав\",\"th\":\"มอสโก\",\"la\":\"Moscua\",\"cy\":\"Moscfa\",\"tk\":\"Moskwa\",\"tl\":\"Moscow\",\"lg\":\"Moosko\",\"li\":\"Moskou\",\"da\":\"Moskva\",\"tr\":\"Moskova\",\"tt\":\"Мәскәү\",\"de\":\"Moskau\",\"ln\":\"Moskú\",\"ty\":\"Moscou\",\"lt\":\"Maskva\",\"lv\":\"Maskava\",\"ug\":\"Moskwa\",\"dv\":\"މޮސްކޯ\",\"dz\":\"མོསི་ཀོ\",\"uk\":\"Москва\",\"mg\":\"Moskva\",\"mi\":\"Mohikau\",\"ur\":\"ماسکو\",\"mk\":\"Москва\",\"ml\":\"മോസ്കോ\",\"mn\":\"Москва\",\"mr\":\"मॉस्को\",\"uz\":\"Moskva\",\"ms\":\"Moscow\",\"el\":\"Μόσχα\",\"mt\":\"Moska\",\"en\":\"Moscow\",\"eo\":\"Moskvo\",\"my\":\"မော်စကိုမြို့\",\"es\":\"Moscú\",\"et\":\"Moskva\",\"eu\":\"Mosku\",\"na\":\"Moscow\",\"vi\":\"Mát-xcơ-va\",\"nb\":\"Moskva\",\"vo\":\"Moskva\",\"fa\":\"مسکو\",\"nl\":\"Moskou\",\"nn\":\"Moskva\",\"no\":\"Moskva\",\"fi\":\"Moskova\",\"fo\":\"Moskva\",\"wa\":\"Moscou\",\"fr\":\"Moscou\",\"fy\":\"Moskou\",\"oc\":\"Moscòu\",\"wo\":\"Mosku\",\"ga\":\"Moscó\",\"gd\":\"Moscobha\",\"ascii\":\"Moscow\",\"os\":\"Мæскуы\",\"gl\":\"Moscova - Москва\",\"gn\":\"Mosku\",\"gv\":\"Moscow\",\"pl\":\"Moskwa\",\"he\":\"מוסקווה\"}"
                          )
            ),
            55.75,
            37.62,
            "RU",
            "Moscow"
    );
    public static final CityDTO NOGINSK_DTO = new CityDTO(
            NOGINSK.getName(),
            new HashMap<>(
                    Map.of(
                            "Noginsk", "{\"nn\":\"Noginsk\",\"de\":\"Noginsk\",\"no\":\"Noginsk\",\"be\":\"Нагінск\",\"ru\":\"Ногинск\",\"fi\":\"Noginsk\",\"pt\":\"Noguinsk\",\"lt\":\"Noginskas\",\"hr\":\"Noginsk\",\"lv\":\"Noginska\",\"fr\":\"Noguinsk\",\"hu\":\"Noginszk\",\"uk\":\"Ногінськ\",\"ca\":\"Noguinsk\",\"sr\":\"Ногинск\",\"sv\":\"Noginsk\",\"ko\":\"노긴스크\",\"en\":\"Noginsk\",\"eo\":\"Noginsk\",\"it\":\"Noginsk\",\"es\":\"Noguinsk\",\"zh\":\"諾金斯克\",\"et\":\"Noginsk\",\"cs\":\"Noginsk\",\"ar\":\"نوجينسك\",\"vi\":\"Noginsk\",\"ja\":\"ノギヌスク\",\"tl\":\"Noginsk\",\"az\":\"Noqinsk\",\"fa\":\"نوگینسک\",\"pl\":\"Nogińsk\",\"ro\":\"Noghinsk\",\"nl\":\"Noginsk\",\"tr\":\"Noginsk\"}"
                          )
            ),
            55.86,
            38.44,
            "RU",
            "Moscow Oblast"
    );
    public static final CityDTO LONDON_DTO = new CityDTO(
            LONDON.getName(),
            new HashMap<>(
                    Map.of(
                            "London", "{\"hi\":\"लंदन\",\"ps\":\"لندن\",\"pt\":\"Londres\",\"hr\":\"London\",\"ht\":\"Lonn\",\"hu\":\"London\",\"yi\":\"לאנדאן\",\"hy\":\"Լոնդոն\",\"yo\":\"Lọndọnu\",\"ia\":\"London\",\"id\":\"London\",\"ie\":\"London\",\"ig\":\"London\",\"ab\":\"Лондон\",\"feature_name\":\"London\",\"qu\":\"London\",\"af\":\"Londen\",\"io\":\"London\",\"is\":\"London\",\"it\":\"Londra\",\"am\":\"ለንደን\",\"an\":\"Londres\",\"zh\":\"伦敦\",\"ar\":\"لندن\",\"av\":\"Лондон\",\"ja\":\"ロンドン\",\"ay\":\"London\",\"az\":\"London\",\"zu\":\"ILondon\",\"rm\":\"Londra\",\"ro\":\"Londra\",\"ba\":\"Лондон\",\"be\":\"Лондан\",\"ru\":\"Лондон\",\"bg\":\"Лондон\",\"bh\":\"लंदन\",\"bi\":\"London\",\"bm\":\"London\",\"bn\":\"লন্ডন\",\"jv\":\"London\",\"bo\":\"ལོན་ཊོན།\",\"sa\":\"लन्डन्\",\"br\":\"Londrez\",\"sc\":\"Londra\",\"bs\":\"London\",\"sd\":\"لنڊن\",\"se\":\"London\",\"sh\":\"London\",\"ka\":\"ლონდონი\",\"si\":\"ලන්ඩන්\",\"sk\":\"Londýn\",\"sl\":\"London\",\"sm\":\"Lonetona\",\"sn\":\"London\",\"so\":\"London\",\"ca\":\"Londres\",\"sq\":\"Londra\",\"sr\":\"Лондон\",\"kk\":\"Лондон\",\"kl\":\"London\",\"st\":\"London\",\"km\":\"ឡុងដ៍\",\"su\":\"London\",\"ce\":\"Лондон\",\"sv\":\"London\",\"kn\":\"ಲಂಡನ್\",\"ko\":\"런던\",\"sw\":\"London\",\"ku\":\"London\",\"kv\":\"Лондон\",\"kw\":\"Loundres\",\"co\":\"Londra\",\"ta\":\"இலண்டன்\",\"ky\":\"Лондон\",\"cs\":\"Londýn\",\"te\":\"లండన్\",\"cu\":\"Лондонъ\",\"tg\":\"Лондон\",\"cv\":\"Лондон\",\"th\":\"ลอนดอน\",\"cy\":\"Llundain\",\"lb\":\"London\",\"tk\":\"London\",\"tl\":\"Londres\",\"to\":\"Lonitoni\",\"li\":\"Londe\",\"da\":\"London\",\"tr\":\"Londra\",\"tt\":\"Лондон\",\"de\":\"London\",\"ln\":\"Lóndɛlɛ\",\"lo\":\"ລອນດອນ\",\"tw\":\"London\",\"lt\":\"Londonas\",\"lv\":\"Londona\",\"ug\":\"لوندۇن\",\"uk\":\"Лондон\",\"mg\":\"Lôndôna\",\"mi\":\"Rānana\",\"ur\":\"علاقہ لندن\",\"mk\":\"Лондон\",\"ml\":\"ലണ്ടൻ\",\"ee\":\"London\",\"mn\":\"Лондон\",\"uz\":\"London\",\"mr\":\"लंडन\",\"ms\":\"London\",\"el\":\"Λονδίνο\",\"mt\":\"Londra\",\"en\":\"London\",\"eo\":\"Londono\",\"my\":\"လန်ဒန်မြို့\",\"es\":\"Londres\",\"et\":\"London\",\"eu\":\"Londres\",\"na\":\"London\",\"vi\":\"Luân Đôn\",\"ne\":\"लन्डन\",\"vo\":\"London\",\"fa\":\"لندن\",\"nl\":\"Londen\",\"nn\":\"London\",\"ff\":\"London\",\"no\":\"London\",\"fi\":\"Lontoo\",\"fj\":\"Lodoni\",\"nv\":\"Tooh Dineʼé Bikin Haalʼá\",\"fo\":\"London\",\"wa\":\"Londe\",\"ny\":\"London\",\"fr\":\"Londres\",\"fy\":\"Londen\",\"oc\":\"Londres\",\"wo\":\"Londar\",\"ga\":\"Londain\",\"ascii\":\"London\",\"gd\":\"Lunnainn\",\"om\":\"Landan\",\"or\":\"ଲଣ୍ଡନ\",\"os\":\"Лондон\",\"gl\":\"Londres\",\"gn\":\"Lóndyre\",\"gu\":\"લંડન\",\"gv\":\"Lunnin\",\"pa\":\"ਲੰਡਨ\",\"ha\":\"Landan\",\"pl\":\"Londyn\",\"he\":\"לונדון\"}"
                          )
            ),
            51.51,
            -0.13,
            "GB",
            "England"
    );
    public static final CityDTO[] CITIES_DTO = {MOSCOW_DTO, NOGINSK_DTO, LONDON_DTO};
    public static final WhetherMain WHETHER_MAIN = new WhetherMain(
            new BigDecimal("-4.73"),
            new BigDecimal("-10.55"),
            new BigDecimal("-5.68"),
            new BigDecimal("-4.71"),
            new BigDecimal("1038"),
            new BigDecimal("83")
    );
    public static final WhetherWind WHETHER_WIND = new WhetherWind(
            new BigDecimal("4.59"),
            new BigDecimal("133")
    );
    public static final WhetherSunshine WHETHER_SUNSHINE = new WhetherSunshine(1733463780L, 1733489918L);
    public static final WhetherDTO WHETHER_DTO = new WhetherDTO(
            WHETHER_MAIN,
            new BigDecimal("1000"),
            WHETHER_WIND,
            WHETHER_SUNSHINE
    );
    public static final Whether WHETHER = new Whether(
            WHETHER_MAIN.getTemp(),
            WHETHER_MAIN.getFeels_like(),
            WHETHER_MAIN.getTemp_min(),
            WHETHER_MAIN.getTemp_max(),
            WHETHER_MAIN.getPressure().multiply(BigDecimal.valueOf(0.7500615758456601)),
            WHETHER_MAIN.getHumidity(),
            WHETHER_DTO.getVisibility(),
            WHETHER_WIND.getSpeed(),
            WHETHER_WIND.getDeg(),
            new Time(WHETHER_SUNSHINE.getSunrise() * 1000),
            new Time(WHETHER_SUNSHINE.getSunset() * 1000)
    );
    //
    public static final Reader READER_UNSUPPORTED = new Reader(4, "4th name with unsupported length, that will be ignored", "4th second name", "4th surname", 444, new ArrayList<>());
    public static final Path BOOK_IMAGE_PATH_1 = Path.of("src/test/resources/testImages/1.jpg");
    public static final Path BOOK_IMAGE_PATH_2 = Path.of("src/test/resources/testImages/2.jpg");
    public static final Path BOOK_IMAGE_PATH_3 = Path.of("src/test/resources/testImages/3.jpg");
}