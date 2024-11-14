package lorgar.avrelian.javaconspectrus.configurations;

import lorgar.avrelian.javaconspectrus.securityFilters.BasicAuthCorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

// Включает поддержку Spring Security
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // репозиторий CSRF-токена
        CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        // стандартные настройки цепочки безопасности
        return http
                // настройка CSRF
                .csrf(csrf -> csrf
                        // обработчик запроса, обрабатываемого в целях защиты от CSRF-атаки:
                        // - по умолчанию используется XorCsrfTokenRequestAttributeHandler,
                        // шифрующий токен для каждого запроса
                        // - также может использоваться CsrfTokenRequestAttributeHandler,
                        // который не использует шифрование
                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                        // репозиторий CSRF-токена:
                        // - по умолчанию используется HttpSessionCsrfTokenRepository,
                        // сохраняющий CSRF-токен в параметрах HTTP-сессии
                        // - также может использоваться CookieCsrfTokenRepository,
                        // сохраняющий CSRF-токен в файлах cookie браузера
                        .csrfTokenRepository(csrfTokenRepository)
                        // компонент, позволяющий выполнять какие-либо
                        // действия после успешной аутентификации:
                        // по умолчанию используется CsrfAuthenticationStrategy,
                        // изменяющая CSRF-токен после успешной аутентификации
                        .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository))
                        // настройка адресов, по которым НЕ должна
                        // осуществляться защита от CSRF-атак
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/random", "/whether/**"))
                        // настройка адресов, по которым ОБЯЗАТЕЛЬНО должна
                        // осуществляться защита от CSRF-атак
                        .requireCsrfProtectionMatcher(new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON))
                     )
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
