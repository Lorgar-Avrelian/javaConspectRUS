package lorgar.avrelian.javaconspectrus.securityFilters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HexAuthenticationFilter extends OncePerRequestFilter {
    // Доступ к стратегии хранения информации в контексте безопасности
    private SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    // Доступ к репозиторию контекста безопасности
    private SecurityContextRepository securityContextRepository =
            new RequestAttributeSecurityContextRepository();
    // Доступ к менеджеру аутентификации
    private final AuthenticationManager authenticationManager;
    // Доступ к точке входа
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public HexAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Получение заголовка AUTHORIZATION
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        // Проверка на наличие заголовка и его содержания, что он
        // содержит именно Hex-кодировку (Base16-кодировку) логина и пароля
        if (authorization != null && authorization.startsWith("Hex ")) {
            // Получение закодированного токена с помощью регулярного выражения
            String encodedToken = authorization.replaceAll("^Hex ", "");
            // Декодирование токена при помощи Hex-кодера Spring Security
            String decodedToken = new String(Hex.decode(encodedToken), StandardCharsets.UTF_8);
            // Получение логина и пароля после декодирования
            String[] split = decodedToken.split(":");
            // Создание не аутентифицированного токена
            // UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(split[0], split[1]);
            try {
                // Обращение к менеджеру аутентификации AuthenticationManager
                // для аутентификации полученного запроса
                Authentication authenticate = this.authenticationManager.authenticate(unauthenticated);
                // Создание пустого контекста безопасности
                SecurityContext emptyContext = this.securityContextHolderStrategy.createEmptyContext();
                // Размещение результата аутентификации в созданном контексте безопасности
                emptyContext.setAuthentication(authenticate);
                // Сохранение нового контекста в стратегии и репозитории контекста безопасности
                this.securityContextHolderStrategy.setContext(emptyContext);
                this.securityContextRepository.saveContext(emptyContext, request, response);
            } catch (AuthenticationException e) {
                // При возникновении ошибок аутентификации:
                // - очистка контекста
                this.securityContextHolderStrategy.clearContext();
                // - перенаправление пользователя на повторную аутентификацию
                this.authenticationEntryPoint.commence(request, response, e);
                // - прерывание выполнения цепочки фильтров
                return;
            }
        }
        // Продолжить выполнение цепочки фильтров
        filterChain.doFilter(request, response);
    }
}
