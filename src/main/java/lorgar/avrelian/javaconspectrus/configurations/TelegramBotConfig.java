package lorgar.avrelian.javaconspectrus.configurations;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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
