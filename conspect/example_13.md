## Пример 1:

> [[_оглавление_]](../README.md/#39-spring-security)

> [**[3.9 Spring Security]**](/conspect/3.md/#39-spring-security)

### Настройка _Spring Security_ с _Basic_-аутентификацией и использованием _JPA_-репозитория для данных пользователей:

- [подключение зависимостей](#подключение-зависимостей) в файле _pom.xml_:
    * [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web);
    * [Spring Boot Starter Thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf);
    * [SpringDoc OpenAPI Starter WebMVC UI (Swagger UI)](https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui);
    * [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa);
    * [PostgreSQL JDBC Driver](https://mvnrepository.com/artifact/org.postgresql/postgresql);
    * [H2 Database Engine](https://mvnrepository.com/artifact/com.h2database/h2);
    * [Liquibase](https://mvnrepository.com/artifact/org.liquibase/liquibase-core);
    * [Logback Core Module](https://mvnrepository.com/artifact/ch.qos.logback/logback-core);
    * [SLF4J API Module](https://mvnrepository.com/artifact/org.slf4j/slf4j-api);
    * [Logback Classic Module](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic);
    * [Colorizer Extension Library For Logback](https://mvnrepository.com/artifact/org.tuxdude.logback.extensions/logback-colorizer);
    * [Spring Boot Starter Cache](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache);
    * [MapStruct Core](https://mvnrepository.com/artifact/org.mapstruct/mapstruct);
    * [MapStruct Processor](https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor);
    * [Project Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok);
    * [Spring Boot Starter Security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security);
    * [JUnit Jupiter Params](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params);
    * [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test);
    * [Spring Security Test](https://mvnrepository.com/artifact/org.springframework.security/spring-security-test);
    * [Telegram Bots](https://mvnrepository.com/artifact/org.telegram/telegrambots);
- [добавление плагинов для подключённых зависимостей](#добавление-плагинов-для-подключённых-зависимостей);
- создание директорий приложения:
    * _configurations_;
    * _controllers_;
    * _models_:
        + _dao_;
        + _dto_;
    * _exceptionHandlers_;
    * _mappers_;
    * _repository_;
    * _securityFilters_;
    * _services_:
        + implementations;
    * _telegramBot_;
- [настройка параметров приложения](#настройка-параметров-приложения) в файле _application.properties_:
    * приложения в целом;
    * параметров отображения _Swagger UI_;
    * параметров подключения к БД и _Hibernate_;
    * задание файла _change_-лога для _Liquibase_;
    * определение локальных параметров, необходимых для работы сервисов приложения;
- [настройка стартера приложения](#настройка-стартера-приложения);
- [настройка логирования](#настройка-логирования) в файле _logback.xml_ (_src/main/resources/logback.xml_);
- [настройка _Liquibase_](#настройка-liquibase):
    * создание файла _change_-лога для _Liquibase_ (_src/main/resources/liquibase/changelog-master.yml_);
    * написание _SQL_-скрипта (_src/main/resources/liquibase/scripts/conspectus.sql_) для создания таблиц в БД;
- [создание перечисления ролей пользователей](#создание-перечисления-ролей-пользователей);
- [создание сущностей _DAO_](#создание-сущностей-dao):
    * сущности для хранения аутентификационных данных пользователей;
    * других сущностей, необходимых для работы приложения;
- [создание репозиториев для работы с БД](#создание-репозиториев-для-работы-с-бд):
    * репозитория для хранения аутентификационных данных пользователей;
    * других репозиториев, необходимых для работы приложения;
- [создание сервиса-наследника
  _UserDetailsService_ и его реализация](#создание-сервиса-наследника-userdetailsservice-и-его-реализация) для
  получения аутентификационных данных пользователей из БД;
- [создание сущностей _DTO_](#создание-сущностей-dto):
    * сущности для передачи аутентификационных данных пользователей;
    * других сущностей, необходимых для работы приложения;
- [создание мапперов](#создание-мапперов) для _DTO_;

### Подключение зависимостей

> [[**В начало**]](#пример-1)

```xml

<dependencies>
    <!--  Spring Boot Starter Web  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--  Spring Boot Starter Thymeleaf  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <!--  SpringDoc OpenAPI Starter WebMVC UI (Swagger UI)  -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.6.0</version>
    </dependency>
    <!--  Spring Boot Starter Data JPA  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <!-- PostgreSQL JDBC Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.4</version>
        <scope>runtime</scope>
    </dependency>
    <!-- H2 Database Engine -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.3.232</version>
        <scope>test</scope>
    </dependency>
    <!--  Liquibase  -->
    <dependency>
        <groupId>org.liquibase</groupId>
        <artifactId>liquibase-core</artifactId>
    </dependency>
    <!-- Logback Core Module -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>${ch.qos.logback.version}</version>
    </dependency>
    <!-- SLF4J API Module -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.1.0-alpha1</version>
    </dependency>
    <!-- Logback Classic Module -->
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>${ch.qos.logback.version}</version>
    </dependency>
    <!-- Colorizer Extension Library For Logback -->
    <dependency>
        <groupId>org.tuxdude.logback.extensions</groupId>
        <artifactId>logback-colorizer</artifactId>
        <version>1.0.1</version>
    </dependency>
    <!-- Spring Boot Starter Cache -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-cache</artifactId>
        <version>3.3.5</version>
    </dependency>
    <!-- MapStruct Core -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
    <!-- MapStruct Processor -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
    <!-- Project Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${org.lombok.version}</version>
        <scope>provided</scope>
    </dependency>
    <!--  Spring Boot Starter Security  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <!-- JUnit Jupiter Params -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>5.11.3</version>
        <scope>test</scope>
    </dependency>
    <!--  Spring Boot Starter Test  -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <!--  Spring Security Test  -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-test</artifactId>
        <scope>test</scope>
    </dependency>
    <!--  Telegram Bots  -->
    <dependency>
        <groupId>org.telegram</groupId>
        <artifactId>telegrambots</artifactId>
        <version>6.9.7.1</version>
    </dependency>
</dependencies>
```

### Добавление плагинов для подключённых зависимостей

> [[**В начало**]](#пример-1)

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.mapstruct</groupId>
                        <artifactId>mapstruct-processor</artifactId>
                        <version>${org.mapstruct.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>${org.lombok.version}</version>
                    </path>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-mapstruct-binding</artifactId>
                        <version>0.2.0</version>
                    </path>
                </annotationProcessorPaths>
                <compilerArgs>
                  <compilerArg>
                    -Amapstruct.defaultComponentModel=spring
                  </compilerArg>
                </compilerArgs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

### Настройка параметров приложения

> [[**В начало**]](#пример-1)

```properties
# Application properties
spring.application.name=Java conspectus RUS
server.port=8080
# Swagger UI setting for turn on sorting:
# - controllers by tags names
# - methods by HTTP method
springdoc.swagger-ui.tags-sorter=alpha
springdoc.swagger-ui.operations-sorter=method
# Spring Data JPA parameters for DB connection
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=library_user
spring.datasource.password=123
# Hibernate settings
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=validate
# Settings of showing SQL-requests of Hibernate
spring.jpa.show-sql=false
# Liquibase change-log settings
spring.liquibase.change-log=classpath:liquibase/changelog-master.yml
# Lombok options for working with MapStruct
lombok.addLombokGeneratedAnnotation=true
lombok.anyConstructor.addConstructorProperties=true
# Specific parameter for RandomizeConfig class
rand.diapason=1000
# Specific parameter for BookCoverService implementations
books.covers.dir.path=books/covers
# Specific parameter for WhetherService implementations
whether.current.url=https://api.openweathermap.org/data/2.5/weather
whether.geo.url=http://api.openweathermap.org/geo/1.0/direct
whether.geo.result.count=10
whether.api.key=c2bdc9ea4264d25b8b06326de0ea9591
```

### Настройка стартера приложения

> [[**В начало**]](#пример-1)

```java
// Включает Spring Boot
@SpringBootApplication
// Включает Swagger UI
@OpenAPIDefinition(
        info = @Info(
                title = "Конспект по языку Java",
                description = "Конспект по языку Java на русском языке",
                version = "0.1.0",
                contact = @Contact(
                        name = "Токовенко Виктор",
                        email = "victor-14-244@mail.ru",
                        url = "https://github.com/Lorgar-Avrelian?tab=repositories"
                )
        )
)
// Включает кэширование
@EnableScheduling
// Включает защиту методов Spring Security
@EnableMethodSecurity(securedEnabled = true)
public class JavaConspectusRusApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaConspectusRusApplication.class, args);
    }
}
```

### Настройка логирования

> [[**В начало**]](#пример-1)

```xml

<configuration>
    <!-- Stop output INFO at start -->
    <statusListener class="ch.qos.logback.core.status.NopStatusListener"/>
    <!--    Настройка вывода логов в терминал (консоль)-->
    <!--    Выбор цветовой схемы-->
    <!--    <property scope="context" name="COLORIZER_COLORS" value="red@black,yellow@black,green@black,blue@black,magenta@black" />-->
    <!--    ИЛИ-->
    <property scope="context" name="COLORIZER_COLORS" value="red@,yellow@,green@,blue@,magenta@"/>
    <conversionRule conversionWord="colorize" converterClass="org.tuxdude.logback.extensions.LogColorizer"/>
    <!--    Настройка ConsoleAppender-->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <withJansi>true</withJansi>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                <!--    Раскрашивание логов с помощью "org.tuxdude.logback.extensions.LogColorizer"            -->
                %colorize(%d{HH:mm:ss.SSS} [%t] %-4relative %-5level %logger{36} - %msg%n)
                <!--    Раскрашивание логов с помощью стандартной библиотеки                                   -->
                <!--                %highlight(%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n)-->
            </Pattern>
        </layout>
    </appender>
    <!--    Настройка вывода логов в файл-->
    <!--    Задание директории для лог-файлов-->
    <property name="LOG_DIR" value="logs"/>
    <!--    Настройка FileAppender-->
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <!--    Задание названия файла для сохранения логов-->
        <file>${LOG_DIR}/services.log</file>
        <append>true</append>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%t] %-5p %c{1}:%L - %m%n
            </pattern>
        </encoder>
    </appender>
    <!--    Настройка вывода логов в файл с прокруткой-->
    <!--    Задание названий для лог-файлов-->
    <property name="LOG_FILE_WEB" value="web"/>
    <!--    Настройка RollingFileAppender-->
    <appender name="ROLL-FILE-1" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--    Задание названия файла для сохранения логов-->
        <file>${LOG_DIR}/${LOG_FILE_WEB}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- ежедневная прокрутка -->
            <fileNamePattern>
                ${LOG_FILE_WEB}.%d{yyyy-MM-dd}.gz
            </fileNamePattern>
            <!-- каждый из архивных файлов не больше 10МБ, общий объем истории за 30 дней должен быть ограничен 3 ГБ -->
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
        </encoder>
    </appender>
    <!--    Настройка ещё одного вывода логов в файл с прокруткой-->
    <property name="LOG_FILE_CONTROLLER" value="controller"/>
    <appender name="ROLL-FILE-2" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/${LOG_FILE_CONTROLLER}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                ${LOG_FILE_CONTROLLER}.%d{yyyy-MM-dd}.gz
            </fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
        </encoder>
    </appender>
    <!--    Настройка ещё одного вывода логов в файл с прокруткой-->
    <property name="LOG_FILE_CONFIG" value="configuration"/>
    <appender name="ROLL-FILE-3" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_DIR}/${LOG_FILE_CONFIG}.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                ${LOG_FILE_CONFIG}.%d{yyyy-MM-dd}.gz
            </fileNamePattern>
            <maxFileSize>10MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%-4relative [%thread] %-5level %logger{35} - %msg%n</pattern>
        </encoder>
    </appender>
    <!--    Настройка ещё одного вывода логов в файл -->
    <property name="LOG_FILE_TELEGRAM" value="telegram-bot"/>
    <appender name="TELEGRAM" class="ch.qos.logback.core.FileAppender">
        <file>${LOG_DIR}/${LOG_FILE_TELEGRAM}.log</file>
        <append>true</append>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss} [%t] %-5p %c{1}:%L - %m%n
            </pattern>
        </encoder>
    </appender>

    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для всего приложения-->
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для org.springframework.web-->
    <logger name="org.springframework.web" level="ERROR" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ROLL-FILE-1"/>
    </logger>
    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для пакета-->
    <logger name="lorgar.avrelian.javaconspectrus.services.implementations" level="TRACE" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
    </logger>
    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для контроллеров-->
    <logger name="lorgar.avrelian.javaconspectrus.controllers" level="TRACE" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ROLL-FILE-2"/>
    </logger>
    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для конфигураций-->
    <logger name="lorgar.avrelian.javaconspectrus.configurations" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="ROLL-FILE-3"/>
    </logger>
    <!--    Задание уровня логирования и конкретной реализации интерфейса Appenders для телеграм-бота-->
    <logger name="lorgar.avrelian.javaconspectrus.telegramBot" level="INFO" additivity="false">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="TELEGRAM"/>
    </logger>
    <logger name="org.springframework.security" level="TRACE" additivity="false">
        <appender-ref ref="CONSOLE"/>
    </logger>

</configuration>
```

### Настройка Liquibase

> [[**В начало**]](#пример-1)

```yaml
databaseChangeLog:
  - include:
      file: liquibase/scripts/conspectus.sql
```

```sql
-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE expense
(
    id       SERIAL NOT NULL,
    title    TEXT   NOT NULL,
    date     DATE   NOT NULL,
    category TEXT,
    amount   FLOAT  NOT NULL
);

-- changeset tokovenko:2
CREATE INDEX category_index ON expense (category);

-- changeset tokovenko:3
CREATE TABLE reader
(
    id              BIGSERIAL,
    name            VARCHAR(12) NOT NULL,
    second_name     VARCHAR(16) NOT NULL,
    surname         VARCHAR(30) NOT NULL,
    personal_number INTEGER     NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- changeset tokovenko:4
CREATE TABLE book
(
    id        BIGSERIAL,
    author    VARCHAR(30) NOT NULL,
    title     VARCHAR(30) NOT NULL,
    year      SMALLINT CHECK ( year > 1970 ),
    reader_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (reader_id) REFERENCES reader (id)
);

-- changeset tokovenko:5
CREATE TABLE book_cover
(
    id            BIGSERIAL,
    file_path     VARCHAR(255) NOT NULL,
    file_size     INTEGER      NOT NULL,
    image_preview oid          NOT NULL,
    media_type    VARCHAR(30)  NOT NULL,
    book_id       BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

-- changeset tokovenko:6
CREATE INDEX reader_index ON book (reader_id);

-- changeset tokovenko:7
CREATE INDEX book_index ON book_cover (book_id);

-- changeset tokovenko:8
CREATE TABLE city
(
    id          BIGSERIAL,
    name        VARCHAR(255)    NOT NULL,
    latitude    NUMERIC(16, 13) NOT NULL,
    longitude   NUMERIC(16, 13) NOT NULL,
    country     VARCHAR(30)     NOT NULL,
    state       VARCHAR(30)     NOT NULL,
    local_names TEXT            NOT NULL,
    PRIMARY KEY (id)
);

-- changeset tokovenko:9
CREATE TABLE login
(
    id       BIGSERIAL,
    login    VARCHAR(30) NOT NULL UNIQUE,
    password TEXT        NOT NULL,
    role     SMALLINT    NOT NULL,
    PRIMARY KEY (id)
);
```

### Создание перечисления ролей пользователей

> [[**В начало**]](#пример-1)

```java
public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN, ROLE_OWNER;

    @Override
    public String getAuthority() {
        return name();
    }
}
```

### Создание сущностей DAO

> [[**В начало**]](#пример-1)

- сущность для хранения аутентификационных данных пользователей:

```java

@Schema(title = "Логин", description = "Сущность логина пользователя")
@Entity
@Table(name = "login")
@NoArgsConstructor
@Data
public class Login {
    @Schema(title = "ID", description = "ID пользователя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    @Column(name = "login", nullable = false, unique = true, length = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "{noop}123", required = true, minLength = 3)
    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;
    @Schema(title = "Роль", description = "Роль пользователя в системе", defaultValue = "GUEST", required = true, minLength = 3, maxLength = 30)
    @Column(name = "role", nullable = false)
    private Role role;
}
```

- другие сущности, необходимые для работы приложения:

```java

@Schema(title = "Книга", description = "Сущность книги")
@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
public class Book {
    @Schema(title = "ID", description = "ID книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    @Column(name = "title", nullable = false, unique = true, length = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    @Column(name = "author", nullable = false, length = 30)
    private String author;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    @Column(name = "year", nullable = false)
    private short year;
    @Schema(title = "ID читателя", description = "ID читателя", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "reader_id")
    private Reader reader;
}
```

```java

@Schema(title = "Обложка книги", description = "Сущность обложки для книг")
@Entity
@Table(name = "book_cover")
@Data
@NoArgsConstructor
public class BookCover {
    @Schema(title = "ID", description = "ID обложки книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Путь", description = "Путь сохранения обложки книги", required = true)
    @Column(name = "file_path", nullable = false, unique = true)
    private String filePath;
    @Schema(title = "Размер", description = "Размер сохранённой обложки книги", required = true)
    @Column(name = "file_size", nullable = false)
    private int fileSize;
    @Schema(title = "Тип", description = "Тип сохранённой обложки книги", required = true)
    @Column(name = "media_type", nullable = false)
    private String mediaType;
    @Lob
    @Schema(title = "Превью", description = "Превью обложки книги", required = true)
    @Column(name = "image_preview", nullable = false)
    private byte[] imagePreview;
    @Schema(title = "ID книги", description = "ID книги", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
```

> [[**В начало**]](#пример-1)

```java

@Schema(title = "Читатель", description = "Сущность читателя")
@Entity
@Table(name = "reader")
@Data
@NoArgsConstructor
public class Reader {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    @Column(name = "name", nullable = false, length = 12)
    @EqualsAndHashCode.Exclude
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    @Column(name = "second_name", nullable = false, length = 16)
    @EqualsAndHashCode.Exclude
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    @Column(name = "surname", nullable = false, length = 30)
    @EqualsAndHashCode.Exclude
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    @Column(name = "personal_number", nullable = false, unique = true)
    @EqualsAndHashCode.Exclude
    private int personalNumber;
    @Schema(title = "Книги", description = "Книги  читателя", defaultValue = "null", hidden = true)
    @OneToMany(mappedBy = "reader")
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Book> books;
}
```

```java

@Schema(title = "Расход", description = "Сущность затрат")
@Entity
@Table(name = "expense")
@Data
@NoArgsConstructor
public class Expense {
    @Schema(title = "ID", description = "ID траты", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private int id;
    @Schema(title = "Название", description = "Название траты", defaultValue = "Поездка на транспорте", required = true, minLength = 3, maxLength = 30)
    @Column(name = "title", nullable = false, length = 30)
    private String title;
    @Schema(title = "Дата", description = "Дата траты", defaultValue = "2024-10-09", required = true)
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Schema(title = "Категория", description = "Категория затрат", defaultValue = "Транспорт", required = true, minLength = 3, maxLength = 30)
    @Column(name = "category", nullable = false, length = 30)
    private String category;
    @Schema(title = "Количество", description = "Количество затрат", defaultValue = "1000", required = true, minimum = "0")
    @Column(name = "amount", nullable = false)
    private float amount;
}
```

```java

@Schema(title = "Город", description = "Информация о городе")
@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
public class City {
    @Schema(title = "ID", description = "ID города", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private long id;
    @Schema(title = "Название", description = "Название города", defaultValue = "Москва", required = true, minLength = 2, maxLength = 30)
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    @Schema(title = "Широта", description = "Географическая широта города", defaultValue = "55.7504461", required = true, minimum = "0", maximum = "180")
    @Column(name = "latitude", nullable = false)
    private BigDecimal lat;
    @Schema(title = "Долгота", description = "Географическая долгота города", defaultValue = "37.6174943", required = true, minimum = "0", maximum = "180")
    @Column(name = "longitude", nullable = false)
    private BigDecimal lon;
    @Schema(title = "Страна", description = "Код страны", defaultValue = "RU", required = true, minLength = 2, maxLength = 30)
    @Column(name = "country", nullable = false, length = 30)
    private String country;
    @Schema(title = "Регион", description = "Регион страны", defaultValue = "Moscow", required = true, minLength = 2, maxLength = 30)
    @Column(name = "state", nullable = false, length = 30)
    private String state;
    @Schema(title = "Варианты написания", description = "Варианты написания названия города на различных языках", defaultValue = "{\"feature_name\":\"Moscow\",\"no\":\"Moskva\",\"bi\":\"Moskow\",\"na\":\"Moscow\",\"io\":\"Moskva\",\"bs\":\"Moskva\",\"jv\":\"Moskwa\",\"el\":\"Μόσχα\",\"mg\":\"Moskva\",\"ja\":\"モスクワ\",\"su\":\"Moskwa\",\"eo\":\"Moskvo\",\"ab\":\"Москва\",\"co\":\"Moscù\",\"is\":\"Moskva\",\"az\":\"Moskva\",\"hr\":\"Moskva\",\"iu\":\"ᒨᔅᑯ\",\"sk\":\"Moskva\",\"hy\":\"Մոսկվա\",\"sl\":\"Moskva\",\"uk\":\"Москва\",\"an\":\"Moscú\",\"sm\":\"Moscow\",\"yi\":\"מאסקווע\",\"be\":\"Масква\",\"ie\":\"Moskwa\",\"ro\":\"Moscova\",\"tr\":\"Moskova\",\"tt\":\"Мәскәү\",\"sr\":\"Москва\",\"mr\":\"मॉस्को\",\"kk\":\"Мәскеу\",\"mn\":\"Москва\",\"ca\":\"Moscou\",\"zh\":\"莫斯科\",\"ce\":\"Москох\",\"es\":\"Moscú\",\"vo\":\"Moskva\",\"av\":\"Москва\",\"gd\":\"Moscobha\",\"dz\":\"མོསི་ཀོ\",\"yo\":\"Mọsko\",\"nn\":\"Moskva\",\"bo\":\"མོ་སི་ཁོ།\",\"cy\":\"Moscfa\",\"ka\":\"მოსკოვი\",\"ug\":\"Moskwa\",\"sc\":\"Mosca\",\"cs\":\"Moskva\",\"ss\":\"Moscow\",\"lg\":\"Moosko\",\"dv\":\"މޮސްކޯ\",\"se\":\"Moskva\",\"ascii\":\"Moscow\",\"gv\":\"Moscow\",\"fr\":\"Moscou\",\"mt\":\"Moska\",\"am\":\"ሞስኮ\",\"sh\":\"Moskva\",\"it\":\"Mosca\",\"br\":\"Moskov\",\"ko\":\"모스크바\",\"ur\":\"ماسکو\",\"kv\":\"Мӧскуа\",\"et\":\"Moskva\",\"fo\":\"Moskva\",\"zu\":\"IMoskwa\",\"gl\":\"Moscova - Москва\",\"hi\":\"मास्को\",\"sg\":\"Moscow\",\"ru\":\"Москва\",\"kw\":\"Moskva\",\"da\":\"Moskva\",\"ln\":\"Moskú\",\"th\":\"มอสโก\",\"bg\":\"Москва\",\"li\":\"Moskou\",\"ku\":\"Moskow\",\"de\":\"Moskau\",\"my\":\"မော်စကိုမြို့\",\"ky\":\"Москва\",\"wa\":\"Moscou\",\"ga\":\"Moscó\",\"ak\":\"Moscow\",\"fi\":\"Moskova\",\"sw\":\"Moscow\",\"fa\":\"مسکو\",\"id\":\"Moskwa\",\"ht\":\"Moskou\",\"mk\":\"Москва\",\"uz\":\"Moskva\",\"tl\":\"Moscow\",\"mi\":\"Mohikau\",\"so\":\"Moskow\",\"wo\":\"Mosku\",\"sq\":\"Moska\",\"nl\":\"Moskou\",\"cu\":\"Москъва\",\"ps\":\"مسکو\",\"tg\":\"Маскав\",\"kn\":\"ಮಾಸ್ಕೋ\",\"fy\":\"Moskou\",\"st\":\"Moscow\",\"qu\":\"Moskwa\",\"ml\":\"മോസ്കോ\",\"ta\":\"மாஸ்கோ\",\"he\":\"מוסקווה\",\"ay\":\"Mosku\",\"cv\":\"Мускав\",\"ch\":\"Moscow\",\"ms\":\"Moscow\",\"lv\":\"Maskava\",\"la\":\"Moscua\",\"af\":\"Moskou\",\"lt\":\"Maskva\",\"za\":\"Moscow\",\"kg\":\"Moskva\",\"kl\":\"Moskva\",\"gn\":\"Mosku\",\"pt\":\"Moscou\",\"ia\":\"Moscova\",\"os\":\"Мæскуы\",\"oc\":\"Moscòu\",\"vi\":\"Mát-xcơ-va\",\"te\":\"మాస్కో\",\"sv\":\"Moskva\",\"ar\":\"موسكو\",\"pl\":\"Moskwa\",\"tk\":\"Moskwa\",\"eu\":\"Mosku\",\"en\":\"Moscow\",\"ty\":\"Moscou\",\"hu\":\"Moszkva\",\"bn\":\"মস্কো\",\"ba\":\"Мәскәү\",\"nb\":\"Moskva\"}", required = true)
    @Column(name = "local_names", nullable = false, columnDefinition = "TEXT")
    private String localNames;
}
```

### Создание репозиториев для работы с БД

> [[**В начало**]](#пример-1)

- репозиторий для хранения аутентификационных данных пользователей:

```java

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByLoginEqualsIgnoreCase(String login);
}
```

- другие репозитории, необходимые для работы приложения:

```java
/**
 * An implementation of the {@link JpaRepository}.<br>
 * Repository for CRUD operations with {@link Book} entities
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.
     *
     * @param author part of author name
     * @param title  part of book title
     * @return {@code Collection<Book>} of founded books
     */
    List<Book> findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String author, String title);

    /**
     * Method is returning {@link Collection} of all {@link Book} of {@link Reader} with this ID from DB.
     *
     * @param readerId ID of reader
     * @return {@code Collection<Book>} of founded books
     */
    List<Book> findByReaderId(long readerId);
}
```

```java

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
    Optional<BookCover> findByBookId(Long bookId);
}
```

> [[**В начало**]](#пример-1)

```java

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    List<Reader> findByNameContainsIgnoreCase(String namePart);

    List<Reader> findBySecondNameContainsIgnoreCase(String secondNamePart);

    List<Reader> findBySurnameContainsIgnoreCase(String surnamePart);

    List<Reader> findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(String namePart, String secondNamePart, String surnamePart);
}
```

```java

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query(value = "SELECT category, SUM(amount) AS amount FROM expense GROUP BY category", nativeQuery = true)
    List<ExpensesByCategory> getExpenseByCategories();
}
```

```java

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByLocalNamesContainsIgnoreCase(String localName);

    List<City> findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(String localName, String country);
}
```

### Создание сервиса-наследника UserDetailsService и его реализация

> [[**В начало**]](#пример-1)

```java
/**
 * Сервис для работы с сущностями пользователей {@link UserDetails}, сохранёнными в БД,<br>
 * с использованием {@link JpaRepository} и {@link UserDetailsService}
 */
@Service
public interface JpaUserDetailsService extends UserDetailsService {
}
```

```java
/**
 * Сервис-реализация интерфейса {@link JpaUserDetailsService} для работы с<br>
 * сущностями пользователей {@link UserDetails}, сохранёнными в БД,<br>
 * с использованием {@link JpaRepository} и {@link UserDetailsService}
 *
 * @see JpaRepository
 * @see JpaUserDetailsService
 * @see UserDetails
 * @see UserDetailsService
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class JpaUserDetailsServiceImpl implements JpaUserDetailsService {
    private final LoginRepository loginRepository;

    public JpaUserDetailsServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    /**
     * Метод для загрузки информации о пользователе по его логину
     *
     * @param username логин пользователя
     * @return информация о пользователе (объект класса {@link UserDetails})
     * @throws UsernameNotFoundException если пользователь с таким логином не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loginRepository.findByLoginEqualsIgnoreCase(username)
                              .map(login -> {
                                  return new User(
                                          login.getLogin(),
                                          login.getPassword(),
                                          Collections.singleton(login.getRole())
                                  );
                              })
                              .orElseThrow(() -> {
                                  log.severe("No DB connection");
                                  return new UsernameNotFoundException(username);
                              });
    }
}
```

### Создание сущностей DTO

> [[**В начало**]](#пример-1)

- сущности для передачи аутентификационных данных пользователей:

```java

@Schema(title = "Basic-аутентификация", description = "Логин и пароль пользователя")
@NoArgsConstructor
@Data
public class BasicAuthDTO {
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "123", required = true, minLength = 3)
    private String password;
}
```

```java

@Schema(title = "Логин", description = "Сущность логина пользователя")
@NoArgsConstructor
@Data
public class LoginDTO {
    @Schema(title = "ID", description = "ID пользователя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Роль", description = "Роль пользователя в системе", defaultValue = "GUEST", required = true, minLength = 3, maxLength = 30)
    private Role role;
}
```

```java

@Schema(title = "Регистрационные данные", description = "Сущность данных пользователя при регистрации")
@NoArgsConstructor
@Data
public class RegisterDTO {
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "123", required = true, minLength = 3)
    private String password;
    @Schema(title = "Подтверждение пароля", description = "Подтверждение пароля пользователя", defaultValue = "123", required = true, minLength = 3)
    private String passwordConfirmation;
}
```

> [[**В начало**]](#пример-1)

- другие сущности, необходимые для работы приложения:

```java

@Schema(title = "Расходы по категориям", description = "Сущность расходов по категориям")
public interface ExpensesByCategory {
    @Schema(title = "Категория", description = "Название категории", defaultValue = "Транспорт")
    String getCategory();

    @Schema(title = "Количество", description = "Количество затрат", defaultValue = "1000", minimum = "0")
    float getAmount();
}
```

```java

@Schema(title = "Книга", description = "Сущность книги")
@NoArgsConstructor
@Data
public class BookDTO {
    @Schema(title = "ID", description = "ID книги", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    private String author;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    private short year;
    @Schema(title = "ID читателя", description = "ID читателя", defaultValue = "null", minimum = "1", maximum = "9223372036854775807")
    private ReaderNoBooksDTO reader;
}
```

```java

@Schema(title = "Новая книга", description = "Сущность новой книги")
@NoArgsConstructor
@Data
public class NewBookDTO {
    @Schema(title = "Название", description = "Название книги", defaultValue = "Война и мир", required = true, minLength = 3, maxLength = 30)
    private String title;
    @Schema(title = "Автор", description = "Автор книги", defaultValue = "Л.Н. Толстой", required = true, minLength = 8, maxLength = 24)
    private String authorFIO;
    @Schema(title = "Год", description = "Год публикации книги", defaultValue = "1986", required = true, minimum = "1970")
    private short year;
}
```

> [[**В начало**]](#пример-1)

```java

@Schema(title = "Читатель", description = "Сущность читателя")
@NoArgsConstructor
@Data
public class ReaderNoBooksDTO {
    @Schema(title = "ID", description = "ID читателя", defaultValue = "1", required = true, minimum = "1", maximum = "9223372036854775807")
    private long id;
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    private int personalNumber;
}
```

```java
@Schema(title = "Новый читатель", description = "Сущность нового читателя")
@NoArgsConstructor
@Data
public class NewReaderDTO {
    @Schema(title = "Имя", description = "Имя читателя", defaultValue = "Иван", required = true, minLength = 2, maxLength = 12)
    private String name;
    @Schema(title = "Отчество", description = "Отчество читателя", defaultValue = "Иванович", minLength = 2, maxLength = 16)
    private String secondName;
    @Schema(title = "Фамилия", description = "Фамилия читателя", defaultValue = "Иванов", required = true, minLength = 2, maxLength = 30)
    private String surname;
    @Schema(title = "Номер билета", description = "Номер читательского билета", defaultValue = "1", required = true, minimum = "1", maximum = "2147483648")
    private int personalNumber;
}
```

```java

@NoArgsConstructor
@Data
public class CityDTO {
    private String name;
    private HashMap<String, String> local_names;
    private double lat;
    private double lon;
    private String country;
    private String state;
}
```

> [[**В начало**]](#пример-1)

```java

@NoArgsConstructor
@Data
public class WhetherDTO {
    private WhetherMain main;
    private BigDecimal visibility;
    private WhetherWind wind;
    private WhetherSunshine sys;
}
```

```java

@NoArgsConstructor
@Data
public class WhetherMain {
    private BigDecimal temp;
    private BigDecimal feels_like;
    private BigDecimal temp_min;
    private BigDecimal temp_max;
    private BigDecimal pressure;
    private BigDecimal humidity;
}
```

```java

@NoArgsConstructor
@Data
public class WhetherSunshine {
    private long sunrise;
    private long sunset;
}
```

> [[**В начало**]](#пример-1)

```java

@NoArgsConstructor
@Data
public class WhetherWind {
    private BigDecimal speed;
    private BigDecimal deg;
}
```

```java

@Schema(title = "Погода", description = "Информация о погоде")
@NoArgsConstructor
@Data
public class Whether {
    @Schema(title = "Температура, ºC", description = "Значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temperature;
    @Schema(title = "Ощущается, ºC", description = "Ощущается как температура, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal feels_like;
    @Schema(title = "Минимальная температура, ºC", description = "Минимальное зафиксированное значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temp_min;
    @Schema(title = "Максимальная температура, ºC", description = "Максимальное зафиксированное значение температуры, ºC", defaultValue = "0", minimum = "-273", maximum = "400")
    private BigDecimal temp_max;
    @Schema(title = "Давление, мм рт. ст.", description = "Значение атмосферного давления, мм рт. ст.", defaultValue = "746", minimum = "0", maximum = "1000")
    private BigDecimal pressure;
    @Schema(title = "Влажность, %", description = "Значение влажности воздуха, %", defaultValue = "50", minimum = "0", maximum = "100")
    private BigDecimal humidity;
    @Schema(title = "Видимость, м", description = "Значение прямой видимости, м", defaultValue = "5000", minimum = "0", maximum = "10000")
    private BigDecimal visibility;
    @Schema(title = "Скорость ветра, м/с", description = "Значение скорости ветра, м/с", defaultValue = "3", minimum = "0", maximum = "100")
    private BigDecimal wind_speed;
    @Schema(title = "Направление ветра, º", description = "Значение направления ветра, º", defaultValue = "0", minimum = "0", maximum = "360")
    private BigDecimal wind_deg;
    @Schema(title = "Восход", description = "Время восхода солнца", defaultValue = "06:00")
    private Time sunrise;
    @Schema(title = "Закат", description = "Время заката солнца", defaultValue = "20:00")
    private Time sunset;
}
```

### Создание мапперов

> [[**В начало**]](#пример-1)

