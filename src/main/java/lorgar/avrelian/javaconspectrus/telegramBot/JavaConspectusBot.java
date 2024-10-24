package lorgar.avrelian.javaconspectrus.telegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                logger.error("Error sending message to user {} : {}", update.getMessage().getChat().getUserName(), e.getMessage());
            }
        }
    }
}
