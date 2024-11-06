package lorgar.avrelian.javaconspectrus.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Включает поддержку Spring Security
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // стандартные настройки цепочки безопасности
        return http
                // разрешить доступ только для аутентифицированных пользователей
                .authorizeHttpRequests(
                        requests -> requests.anyRequest().authenticated()
                                      )
                // включить поддержку формы входа
                .formLogin(Customizer.withDefaults())
                // включить поддержку Basic-аутентификации
                .httpBasic(Customizer.withDefaults())
                // собрать цепочку фильтров
                .build();
    }
}
