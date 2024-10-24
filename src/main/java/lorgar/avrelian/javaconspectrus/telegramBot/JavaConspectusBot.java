package lorgar.avrelian.javaconspectrus.telegramBot;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.BookCover;
import lorgar.avrelian.javaconspectrus.models.Reader;
import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import lorgar.avrelian.javaconspectrus.services.ManageService;
import lorgar.avrelian.javaconspectrus.services.ReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        // Метод, формирующий ответ бота с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
        // SendMessage
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

    // Метод, формирующий ответ бота с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
    // SendMessage
    private void botAnswer(Update update) {
        // Метод, обрабатывающий ответы на ручные команды
        commandsAnswer(update);
        // Метод, обрабатывающий ответы на команды, получаемые при нажатии кнопок
        buttonCallbackAnswer(update);
    }

    // Метод, обрабатывающий ответы на ручные команды
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
                case "Выдать" -> {
                    commandGive(update, sendMessage);
                }
                case "Принять" -> {
                    commandTake(update, sendMessage);
                }
                default -> sendMessage.setText("Команда не распознана");
            }
            // Отправка ответа SendMessage
            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Error sending message to user {} : {}", update.getMessage().getChat().getUserName(), e.getMessage());
            }
        }
    }

    private void commandTake(Update update, SendMessage sendMessage) {
    }

    private void commandGive(Update update, SendMessage sendMessage) {
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
                // Прикрепление фото к сообщению (выбрано preview для ускорения отправки),
                // можно отправлять файл напрямую, используя new InputFile(new File(Path.of(bookCover.getFilePath())));
                InputFile inputFile = new InputFile(new ByteArrayInputStream(bookCover.getImagePreview()), book.getTitle());
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
}
