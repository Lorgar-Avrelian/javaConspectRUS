package lorgar.avrelian.javaconspectrus.telegramBot;

import lorgar.avrelian.javaconspectrus.services.BookCoverService;
import lorgar.avrelian.javaconspectrus.services.BookService;
import lorgar.avrelian.javaconspectrus.services.ReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
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

    public JavaConspectusBot(@Value("${telegram.bot.token}") String botToken,
                             @Qualifier("bookServiceImplDB") BookService bookService,
                             @Qualifier("readerServiceImpl") ReaderService readerService,
                             @Qualifier("bookCoverServiceImpl") BookCoverService bookCoverService) {
        super(botToken);
        this.bookService = bookService;
        this.readerService = readerService;
        this.bookCoverService = bookCoverService;
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
            List<BotCommand> botCommands = new ArrayList<>(List.of(
                    new BotCommand("/start", "Начать"),
                    new BotCommand("/books", "Книги"),
                    new BotCommand("/readers", "Читатели")
                                                                  ));
            SetMyCommands setMyCommands = new SetMyCommands();
            setMyCommands.setCommands(botCommands);
            this.sendApiMethod(setMyCommands);
        } catch (TelegramApiException e) {
            logger.error("Bot menu is not created: {}", e.getMessage());
        }
    }

    // Метод, формирующий ответ бота с помощью стандартного метода (класса) телеграм-бота (BotApiMethod):
    // SendMessage
    private void botAnswer(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            switch (message) {
                case "/start" -> {
                    String userName = update.getMessage().getFrom().getFirstName();
                    if (userName.isEmpty()) {
                        userName = update.getMessage().getFrom().getLastName();
                    }
                    if (userName.isEmpty()) {
                        userName = update.getMessage().getFrom().getUserName();
                    }
                    sendMessage.setText("Hi, " + userName + "!");
                }
                default -> sendMessage.setText(message);
            }
            try {
                this.sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Error sending message to user {} : {}", update.getMessage().getChat().getUserName(), e.getMessage());
            }
        }
    }
}
