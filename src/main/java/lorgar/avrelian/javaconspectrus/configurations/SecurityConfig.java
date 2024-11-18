package lorgar.avrelian.javaconspectrus.configurations;

import jakarta.servlet.DispatcherType;
import lorgar.avrelian.javaconspectrus.models.Role;
import lorgar.avrelian.javaconspectrus.securityFilters.BasicAuthCorsFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

// Включает поддержку Spring Security
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(@Qualifier(value = "jpaUserDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // репозиторий CSRF-токена
        CsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        // настройка очистки файлов Cookies и заголовков при выходе
        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter());
        // стандартные настройки цепочки безопасности
        return http
                // настройка авторизации
                .authorizeHttpRequests(
                        requests -> requests
                                // разрешить доступ при перенаправлении
                                .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll()
                                // запретить доступ всем пользователям к указанным ресурсам
                                .requestMatchers("/counter/**").denyAll()
                                .requestMatchers(HttpMethod.TRACE).denyAll()
                                .requestMatchers(HttpMethod.DELETE, "/logout").denyAll()
                                // разрешить доступ только анонимным пользователям к указанным ресурсам
                                .requestMatchers(
                                        "/",
                                        "/login",
                                        "/register"
                                                ).anonymous()
                                // разрешить доступ только аутентифицированным пользователям к указанным ресурсам
                                .requestMatchers(
                                        "/logout",
                                        "/users",
                                        "/set-role",
                                        "/set-password",
                                        "/delete",
                                        "/books/**",
                                        "/expenses/**",
                                        "/manage/**",
                                        "/random",
                                        "/readers/**",
                                        "/user/**"
                                                ).authenticated()
                                // разрешить доступ всем пользователям к указанным ресурсам
                                .requestMatchers(
                                        "/error",
                                        "/whether/**",
                                        "/swagger-resources/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs",
                                        "/webjars/**"
                                                ).permitAll()
                                // разрешить доступ пользователям с долгоживущей сессией к указанным ресурсам
                                .requestMatchers(
                                        "/whether/**",
                                        "/swagger-resources/**",
                                        "/swagger-ui.html",
                                        "/v3/api-docs",
                                        "/webjars/**"
                                                ).rememberMe()
                                // разрешить доступ пользователям, прошедшим полную аутентификацию,
                                // а не из восстановленной сессии, к указанным ресурсам
                                .requestMatchers(
                                        "/logout",
                                        "/users",
                                        "/set-role",
                                        "/set-password",
                                        "/delete",
                                        "/books/**",
                                        "/expenses/**",
                                        "/manage/**",
                                        "/random",
                                        "/readers/**",
                                        "/user/**"
                                                ).fullyAuthenticated()
                                // разрешить доступ только пользователям, имеющим заданные права, к указанным ресурсам
                                .requestMatchers(
                                        "/manage/**"
                                                ).hasAuthority(Role.ROLE_USER.getAuthority())
                                // разрешить доступ пользователям, имеющим любое из заданных прав, к указанным ресурсам
                                .requestMatchers(
                                        "/set-role",
                                        "/set-password",
                                        "/delete"
                                                ).hasAnyAuthority(Role.ROLE_OWNER.getAuthority(),
                                                                  Role.ROLE_ADMIN.getAuthority())
                                // разрешить доступ только пользователям, имеющим заданную роль, к указанным ресурсам
                                .requestMatchers(
                                        "/random"
                                                ).hasRole("ROLE_ADMIN")
                                // разрешить доступ пользователям, имеющим любую из заданных ролей, к указанным ресурсам
                                .requestMatchers(
                                        "/users"
                                                ).hasAnyRole("ROLE_OWNER", "ROLE_ADMIN")
                                .requestMatchers(
                                        "/expenses/**"
                                                ).access((authentication, object) -> {
                                    return new AuthorizationDecision(Role.ROLE_USER.name().equals(authentication.get().getName()));
                                })
                                // запретить все другие запросы
                                .anyRequest().denyAll()
                                      )
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
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/login"),
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/register"),
                                new AntPathRequestMatcher("/whether/**"),
                                new AntPathRequestMatcher("/swagger-resources/**"),
                                new AntPathRequestMatcher("/swagger-ui.html"),
                                new AntPathRequestMatcher("/v3/api-docs"),
                                new AntPathRequestMatcher("/webjars/**")
                                                )
                        // настройка адресов, по которым ОБЯЗАТЕЛЬНО должна
                        // осуществляться защита от CSRF-атак
                        .requireCsrfProtectionMatcher(new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON))
                     )
                .cors(configurer -> {
                    // Источник конфигураций CORS
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    // Конфигурация CORS
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    // Разрешаются CORS-запросы:
                    // - с сайта http://localhost:8080
                    corsConfiguration.addAllowedOrigin("http://localhost:8080");
                    // - с нестандартными заголовками Authorization и X-CUSTOM-HEADER
                    corsConfiguration.addAllowedHeader(HttpHeaders.AUTHORIZATION);
                    corsConfiguration.addAllowedHeader("X-CUSTOM-HEADER");
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
                    // JavaScript может обращаться к заголовку X-OTHER-CUSTOM-HEADER ответа
                    corsConfiguration.setExposedHeaders(List.of("X-OTHER-CUSTOM-HEADER"));
                    // Браузер может кешировать настройки CORS на 10 секунд
                    corsConfiguration.setMaxAge(Duration.ofSeconds(10));
                    // Использование конфигурации CORS для всех запросов
                    source.registerCorsConfiguration("/**", corsConfiguration);
                    // Возврат настроенного фильтра
                    configurer.configurationSource(source);
                })
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
                                    response.sendRedirect("http://localhost:8080/index.html");
                                    return;
                                }
                                                 ))
                // Включение созданного фильтра BasicAuthCorsFilter в цепочку
                // фильтров перед фильтром UsernamePasswordAuthenticationFilter
                .addFilterBefore(new BasicAuthCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                // настройка выхода
                .logout(logout -> logout
                        .addLogoutHandler(clearSiteData)
                        .clearAuthentication(true)
                       )
                // собрать цепочку фильтров
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
