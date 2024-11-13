package lorgar.avrelian.javaconspectrus.configurations;

import lorgar.avrelian.javaconspectrus.securityFilters.BasicAuthCorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Включает поддержку Spring Security
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // стандартные настройки цепочки безопасности
        return http
                // отключение CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // разрешить доступ только для аутентифицированных пользователей
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers("/error").permitAll()
                                .anyRequest().authenticated()
                                      )
                // включить поддержку формы входа
                .formLogin(Customizer.withDefaults())
                // включить поддержку Basic-аутентификации
                .httpBasic(Customizer.withDefaults())
                // включить вывод в терминал текста ошибки,
                // возникшей при выполнении обработки запроса
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        // вывод ошибок доступа
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {
                                    accessDeniedException.printStackTrace();
                                    response.sendError(HttpStatus.FORBIDDEN.value());
                                    return;
                                }
                                            )
                        // вывод ошибок аутентификации
                        .authenticationEntryPoint(
                                (request, response, authenticationException) -> {
                                    authenticationException.printStackTrace();
                                    response.sendError(403);
                                    return;
                                }
                                                 ))
                // Включение созданного фильтра BasicAuthCorsFilter в цепочку
                // фильтров перед фильтром UsernamePasswordAuthenticationFilter
                .addFilterBefore(new BasicAuthCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                // собрать цепочку фильтров
                .build();
    }
}
