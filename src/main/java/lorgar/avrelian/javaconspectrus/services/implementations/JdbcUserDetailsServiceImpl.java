package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.services.JdbcUserDetailsService;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис, предназначенный для обращения к БД и получения из неё сущности пользователя {@link UserDetails}
 */
//@Service
public class JdbcUserDetailsServiceImpl extends MappingSqlQuery<UserDetails> implements JdbcUserDetailsService {
    /**
     * Конструктор по умолчанию, в который передаётся источник данных (БД) и в котором<br>
     * присутствует <i>SQL</i>-запрос для получения сущности пользователя {@link UserDetails} из БД
     * @param ds база данных
     */
    public JdbcUserDetailsServiceImpl(DataSource ds) {
        // конструктор родительского класса
        super(ds, """
                SELECT l.login AS login, p.password AS password, r.role AS role
                FROM user_login AS l
                    LEFT JOIN user_password p ON l.id = p.user_id
                    LEFT JOIN user_role r ON r.id = l.role_id
                WHERE l.login = :username;
                """);
        // объявление нового параметра username
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        // фиксация конструктора путём определения его как неизменяемого
        this.compile();
    }

    /**
     * Метод, предназначенный для преобразования объекта,<br>
     * получаемого из БД, в объект класса {@link UserDetails}
     * @param rs данные о пользователе, полученные в результате <i>SQL</i>-запроса конструктора
     * @param rowNum номер строки в ответе, полученном от БД в результате <i>SQL</i>-запроса конструктора
     * @return информация о пользователе (объект класса {@link UserDetails})
     * @throws SQLException если при выполнении <i>SQL</i>-запроса возникла ошибка
     */
    @Override
    protected UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .username(rs.getString("login"))
                .password(rs.getString("password"))
                .authorities((String[]) rs.getArray("role").getArray())
                .build();
    }

    /**
     * Метод для загрузки информации о пользователе по его логину
     * @param username логин пользователя
     * @return информация о пользователе (объект класса {@link UserDetails})
     * @throws UsernameNotFoundException если пользователь с таким логином не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(this.findObjectByNamedParam(Map.of("username", username)))
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found".formatted(username)));
    }
}
