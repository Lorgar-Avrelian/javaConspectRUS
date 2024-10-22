package lorgar.avrelian.javaconspectrus.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class TelegramConfig {
    @Value("${telegram.bot.name}")
    String name;
    @Value("${telegram.bot.token}")
    String token;

    public TelegramConfig() {
    }

    public TelegramConfig(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
