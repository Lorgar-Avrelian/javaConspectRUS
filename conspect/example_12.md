## Пример 1:

> [[_оглавление_]](../README.md/#111-telegram)

> [**[11.1 Telegram]**](/conspect/11.md/#111-telegram)

- добавление токена и названия бота в файл _application.properties_:

```properties
# Application properties
spring.application.name=Java conspectus RUS
server.port=8080
# Specific parameter for RandomizeConfig class
rand.diapazon=1000
# Specific parameter for BookCoverService implementations
books.covers.dir.path=books/covers
# Specific parameter for WhetherService implementations
whether.current.url=https://api.openweathermap.org/data/2.5/weather
whether.geo.url=http://api.openweathermap.org/geo/1.0/direct
whether.geo.result.count=10
whether.api.key=c2bdc9ea4264d25b8b06326de0ea9591
# Spring Data JPA parameters for DB connection
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=library_user
spring.datasource.password=123
# Hibernate settings
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=validate
# Settings of showing SQL-requests Spring Data JPA
spring.jpa.show-sql=false
# Liquibase change-log settings
spring.liquibase.change-log=classpath:liquibase/changelog-master.yml
# Telegram bot token
telegram.bot.name=conspectus_RUS_bot
telegram.bot.token=8069507794:AAHfv855XmPqiLPUwdmTTPbGJ8HEwjSPrkU
```

- добавление зависимости в файл _pom.xml_:

```xml

<dependency>
    <groupId>org.telegram</groupId>
    <artifactId>telegrambots</artifactId>
    <version>6.9.7.1</version>
</dependency>
```

- добавление конфигурации телеграм-бота:

```java

@Configuration
// Конфигурационный файл для bean-компонента с названием "javaConspectusBot" (телеграм-бота)
public class TelegramBotConfig {
    // Добавляем логгер к конфигурации телеграм-бота
    private Logger logger = LoggerFactory.getLogger(TelegramBotConfig.class);
    // Внедряем телеграм-бота в конфигурацию
    private final TelegramBot bot;

    // Телеграм-бот в приложении может быть не один, поэтому в конструкторе необходимо определить наименование
    // конкретного бота
    public TelegramBotConfig(@Qualifier("javaConspectusBot") TelegramBot bot) {
        this.bot = bot;
    }

    @PostConstruct
    // Инициализация телеграм-ботов.
    // В случае, если в приложении не один бот, то должны быть проинициализированы все
    public void init() {
        // Создание и инициализация API Telegram
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            logger.error("TelegramBotsApi threw exception: {}", e.getMessage());
        }
        // Регистрация телеграм-бота на сервере Telegram (вход в Telegram)
        if (telegramBotsApi != null) {
            try {
                telegramBotsApi.registerBot((LongPollingBot) bot);
            } catch (TelegramApiException e) {
                logger.error("TelegramBot is not registered: {}", e.getMessage());
            }
        }
    }
}
```

- создание телеграм-бота и добавление ему функциональности:

```java

@Component
// Bean-компонент с названием "javaConspectusBot" - телеграм-бот
public class JavaConspectusBot extends TelegramLongPollingBot {
    // Добавляем логгер к телеграм-боту
    private Logger logger = LoggerFactory.getLogger(JavaConspectusBot.class);
    // Внедряем имя телеграм-бота
    @Value("${telegram.bot.name}")
    private String name;
    // Внедряем токен для телеграм-бота
    @Value("${telegram.bot.token}")
    private String token;
    private final BookService bookService;
    private final ReaderService readerService;
    private final BookCoverService bookCoverService;
    private final ManageService manageService;
    private final static String BOOKS = "/books/";
    private final static String READERS = "/readers/";
    private static final HashMap<Long, Long> giveMap = new HashMap<>();
    private static final HashMap<Long, Long> takeMap = new HashMap<>();
    @Value("${books.covers.dir.path}")
    private String coversDir;
    private static final Set<Long> bookAdd = new HashSet<>();
    private static final Map<Path, Book> bookCovers = new HashMap<>();

    public JavaConspectusBot(@Value("${telegram.bot.token}") String botToken,
                             @Qualifier("bookServiceImplDB") BookService bookService,
                             @Qualifier("readerServiceImpl") ReaderService readerService,
                             @Qualifier("bookCoverServiceImpl") BookCoverService bookCoverService,
                             @Qualifier("manageServiceImpl") ManageService manageService) {
        super(botToken);
        this.bookService = bookService;
        this.readerService = readerService;
        this.bookCoverService = bookCoverService;
        this.manageService = manageService;
    }

    // ОБЯЗАТЕЛЬНО!!!
    // переопределяем метод getBotUsername() - указываем логин от телеграм-бота
    @Override
    public String getBotUsername() {
        return name;
    }

    // ОБЯЗАТЕЛЬНО!!!
    // переопределяем метод getBotToken() - указываем токен для телеграм-бота
    @Override
    public String getBotToken() {
        return token;
    }

    // ОБЯЗАТЕЛЬНО!!!
    // переопределяем метод onUpdateReceived() - метод определяет ВСЮ функциональность телеграм-бота
    // и принимает на вход входящее телеграм-сообщение класса Update
    @Override
    public void onUpdateReceived(Update update) {
        // Метод, создающий меню бота с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
        // SetMyCommands
        botMenu();
        // Метод, формирующий ответ бота, добавляющий подстрочное меню и кнопки управления с помощью
        // стандартного метода (класса) телеграм-бота (BotApiMethod): SendMessage
        botAnswer(update);
    }

    // Метод, создающий меню бота с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
    // SetMyCommands
    private void botMenu() {
        try {
            // Создание списка команд для меню бота
            List<BotCommand> botCommands = new ArrayList<>(List.of(
                    new BotCommand("/start", "Приветствие"),
                    new BotCommand("/books", "Список книг в наличии"),
                    new BotCommand("/readers", "Список зарегистрированных читателей")
                                                                  ));
            // Создание сущности меню бота SetMyCommands
            SetMyCommands setMyCommands = new SetMyCommands();
            // Изменение перечня команд в меню бота SetMyCommands
            setMyCommands.setCommands(botCommands);
            // Отправка меню в чат пользователя
            this.sendApiMethod(setMyCommands);
        } catch (TelegramApiException e) {
            logger.error("Bot menu is not created: {}", e.getMessage());
        }
    }

    // Метод, формирующий ответ бота, добавляющий подстрочное меню и кнопки управления с помощью
    // стандартного метода (класса) телеграм-бота (BotApiMethod): SendMessage
    private void botAnswer(Update update) {
        // Метод, обрабатывающий ответы на ручные команды и добавляющий подстрочное меню
        commandsAnswer(update);
        // Метод, обрабатывающий ответы на команды, получаемые при нажатии кнопок
        buttonCallbackAnswer(update);
    }

    // Метод, обрабатывающий ответы на ручные команды и добавляющий подстрочное меню
    private void commandsAnswer(Update update) {
        // Проверка наличия текста сообщения
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Получение текста сообщения
            String message = update.getMessage().getText();
            // Получение ID чата
            long chatId = update.getMessage().getChatId();
            // Создание сущности ответа SendMessage
            SendMessage sendMessage = new SendMessage();
            // Указание ID чата SendMessage
            sendMessage.setChatId(chatId);
            // Формирование содержания ответа SendMessage
            switch (message) {
                // Формирование приветствия при получении команды /start
                case "/start" -> {
                    commandStart(update, sendMessage);
                    // Метод, создающий кнопки управления для чата с помощью стандартного метода (класса)
                    // телеграм-бота (BotApiMethod): SendMessage
                    botMarkup(update, sendMessage);
                }
                // Формирование списка кнопок с книгами при получении команды /books
                case "/books" -> commandBooks(sendMessage);
                // Формирование списка кнопок с читателями при получении команды /readers
                case "/readers" -> commandReaders(sendMessage);
                // Запуск алгоритма выдачи книги
                case "Выдать" -> commandGive(update, sendMessage);
                // Запуск алгоритма приёма книги
                case "Принять" -> commandTake(update, sendMessage);
                // Информационное сообщение
                case "Управление" -> sendMessage.setText("Используйте команды управления 'Принять' или 'Выдать'!");
                // Запуск алгоритма добавления новой книги
                case "Добавить книгу" -> bookAddInit(chatId, sendMessage);
                // Анализ вводимых сообщений и действия по умолчанию
                default -> analyzeTextMessage(chatId, message, sendMessage);
            }
            // Отправка ответа SendMessage
            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Error sending message to user {} : {}", update.getMessage().getChat().getUserName(), e.getMessage());
            }
        }
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            // Создание сущности ответа SendMessage
            SendMessage sendMessage = new SendMessage();
            // Анализ присылаемых фотографий, их загрузка и действия по умолчанию
            Book book = analyzePhotoMessage(update, sendMessage);
            if (book == null) {
                sendMessage.setText("Ошибка при загрузке обложки книги!");
            }
            // Отправка ответа SendMessage
            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Error sending message to user {} : {}", update.getMessage().getChat().getUserName(), e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 1000)
    private void generateBookCover() {
        for (Map.Entry<Path, Book> pathBookEntry : bookCovers.entrySet()) {
            byte[] fileInBytes;
            try {
                fileInBytes = Files.readAllBytes(pathBookEntry.getKey());
            } catch (IOException e) {
                break;
            }
            if (fileInBytes.length != 0) {
                byte[] previewBytes;
                try (
                        InputStream inputStream = Files.newInputStream(pathBookEntry.getKey());
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
                ) {
                    BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                    if (bufferedImage == null) {
                        break;
                    }
                    int height = bufferedImage.getHeight();
                    int width = bufferedImage.getWidth();
                    BufferedImage preview;
                    if (height > width) {
                        height = height / (width / 200);
                        preview = new BufferedImage(200, height, bufferedImage.getType());
                        width = 200;
                    } else {
                        width = width / (height / 200);
                        preview = new BufferedImage(width, 200, bufferedImage.getType());
                        height = 200;
                    }
                    Graphics2D graphics2D = preview.createGraphics();
                    graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
                    graphics2D.dispose();
                    ImageIO.write(preview, getExtension(pathBookEntry.getKey().getFileName().toString()), byteArrayOutputStream);
                    previewBytes = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    return;
                }
                BookCover bookCover;
                bookCover = bookCoverService.getBookCover(pathBookEntry.getValue().getId());
                bookCover.setId(pathBookEntry.getValue().getId());
                bookCover.setFilePath(pathBookEntry.getKey().toString());
                try {
                    bookCover.setFileSize((int) Files.size(pathBookEntry.getKey()));
                    bookCover.setMediaType(Files.probeContentType(pathBookEntry.getKey()));
                } catch (IOException e) {
                    bookService.deleteBook(pathBookEntry.getValue().getId());
                    return;
                }
                bookCover.setBook(pathBookEntry.getValue());
                if (previewBytes.length != 0) {
                    bookCover.setImagePreview(previewBytes);
                } else {
                    bookService.deleteBook(pathBookEntry.getValue().getId());
                    return;
                }
                bookCoverService.saveBookCover(bookCover);
                bookCover = bookCoverService.getBookCover(pathBookEntry.getValue().getId());
                if (bookCover == null) {
                    bookCoverService.deleteBookCover(pathBookEntry.getValue().getId());
                    bookService.deleteBook(pathBookEntry.getValue().getId());
                    try {
                        Files.deleteIfExists(pathBookEntry.getKey());
                    } catch (IOException e) {
                        logger.error("IOException when deleting file", e);
                    }
                }
                bookCovers.remove(pathBookEntry.getKey());
            }
        }
    }

    private Book analyzePhotoMessage(Update update, SendMessage sendMessage) {
        // Получение ID чата
        long chatId = update.getMessage().getChatId();
        // Получение текста сообщения из сообщения с изображением
        String message = update.getMessage().getCaption();
        // Изменение ID чата для ответного сообщения бота
        sendMessage.setChatId(chatId);
        // Проверка наличия инициализации добавления новой сущности, а также того,
        // чтобы сообщение под изображением было не пустым
        if (!bookAdd.contains(chatId) || message == null || message.isEmpty()) {
            bookAdd.remove(chatId);
            return null;
        }
        // Проверка формата текста под изображением,
        // создание сущности книги на основании присланного текста
        String[] split = message.split(", ");
        if (split.length != 3) {
            bookAddInit(chatId, sendMessage);
            return null;
        }
        short year;
        String title = split[0];
        String author = split[1];
        try {
            year = Short.parseShort(split[2]);
        } catch (NumberFormatException e) {
            bookAdd.remove(chatId);
            return null;
        }
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        // Сохранение новой сущности книги
        book = bookService.createBook(book);
        //
        // Получение версий изображения обложки книги из сообщения
        List<PhotoSize> photos = update.getMessage().getPhoto();
        // Получение версии изображения обложки книги из сообщения в максимальном разрешении
        PhotoSize photoSize = photos.stream()
                                    .max(Comparator.comparing(PhotoSize::getFileSize))
                                    .get();
        // Получение файла изображения из сообщения
        GetFile getFile = new GetFile(photoSize.getFileId());
        // Проверка разрешения файла - не менее 200 пикселей по высоте и ширине
        if (photoSize.getHeight() <= 200 || photoSize.getWidth() <= 200) {
            bookService.deleteBook(book.getId());
            bookAdd.remove(chatId);
            return null;
        }
        // Инициализация загрузки файла
        org.telegram.telegrambots.meta.api.objects.File file;
        String extension;
        Path filePath;
        try {
            // Получение файла Telegram
            file = this.execute(getFile);
            // Получение расширения файла Telegram при помощи статического метода getExtension()
            extension = getExtension(file.getFilePath());
            // Создание пути сохранения файла в системе
            filePath = Path.of(coversDir, book.getId() + "." + extension);
            Files.deleteIfExists(filePath);
            // Загрузка файла
            this.downloadFile(file, new File(filePath.toString()));
        } catch (TelegramApiException | IOException e) {
            bookService.deleteBook(book.getId());
            bookAdd.remove(chatId);
            logger.error("Error creating new file: {}", e.getMessage());
            return null;
        }
        // Изменение текста сообщения
        sendMessage.setText("Книга успешно добавлена!");
        // Деинициализация процесса добавления книги
        bookAdd.remove(chatId);
        // Включение файла обложки книги в список для генерации сущностей обложек
        bookCovers.put(filePath, book);
        // Возврат созданной книги
        return book;
    }

    private void commandTake(Update update, SendMessage sendMessage) {
        long chatId = giveTakeBookStart(update, sendMessage);
        takeMap.put(chatId, null);
    }

    private void commandGive(Update update, SendMessage sendMessage) {
        long chatId = giveTakeBookStart(update, sendMessage);
        giveMap.put(chatId, null);
    }

    private static long giveTakeBookStart(Update update, SendMessage sendMessage) {
        long chatId = update.getMessage().getChatId();
        sendMessage.setText("Введите ID книги");
        return chatId;
    }

    private static void bookAddInit(long chatId, SendMessage sendMessage) {
        bookAdd.add(chatId);
        sendMessage.setText("Введите данные о книге в формате:\n[Название книги] , [Автор книги] , [Год издания]\n Также приложите фото обложки книги!");
    }

    // Метод, обрабатывающий ответы на команды, получаемые при нажатии кнопок
    private void buttonCallbackAnswer(Update update) {
        // Проверка наличия ответной команды какой-либо из кнопок
        if (update.hasCallbackQuery()) {
            // Получение ответной команды кнопки
            String callbackData = update.getCallbackQuery().getData();
            // Получение ID чата
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            // Проверка содержания команды
            if (callbackData.contains(BOOKS)) {
                // Отправка фото в ответном сообщении
                SendPhoto sendPhoto = new SendPhoto();
                // Получение ID книги
                callbackData = callbackData.substring(BOOKS.length());
                long bookId = Long.valueOf(callbackData);
                // Получение книги и обложки к ней
                Book book = bookService.findBook(bookId);
                BookCover bookCover = bookCoverService.getBookCover(bookId);
                // Указание ID чата
                sendPhoto.setChatId(chatId);
                // Добавление текста к фото
                sendPhoto.setCaption(book.getTitle() + " - " + book.getAuthor() + " - ID: " + book.getId());
                // Прикрепление фото к сообщению (выбрана отправка напрямую),
                // можно отправлять preview, используя new InputFile(new ByteArrayInputStream(bookCover.getImagePreview()), book.getTitle());
                InputFile inputFile = new InputFile(new File(bookCover.getFilePath()));
                sendPhoto.setPhoto(inputFile);
                // Отправка ответа SendPhoto
                try {
                    this.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    logger.error("Error sending photo to user {} : {}", update.getCallbackQuery().getFrom().getUserName(), e.getMessage());
                }
            }
            // Проверка содержания команды
            if (callbackData.contains(READERS)) {
                // Отправка текста в ответном сообщении
                SendMessage sendMessage = new SendMessage();
                callbackData = callbackData.substring(READERS.length());
                long readerId = Long.valueOf(callbackData);
                Reader reader = readerService.findReader(readerId);
                sendMessage.setChatId(chatId);
                sendMessage.setText(
                        "ID: " + reader.getId() + "\n" +
                                "Фамилия: " + reader.getSurname() + "\n" +
                                "Имя: " + reader.getName() + "\n" +
                                "Отчество: " + reader.getSecondName() + "\n" +
                                "Номер читательского билета: " + reader.getPersonalNumber()
                                   );
                // Создание текстовых кнопок ответного сообщения SendMessage
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                // Создание рядов кнопок
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                // Получение исходных данных
                Collection<Book> books = readerService.getReaderBooks(readerId);
                // Формирование логики
                for (Book book : books) {
                    // Создание ряда кнопок
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    // Создание текстовой кнопки
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    // Определение текста кнопки
                    inlineKeyboardButton.setText(book.getTitle() + " - " + book.getAuthor() + " - ID: " + book.getId());
                    // Определение значения ответного запроса CallbackData при нажатии кнопки
                    inlineKeyboardButton.setCallbackData(BOOKS + book.getId());
                    // Добавление кнопки в ряд
                    row.add(inlineKeyboardButton);
                    // Добавление ряда к списку рядов
                    rows.add(row);
                }
                // Добавление списка рядов кнопок в текстовые кнопки
                inlineKeyboardMarkup.setKeyboard(rows);
                // Добавление текстовых кнопок в сообщение
                sendMessage.setReplyMarkup(inlineKeyboardMarkup);
                // Отправка ответа SendMessage
                try {
                    this.sendApiMethod(sendMessage);
                } catch (TelegramApiException e) {
                    logger.error("Error sending message to user {} : {}", update.getCallbackQuery().getFrom().getUserName(), e.getMessage());
                }
            }
        }
    }

    // Метод, создающий кнопки управления для чата с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
    // SendMessage
    private void botMarkup(Update update, SendMessage sendMessage) {
        long chatId = update.getMessage().getChatId();
        sendMessage.setChatId(chatId);
        // Создание подстрочного меню бота
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        // Создание рядов подстрочного меню
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        // Создание 1-го ряда подстрочного меню
        KeyboardRow keyboardRow = new KeyboardRow();
        // Добавление кнопки управления в 0-ую позицию 1-го ряда подстрочного меню
        keyboardRow.add(0, "Управление");
        // Добавление 1-го ряда подстрочного меню в подстрочное меню
        keyboardRows.add(0, keyboardRow);
        // Создание 2-го ряда подстрочного меню
        keyboardRow = new KeyboardRow();
        // Добавление кнопки управления в 0-ую и 1-ую позиции 2-го ряда подстрочного меню
        keyboardRow.add(0, "Выдать");
        keyboardRow.add(1, "Принять");
        // Добавление 2-го ряда подстрочного меню в подстрочное меню
        keyboardRows.add(1, keyboardRow);
        // Создание 3-го ряда подстрочного меню
        keyboardRow = new KeyboardRow();
        // Добавление кнопки управления в 0-ую позицию 3-го ряда подстрочного меню
        keyboardRow.add(0, "Добавить книгу");
        // Добавление 3-го ряда подстрочного меню в подстрочное меню
        keyboardRows.add(2, keyboardRow);
        // Добавление рядов подстрочного меню в подстрочное меню
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        // Настройка автоматического выравнивания подстрочного меню
        replyKeyboardMarkup.setResizeKeyboard(true);
        // Добавление подстрочного меню к сущности ответа SendMessage
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

    // Метод, формирующий приветствие при получении команды /start
    private static void commandStart(Update update, SendMessage sendMessage) {
        // Получение имени пользователя из имени, фамилии или названия его аккаунта
        String userName = update.getMessage().getFrom().getFirstName();
        if (userName == null || userName.isBlank()) {
            userName = update.getMessage().getFrom().getLastName();
        }
        if (userName == null || userName.isBlank()) {
            userName = update.getMessage().getFrom().getUserName();
        }
        // Формирование текста приветствия
        sendMessage.setText("Привет, " + userName + "!");
    }

    // Метод, формирующий приветствие при получении команды /books
    private void commandBooks(SendMessage sendMessage) {
        // Создание текстовых кнопок ответного сообщения SendMessage
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // Создание рядов кнопок
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        // Получение исходных данных
        List<Book> books = bookService.getAllBooks().stream()
                                      .filter(book -> book.getReader() == null)
                                      .toList();
        // Формирование логики
        for (Book book : books) {
            // Создание ряда кнопок
            List<InlineKeyboardButton> row = new ArrayList<>();
            // Создание текстовой кнопки
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            // Определение текста кнопки
            inlineKeyboardButton.setText(book.getTitle() + " - " + book.getAuthor() + " - ID: " + book.getId());
            // Определение значения ответного запроса CallbackData при нажатии кнопки
            inlineKeyboardButton.setCallbackData(BOOKS + book.getId());
            // Добавление кнопки в ряд
            row.add(inlineKeyboardButton);
            // Добавление ряда к списку рядов
            rows.add(row);
        }
        // Добавление списка рядов кнопок в текстовые кнопки
        inlineKeyboardMarkup.setKeyboard(rows);
        // Добавление текстовых кнопок в сообщение
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        // Изменение текста сообщения
        sendMessage.setText("Список книг в наличии");
    }

    // Метод, формирующий приветствие при получении команды /readers
    private void commandReaders(SendMessage sendMessage) {
        // Создание текстовых кнопок ответного сообщения SendMessage
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        // Создание рядов кнопок
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        // Получение исходных данных
        Collection<Reader> readers = readerService.getAllReaders();
        // Формирование логики
        for (Reader reader : readers) {
            // Создание ряда кнопок
            List<InlineKeyboardButton> row = new ArrayList<>();
            // Создание текстовой кнопки
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            // Определение текста кнопки
            inlineKeyboardButton.setText(reader.getName() + " " + reader.getSecondName() + " " + reader.getSurname());
            // Определение значения ответного запроса CallbackData при нажатии кнопки
            inlineKeyboardButton.setCallbackData(READERS + reader.getId());
            // Добавление кнопки в ряд
            row.add(inlineKeyboardButton);
            // Добавление ряда к списку рядов
            rows.add(row);
        }
        // Добавление списка рядов кнопок в текстовые кнопки
        inlineKeyboardMarkup.setKeyboard(rows);
        // Добавление текстовых кнопок в сообщение
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        // Изменение текста сообщения
        sendMessage.setText("Список зарегистрированных читателей");
    }

    private void analyzeTextMessage(long chatId, String message, SendMessage sendMessage) {
        long bookId;
        long readerId;
        if (takeMap.containsKey(chatId) || giveMap.containsKey(chatId) && !bookAdd.contains(chatId)) {
            if (takeMap.get(chatId) == null && giveMap.get(chatId) == null) {
                try {
                    bookId = Long.parseLong(message);
                } catch (NumberFormatException e) {
                    giveMap.remove(chatId);
                    takeMap.remove(chatId);
                    sendMessage.setText("Команда не распознана!");
                    return;
                }
                Book book = bookService.findBook(bookId);
                if (book != null) {
                    if (giveMap.containsKey(chatId)) {
                        giveMap.put(chatId, bookId);
                    }
                    if (takeMap.containsKey(chatId)) {
                        takeMap.put(chatId, bookId);
                    }
                    sendMessage.setText("Введите ID читателя!");
                } else {
                    giveMap.remove(chatId);
                    takeMap.remove(chatId);
                    sendMessage.setText("Такой книги нет в наличии!");
                }
            } else if (takeMap.get(chatId) != null) {
                try {
                    readerId = Long.parseLong(message);
                } catch (NumberFormatException e) {
                    giveMap.remove(chatId);
                    takeMap.remove(chatId);
                    sendMessage.setText("Команда не распознана!");
                    return;
                }
                bookId = takeMap.get(chatId);
                Book book = bookService.findBook(takeMap.get(chatId));
                Reader reader = readerService.findReader(readerId);
                if (reader != null && book.getReader().equals(reader)) {
                    manageService.takeBookFromReader(bookId, readerId);
                    sendMessage.setText("Книга с ID " + bookId + " получена от читателя с ID " + readerId);
                } else if (reader == null) {
                    sendMessage.setText("Такого читателя не зарегистрировано!");
                } else {
                    sendMessage.setText("Книга находится у другого читателя!");
                }
                takeMap.remove(chatId);
            } else if (giveMap.get(chatId) != null) {
                try {
                    readerId = Long.parseLong(message);
                } catch (NumberFormatException e) {
                    giveMap.remove(chatId);
                    takeMap.remove(chatId);
                    sendMessage.setText("Команда не распознана!");
                    return;
                }
                bookId = giveMap.get(chatId);
                Book book = bookService.findBook(giveMap.get(chatId));
                Reader reader = readerService.findReader(readerId);
                if (reader != null && book.getReader() == null) {
                    manageService.giveBookToReader(bookId, readerId);
                    sendMessage.setText("Книга с ID " + bookId + " выдана читателю с ID " + readerId);
                } else if (reader == null) {
                    sendMessage.setText("Такого читателя не зарегистрировано!");
                } else {
                    sendMessage.setText("Книга находится у другого читателя!");
                }
                giveMap.remove(chatId);
            } else {
                sendMessage.setText("Команда не распознана!");
            }
        } else if (bookAdd.contains(chatId)) {
            bookAddInit(chatId, sendMessage);
        } else {
            sendMessage.setText("Команда не распознана!");
        }
    }

    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
```