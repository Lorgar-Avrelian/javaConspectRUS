package lorgar.avrelian.javaconspectrus.securityFilters;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигуратор, позволяющий настраивать компоненты Spring Security
 */
public class HexConfigurer extends AbstractHttpConfigurer<HexConfigurer, HttpSecurity> {
    // Точка входа со значением параметров и поведения по умолчанию
    private AuthenticationEntryPoint authenticationEntryPoint =
            ((request, response, authException) -> {
                response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Hex");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            });

    /**
     * Метод, предназначенный для инициализации {@link SecurityBuilder} {@code builder}
     *
     * @param builder строитель для построения объектов Spring Security
     * @throws Exception при возникновении ошибок при построении объекта
     */
    @Override
    public void init(HttpSecurity builder) throws Exception {
        // Назначение точки входа AuthenticationEntryPoint
        // как способа аутентификации по умолчанию
        builder.exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint));
    }

    /**
     * Метод, предназначенный для настройки {@link SecurityBuilder} {@code builder}
     *
     * @param builder строитель для построения объектов Spring Security
     * @throws Exception при возникновении ошибок при построении объекта
     */
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        // Получение менеджера аутентификации
        AuthenticationManager manager = builder.getSharedObject(AuthenticationManager.class);
        // Добавление созданного фильтра в цепочку фильтров FilterChain
        builder.addFilterBefore(new HexAuthenticationFilter(manager, this.authenticationEntryPoint),
                                UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Сеттер для изменения параметров точки входа
     *
     * @param authenticationEntryPoint точка входа
     */
    public HexConfigurer setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        return this;
    }
}
