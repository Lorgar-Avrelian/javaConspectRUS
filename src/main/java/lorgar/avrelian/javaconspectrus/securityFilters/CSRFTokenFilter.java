package lorgar.avrelian.javaconspectrus.securityFilters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CSRFTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        System.out.println(token.getHeaderName());
        System.out.println(token.getParameterName());
        System.out.println(token.getToken());
        response.setHeader("X-CSRF-TOKEN", token.getToken());
        response.addCookie(new Cookie("CSRF-TOKEN", token.getToken()));
        filterChain.doFilter(request, response);
    }
}
