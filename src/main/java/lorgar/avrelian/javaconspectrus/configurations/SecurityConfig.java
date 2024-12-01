package lorgar.avrelian.javaconspectrus.configurations;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.DispatcherType;
import lorgar.avrelian.javaconspectrus.securityFilters.BasicAuthCorsFilter;
import lorgar.avrelian.javaconspectrus.securityFilters.CSRFTokenFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
// Включает поддержку Spring Security
@EnableWebSecurity
// Включает поддержку Swagger UI для Spring Security
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier(value = "jpaUserDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // репозиторий CSRF-токена
        CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        // стандартные настройки цепочки безопасности
        return http
                // включение поддержки базовой аутентификации
                .httpBasic(Customizer.withDefaults())
                // отключение стандартной формы аутентификации
                .formLogin(AbstractHttpConfigurer::disable)
                // настройка формы выхода
                .logout(logout -> logout
                        // URL для выхода
                        .logoutUrl("/logout")
                        // адрес перенаправления после успешного выхода
                        .logoutSuccessUrl("/")
                        // удаление аутентификационных данных
                        .clearAuthentication(true)
                        // очистка сессионных данных
                        .invalidateHttpSession(true)
                        // очистка данных файлов Cookies
                        .addLogoutHandler(
                                new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(
                                        ClearSiteDataHeaderWriter.Directive.CACHE,
                                        ClearSiteDataHeaderWriter.Directive.COOKIES,
                                        ClearSiteDataHeaderWriter.Directive.STORAGE
                                ))
                                         ))
                // настройка авторизации
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        // разрешить доступ при перенаправлении
                                        .dispatcherTypeMatchers(
                                                DispatcherType.ERROR,
                                                DispatcherType.FORWARD
                                                               )
                                        .permitAll()
                                        // точки доступа для
                                        // всех пользователей
                                        .requestMatchers(
                                                // HelloController
                                                "/",
                                                // AuthorizationController
                                                "/login*",
                                                "/csrf",
                                                // стандартный адрес вывода сообщений об ошибке
                                                "/error*",
                                                // стандартные адреса разметки HTML-страниц
                                                "/css/**",
                                                "/js/**",
                                                // адреса Swagger UI
                                                "/swagger-resources/**",
                                                "/swagger-ui/**",
                                                "/v3/api-docs/**",
                                                "/webjars/**",
                                                "/favicon**",
                                                "/v3/api-docs.yaml"
                                                        )
                                        .permitAll()
                                        // точки доступа для
                                        // анонимных пользователей
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                // AuthorizationController
                                                "/register*"
                                                        )
                                        .anonymous()
                                        // точки доступа для полностью
                                        // авторизованных пользователей
                                        .requestMatchers(
                                                // адрес, назначенный для формы выхода
                                                "/logout"
                                                        ).fullyAuthenticated()
                                        // точки доступа для пользователей,
                                        // имеющих авторизацию USER, ADMIN и OWNER
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                // AuthorizationController
                                                "/users"
                                                        )
                                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER")
                                        // точки доступа для пользователей,
                                        // имеющих авторизацию OWNER
                                        .requestMatchers(
                                                HttpMethod.PATCH,
                                                // AuthorizationController
                                                "/set-role"
                                                        )
                                        .hasAuthority("ROLE_OWNER")
                                        // точки доступа для пользователей,
                                        // имеющих роли USER, ADMIN и OWNER
                                        .requestMatchers(
                                                HttpMethod.PATCH,
                                                // AuthorizationController
                                                "/set-password"
                                                        )
                                        .hasAnyRole("USER", "ADMIN", "OWNER")
                                        // точки доступа для пользователей,
                                        // имеющих роли ADMIN и OWNER
                                        .requestMatchers(
                                                HttpMethod.DELETE,
                                                // AuthorizationController
                                                "/delete"
                                                        )
                                        .hasAnyRole("ADMIN", "OWNER")
                                        // разрешить доступ
                                        // всем пользователям
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                // BooksController
                                                "/books",
                                                "/books/*/cover",
                                                "/books/*/cover/preview"
                                                        )
                                        .permitAll()
                                        // разрешить доступ
                                        // только авторизованным пользователям
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                // BooksController
                                                "/books/**",
                                                // ReaderController
                                                "/readers",
                                                // ManageController
                                                "/manage",
                                                // ExpensesController
                                                "/expenses"
                                                        )
                                        .authenticated()
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                // BooksController
                                                "/books/*",
                                                // ReaderController
                                                "/readers/**",
                                                // ManageController
                                                "/manage",
                                                // WhetherController
                                                "/whether/**",
                                                // ExpensesController
                                                "/expenses/**",
                                                // CounterController
                                                "/counter/**",
                                                // RandomizeController
                                                "/random"
                                                        )
                                        .authenticated()
                                        .requestMatchers(
                                                HttpMethod.PUT,
                                                // BooksController
                                                "/books",
                                                // ReaderController
                                                "/readers"
                                                        )
                                        .authenticated()
                                        .requestMatchers(
                                                HttpMethod.DELETE,
                                                // BooksController
                                                "/books/*",
                                                // ReaderController
                                                "/readers/*"
                                                        )
                                        .authenticated()
                                        // запретить другие запросы
                                        .anyRequest().denyAll()
                                      )
                // настройка CORS
                .cors(cors -> {
                    // Источник конфигураций CORS
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    // Конфигурация CORS
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    // Разрешаются CORS-запросы:
                    // - с сайта http://localhost:8080
                    corsConfiguration.addAllowedOrigin("http://localhost:8080");
                    // - с заголовками Authorization, X-CSRF-TOKEN и X-XSRF-TOKEN
                    corsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
                    corsConfiguration.addAllowedHeader("X-CSRF-TOKEN");
                    corsConfiguration.addAllowedHeader("X-XSRF-TOKEN");
                    // - с передачей учётных данных
                    corsConfiguration.setAllowCredentials(true);
                    // - с методами GET, POST, PUT, PATCH и DELETE
                    corsConfiguration.setAllowedMethods(List.of(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.PATCH.name(),
                            HttpMethod.DELETE.name()
                                                               ));
                    // JavaScript может обращаться к заголовкам
                    // X-CSRF-TOKEN и X-XSRF-TOKEN ответа
                    corsConfiguration.setExposedHeaders(List.of("X-CSRF-TOKEN"));
                    corsConfiguration.setExposedHeaders(List.of("X-XSRF-TOKEN"));
                    // Браузер может кешировать настройки CORS на 10 секунд
                    corsConfiguration.setMaxAge(Duration.ofSeconds(10));
                    // Использование конфигурации CORS для всех запросов
                    source.registerCorsConfiguration("/**", corsConfiguration);
                    // Возврат настроенного фильтра
                    cors.configurationSource(source);
                })
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
                        .ignoringRequestMatchers(
                                // отключение проверки для запросов из Swagger UI
                                new RequestHeaderRequestMatcher("referer",
                                                                "http://localhost:8080/swagger-ui/index.html"),
                                // HelloController
                                new AntPathRequestMatcher("/", HttpMethod.GET.name()),
                                // AuthorizationController
                                new AntPathRequestMatcher("/login*", HttpMethod.POST.name()),
                                new AntPathRequestMatcher("/csrf*", HttpMethod.GET.name()),
                                // стандартный адрес вывода сообщений об ошибке
                                new AntPathRequestMatcher("/error*", HttpMethod.GET.name()),
                                // стандартные адреса разметки HTML-страниц
                                new AntPathRequestMatcher("/css/**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/js/**", HttpMethod.GET.name()),
                                // адреса Swagger UI
                                new AntPathRequestMatcher("/swagger-resources/**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/webjars/**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/favicon**", HttpMethod.GET.name()),
                                new AntPathRequestMatcher("/v3/api-docs.yaml", HttpMethod.GET.name()),
                                // BooksController
                                new AntPathRequestMatcher("/books", HttpMethod.GET.name())
                                                )
                        // настройка запросов, по которым ОБЯЗАТЕЛЬНО должна
                        // осуществляться защита от CSRF-атак (API для
                        // GET, HEAD, OPTIONS и TRACE запросов, которые
                        // необходимо защитить от CSRF-атак)
                        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/**", HttpMethod.GET.name()))
                     )
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
                // !!! ВНИМАНИЕ !!!
                // Включение созданного фильтра CSRFTokenFilter в цепочку
                // фильтров после фильтра CsrfFilter, добавляющего CSRF-токен
                // в заголовок запроса (необходим для тестирования через Postman)
                .addFilterAfter(new CSRFTokenFilter(), CsrfFilter.class)
                // собрать цепочку фильтров
                .build();
    }

    @Bean
    // Кодировщик паролей в приложении
    public PasswordEncoder passwordEncoder() {
        // задание реализации DelegatingPasswordEncoder с помощью класса PasswordEncoderFactories
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    // Провайдер аутентификации, осуществляющий логику аутентификации пользователя
    public AuthenticationProvider authenticationProvider() {
        // провайдер для работы с базами данных
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // определение реализации интерфейса UserDetailsService,
        // которая загружает данные о пользователях из БД
        authProvider.setUserDetailsService(userDetailsService);
        // задание кодировщика паролей
        authProvider.setPasswordEncoder(passwordEncoder());
        // возврат значения
        return authProvider;
    }

    @Bean
    // Менеджер аутентификации
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // возвращение менеджера аутентификации с текущими настройками безопасности
        return authenticationConfiguration.getAuthenticationManager();
    }
}