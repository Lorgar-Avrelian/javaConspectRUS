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
- [создание мапперов](#создание-мапперов) для _DTO_:
    * для сущностей для хранения аутентификационных данных пользователей;
    * для других сущностей, необходимых для работы приложения;
- [написание конфигураций для сервисов](#написание-конфигураций-для-сервисов):
    * конфигурации кеширования;
    * отправки _HTTP_-запросов;
    * других необходимых конфигураций;
- [создание сервисов и их реализаций](#создание-сервисов-и-их-реализаций) для логики приложения:
    * сервис и его реализация для аутентификации пользователей;
    * другие сервисы приложения и их реализации;
- [создание контроллеров](#создание-контроллеров):
    * перечень _API_ приложения;
    * создание контроллера авторизации со следующими эндпоинтами:
        + регистрации (`/register`);
        + входа (`/login`) - если создана, то должна быть отключена стандартная форма входа в настройках цепочки
          фильтров _SecurityFilterChain_;
        + БЕЗ точки выхода (`/logout`) - должна настраиваться в цепочке фильтров _SecurityFilterChain_;
        + получения _CSRF_-токена (`/csrf`) - если планируется включение _CSRF_-защиты в цепочке фильтров
          _SecurityFilterChain_;
        + другие эндпоинты для управления учётными данными пользователей;
    * создание контроллеров для отправки _HTML_-страниц;
    * создание _REST_-контроллеров приложения;
- создание хендлеров для контроллеров приложения;
- [применение _api-docs.yaml_](#применение-api-docsyaml) к редактору [_Swagger UI_](https://editor-next.swagger.io/) и
  сверка соответствия _API_ приложения документации;
- [написание дополнительных фильтров безопасности](#написание-дополнительных-фильтров-безопасности):
    * для разрешения использования учётных данных в межсайтовых запросах;
    * для тестирования с помощью _Postman_;
- [написание конфигурации безопасности](#написание-конфигурации-безопасности):
    * настройка цепочки безопасности _SecurityFilterChain_:
        + определение репозитория для хранения _CSRF_-токена;
        + включение поддержки _Basic_-аутентификации;
        + отключение стандартной формы входа;
        + настройка точки выхода;
        + настройка доступа к _API_ приложения;
        + настройка защиты от внутри-серверных переходов _CORS_;
        + настройка защиты от меж-сайтовых переходов _CSRF_ (необходимо разрешить все запросы из интерфейса _Swagger
          UI_);
        + настройка хендлера ошибок авторизации и перенаправлений;
        + включение фильтров в цепочку фильтров безопасности _SecurityFilterChain_;
        + сборка цепочки фильтров;
    * задание кодировщика паролей приложения _PasswordEncoder_;
    * переопределение провайдера аутентификации _AuthenticationProvider_;
    * переопределение менеджера аутентификации _AuthenticationManager_;
- [написание тестов](#написание-тестов):
    * написание конфигурации для тестов;
    * _unit_-тестов;
    * интеграционных тестов.

### Подключение зависимостей

> [[**В начало**]](#пример-1)

```xml

<properties>
    <java.version>23</java.version>
    <org.mapstruct.version>1.6.3</org.mapstruct.version>
    <org.lombok.version>1.18.36</org.lombok.version>
    <ch.qos.logback.version>1.5.12</ch.qos.logback.version>
    <spring-boot-maven-plugin.version>3.4.0</spring-boot-maven-plugin.version>
</properties>
```

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
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>${spring-boot-maven-plugin.version}</version>
            <configuration>
                <jvmArguments>-Dspring.application.admin.enabled=true</jvmArguments>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>start</goal>
                        <goal>stop</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-maven-plugin</artifactId>
            <version>1.4</version>
            <executions>
                <execution>
                    <id>integration-test</id>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
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
# Swagger UI option for permanent Try it out button enable
springdoc.swagger-ui.try-it-out-enabled=true
# Swagger UI option for turning on CSRF protection
springdoc.swagger-ui.csrf.enabled=true
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@AllArgsConstructor
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
@AllArgsConstructor
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
@AllArgsConstructor
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
@AllArgsConstructor
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
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthDTO {
    @Schema(title = "Логин", description = "Логин пользователя", defaultValue = "user@mail.ru", required = true, minLength = 3, maxLength = 30)
    private String login;
    @Schema(title = "Пароль", description = "Пароль пользователя", defaultValue = "123", required = true, minLength = 3)
    private String password;
}
```

```java

@Schema(title = "Логин", description = "Сущность логина пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
@Data
@NoArgsConstructor
@AllArgsConstructor
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

@Data
@NoArgsConstructor
@AllArgsConstructor
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhetherDTO {
    private WhetherMain main;
    private BigDecimal visibility;
    private WhetherWind wind;
    private WhetherSunshine sys;
}
```

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhetherSunshine {
    private long sunrise;
    private long sunset;
}
```

> [[**В начало**]](#пример-1)

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhetherWind {
    private BigDecimal speed;
    private BigDecimal deg;
}
```

```java

@Schema(title = "Погода", description = "Информация о погоде")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

- для сущностей для хранения аутентификационных данных пользователей:

```java

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginMapper {
    LoginDTO loginToLoginDTO(Login login);

    List<LoginDTO> loginListToLoginDTOList(List<Login> loginList);

    default Login registerDTOToLogin(RegisterDTO registerDTO) {
        Login login = new Login();
        login.setLogin(registerDTO.getLogin());
        login.setPassword(registerDTO.getPassword());
        return login;
    }
}
```

- для других сущностей, необходимых для работы приложения:

```java

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    @Mapping(target = "reader", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "newBookDTO.authorFIO")
    Book newBookDTOtoBook(NewBookDTO newBookDTO);

    default Collection<BookDTO> booksListToBookDTOList(Collection<Book> books) {
        Collection<BookDTO> booksDTO = new ArrayList<>();
        for (Book book : books) {
            booksDTO.add(bookToBookDTO(book));
        }
        return booksDTO.stream()
                       .sorted(Comparator.comparing(BookDTO::getId))
                       .toList();
    }

    BookDTO bookToBookDTO(Book book);

    default Book bookDTOToBook(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setYear(bookDTO.getYear());
        return book;
    }
}
```

```java

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReaderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "newReaderDTO.name")
    @Mapping(target = "secondName", source = "newReaderDTO.secondName")
    @Mapping(target = "surname", source = "newReaderDTO.surname")
    @Mapping(target = "personalNumber", source = "newReaderDTO.personalNumber")
    Reader newReaderDTOtoReader(NewReaderDTO newReaderDTO);

    @Mapping(target = "id", source = "reader.id")
    @Mapping(target = "name", source = "reader.name")
    @Mapping(target = "secondName", source = "reader.secondName")
    @Mapping(target = "surname", source = "reader.surname")
    @Mapping(target = "personalNumber", source = "reader.personalNumber")
    ReaderNoBooksDTO readerToNoBooksDTO(Reader reader);

    Collection<ReaderNoBooksDTO> readersToNoBooksDTOs(Collection<Reader> readers);
}
```

### Написание конфигураций для сервисов

> [[**В начало**]](#пример-1)

- конфигурации кеширования:

```java

@Configuration
@EnableCaching
public class CachingConfiguration {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(List.of(new ConcurrentMapCache("book")));
        return cacheManager;
    }
}
```

- отправки _HTTP_-запросов:

```java

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().build();
    }
}
```

- других необходимых конфигураций:

```java

@Configuration
public class RandomizeConfig {
    @Value("${rand.diapason}")
    private long seed;

    @Bean
    @Scope(value = "prototype")
    public Random myRandomInstance() {
        return new Random(seed);
    }

    @Bean
    @Primary
    @Scope(value = "singleton")
    public Random defaultInstance() {
        return new Random();
    }
}
```

### Создание сервисов и их реализаций

> [[**В начало**]](#пример-1)

- сервис и его реализация для аутентификации пользователей:

```java
/**
 * Сервис для управления данными авторизованных пользователей.<br>
 * Имеет следующие реализации:<br>
 * - {@link AuthorizationServiceImpl};
 *
 * @author Victor Tokovenko
 * @see AuthorizationServiceImpl
 */
@Service
public interface AuthorizationService {
    Login login(BasicAuthDTO basicAuthDTO);

    Login register(RegisterDTO registerDTO);

    List<LoginDTO> getAllUsers();

    List<LoginDTO> getAllUsers(int page, int size);

    LoginDTO setRole(UserDetails userDetails, long id, Role role) throws IllegalArgumentException;

    LoginDTO setPassword(UserDetails userDetails, long id, String password) throws IllegalArgumentException;

    LoginDTO delete(UserDetails userDetails, long id) throws IllegalArgumentException;
}
```

```java
/**
 * Реализация сервиса {@link AuthorizationService}.<br>
 * Сервис для управления учётными данными авторизованных пользователей.
 *
 * @author Victor Tokovenko
 * @see AuthorizationService
 */
@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class AuthorizationServiceImpl implements AuthorizationService {
    private final JpaUserDetailsServiceImpl userDetailsService;
    private final LoginRepository loginRepository;
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServiceImpl(@Qualifier(value = "jpaUserDetailsServiceImpl") JpaUserDetailsServiceImpl userDetailsService,
                                    LoginRepository loginRepository,
                                    LoginMapper loginMapper,
                                    @Lazy @Qualifier(value = "passwordEncoder") PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.loginRepository = loginRepository;
        this.loginMapper = loginMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Login login(BasicAuthDTO basicAuthDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(basicAuthDTO.getLogin()).orElse(null);
        if (login != null) {
            if (passwordEncoder.matches(basicAuthDTO.getPassword(), login.getPassword())) {
                userDetailsService.loadUserByUsername(basicAuthDTO.getLogin());
                return login;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Login register(RegisterDTO registerDTO) {
        Login login = loginRepository.findByLoginEqualsIgnoreCase(registerDTO.getLogin()).orElse(null);
        if (login != null || !registerDTO.getPassword().equals(registerDTO.getPasswordConfirmation())) {
            return null;
        }
        login = loginMapper.registerDTOToLogin(registerDTO);
        login.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        if (loginRepository.count() == 0) {
            login.setRole(Role.ROLE_OWNER);
        } else {
            login.setRole(Role.ROLE_USER);
        }
        login = loginRepository.save(login);
        return login;
    }

    @Override
    public List<LoginDTO> getAllUsers() {
        return loginMapper.loginListToLoginDTOList(loginRepository.findAll()).stream()
                          .sorted(Comparator.comparing(LoginDTO::getId))
                          .toList();
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"})
    public List<LoginDTO> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Login> logins = loginRepository.findAll(pageRequest);
        return loginMapper.loginListToLoginDTOList(logins.getContent());
    }

    @Override
    @PreAuthorize("hasRole('OWNER')")
    public LoginDTO setRole(UserDetails userDetails, long id, Role role) throws IllegalArgumentException {
        if (role == Role.ROLE_OWNER) {
            throw new IllegalArgumentException("Should be only one of ROLE_OWNER");
        }
        Login login = loginRepository.findById(id).orElse(null);
        if (login != null) {
            login.setRole(role);
            login = loginRepository.save(login);
            return loginMapper.loginToLoginDTO(login);
        } else {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
    }

    @Override
    @Secured({"ROLE_USER", "ROLE_ADMIN", "ROLE_OWNER"})
    public LoginDTO setPassword(UserDetails userDetails, long id, String password) throws IllegalArgumentException {
        Login login = loginRepository.findById(id).orElse(null);
        Login user = loginRepository.findByLoginEqualsIgnoreCase(userDetails.getUsername()).get();
        if (login != null && (login.getLogin().equals(user.getLogin()) || user.getRole().compareTo(login.getRole()) > 0)) {
            login.setPassword(passwordEncoder.encode(password));
            login = loginRepository.save(login);
            return loginMapper.loginToLoginDTO(login);
        } else {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public LoginDTO delete(UserDetails userDetails, long id) throws IllegalArgumentException {
        if (id == 1) {
            log.info("User " + userDetails.getUsername() + " tried to delete owner!");
            throw new IllegalArgumentException("Owner could not be deleted!");
        }
        Login user = loginRepository.findByLoginEqualsIgnoreCase(userDetails.getUsername()).get();
        Login login = loginRepository.findById(id).orElse(null);
        if (login == null) {
            log.info("User with ID " + id + " is not found");
            throw new IllegalArgumentException("User with ID " + id + " is not found");
        }
        if ((user.getRole().compareTo(login.getRole())) > 0) {
            loginRepository.deleteById(id);
            return loginMapper.loginToLoginDTO(login);
        } else {
            return null;
        }
    }
}
```

- другие сервисы приложения и их реализации:

> [[**В начало**]](#пример-1)

```java
/**
 * Service for making CRUD operations with {@link Book} entities by using {@link JpaRepository} methods.
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 * @see BookServiceImplDB
 */
@Service
public interface BookService {
    /**
     * Method is saving new {@link Book} to DB and returning entity of the saved {@link Book}.
     *
     * @param newBookDTO that should be saved
     * @return {@code book} - that is saved
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#createBook(NewBookDTO)
     */
    Book createBook(NewBookDTO newBookDTO);

    /**
     * Method is returning entity of the {@link Book} by its ID.
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is founded or {@link null} if {@link Book} is not founded
     * @see BookServiceImplDB#findBook(long)
     */
    Book findBook(long id);

    /**
     * Method is saving edited {@link BookDTO} and returning entity of the saved {@link Book}.
     *
     * @param book that should be edited
     * @return {@code book} - that is saved or {@link null} if {@link Book} is not founded
     * @see BookServiceImplDB#editBook(BookDTO)
     */
    Book editBook(BookDTO book);

    /**
     * Method is deleting {@link Book} and returning entity of the deleted {@link Book}.
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is deleted or {@link null} if {@link Book} is not founded
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#deleteBook(long)
     */
    Book deleteBook(long id);

    /**
     * Method is returning {@link Collection} of all {@link Book}.
     *
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#getAllBooks()
     */
    Collection<Book> getAllBooks();

    /**
     * Method is returning {@link Collection} of all {@link Book}.
     *
     * @param authorOrTitle part of author name or book title
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when error received
     * @see BookServiceImplDB#getAllBooks(String)
     */
    Collection<Book> getAllBooks(String authorOrTitle);
}
```

```java
/**
 * An implementation of {@link BookService}.<br>
 * Service for making CRUD operations with {@link Book} entities by using {@link JpaRepository} methods
 *
 * @author Victor Tokovenko
 * @see JpaRepository
 * @see BookService
 */
@Service
public class BookServiceImplDB implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImplDB(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Method is saving new {@link Book} to DB and returning entity of the saved {@link Book} from DB.<br>
     * Using {@link JpaRepository#save(Object)} method
     *
     * @param newBookDTO that should be saved
     * @return {@code book} - that is saved
     * @throws RuntimeException when DB is not accessible
     * @see JpaRepository#save(Object)
     */
    @Override
    public Book createBook(NewBookDTO newBookDTO) {
        Book book = bookMapper.newBookDTOtoBook(newBookDTO);
        return bookRepository.save(book);
    }

    /**
     * Method is returning entity of the {@link Book} from DB by its ID.<br>
     * Using {@link JpaRepository#findById(Object)}
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is founded or {@link null} if {@link Book} is not founded
     * @see JpaRepository#findById(Object)
     */
    @Override
    public Book findBook(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    /**
     * Method is saving edited {@link Book} to DB and returning entity of the saved {@link Book} from DB.<br>
     * Using {@link JpaRepository#save(Object)} and {@link JpaRepository#existsById(Object)} methods
     *
     * @param book that should be edited
     * @return {@code book} - that is saved or {@link null} if {@link Book} is not founded
     * @see JpaRepository#save(Object)
     * @see JpaRepository#existsById(Object)
     */
    @Override
    public Book editBook(BookDTO book) {
        if (bookRepository.existsById(book.getId())) {
            Book dbBook = bookRepository.findById(book.getId()).get();
            Book editedBook = bookMapper.bookDTOToBook(book);
            editedBook.setReader(dbBook.getReader());
            return bookRepository.save(editedBook);
        } else {
            return null;
        }
    }

    /**
     * Method is deleting {@link Book} from DB and returning entity of the deleted {@link Book}.<br>
     * Using {@link BookServiceImplDB#findBook(long)}, {@link JpaRepository#deleteById(Object)} and {@link JpaRepository#existsById(Object)} methods
     *
     * @param id of required {@link Book}
     * @return {@code book} - that is deleted or {@link null} if {@link Book} is not founded
     * @throws RuntimeException when DB is not accessible
     * @see BookServiceImplDB#findBook(long)
     * @see JpaRepository#deleteById(Object)
     * @see JpaRepository#existsById(Object)
     */
    @Override
    public Book deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            Book book = findBook(id);
            bookRepository.deleteById(id);
            return book;
        } else {
            return null;
        }
    }

    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.<br>
     * Using {@link JpaRepository#findAll()} method
     *
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when DB is not accessible
     * @see JpaRepository#findAll()
     */
    @Override
    public Collection<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Method is returning {@link Collection} of all {@link Book} from DB.<br>
     * Using {@link BookRepository#findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String, String)} method
     *
     * @param authorOrTitle part of author name or book title
     * @return {@code Collection<Book>} of founded books
     * @throws RuntimeException when DB is not accessible
     * @see BookRepository#findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(String, String)
     */
    @Override
    public Collection<Book> getAllBooks(String authorOrTitle) {
        return bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(authorOrTitle, authorOrTitle);
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface BookCoverService {
    BookCover uploadCover(long bookId, MultipartFile file);

    BookCover getBookCover(long id);

    void downloadCover(long bookId, HttpServletResponse response);

    BookCover saveBookCover(BookCover bookCover);

    void deleteBookCover(long id);
}
```

```java

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
@Log
public class BookCoverServiceImpl implements BookCoverService {
    @Value("${books.covers.dir.path}")
    private String coversDir;
    private final BookService bookService;
    private final BookCoverRepository bookCoverRepository;

    public BookCoverServiceImpl(@Qualifier("bookServiceImplDB") BookService bookService, BookCoverRepository bookCoverRepository) {
        this.bookService = bookService;
        this.bookCoverRepository = bookCoverRepository;
    }

    @Override
    public BookCover uploadCover(long bookId, MultipartFile file) {
        Book book = bookService.findBook(bookId);
        if (book == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        Path filePath;
        if (fileName != null && !fileName.isEmpty()) {
            filePath = Path.of(coversDir, bookId + "." + getExtension(fileName));
        } else {
            return null;
        }
        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            return null;
        }
        try (
                InputStream inputStream = file.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
        ) {
            bufferedInputStream.transferTo(bufferedOutputStream);
        } catch (IOException e) {
            return null;
        }
        BookCover bookCover = getBookCover(bookId);
        bookCover.setId(bookId);
        bookCover.setFilePath(filePath.toString());
        bookCover.setFileSize((int) file.getSize());
        bookCover.setMediaType(file.getContentType());
        bookCover.setImagePreview(generatePreview(filePath));
        bookCover.setBook(book);
        bookCover = saveBookCover(bookCover);
        if (bookCover.getId() == 0) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                return bookCover;
            }
        }
        return bookCover;
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Override
    public BookCover getBookCover(long id) {
        return bookCoverRepository.findByBookId(id).orElse(new BookCover());
    }

    private byte[] generatePreview(Path path) {
        try (
                InputStream inputStream = Files.newInputStream(path);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ) {
            BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            BufferedImage preview;
            if (height > width) {
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 200);
                preview = new BufferedImage(200, height, bufferedImage.getType());
                width = 200;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 200);
                preview = new BufferedImage(width, 200, bufferedImage.getType());
                height = 200;
            }
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(preview, getExtension(path.getFileName().toString()), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void downloadCover(long bookId, HttpServletResponse response) {
        BookCover bookCover = getBookCover(bookId);
        if (bookCover.getImagePreview() != null) {
            Path filePath = Path.of(bookCover.getFilePath());
            try (
                    InputStream inputStream = Files.newInputStream(filePath);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
                    OutputStream outputStream = response.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream, 1024)
            ) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(bookCover.getMediaType());
                response.setContentLength(bookCover.getFileSize());
                bufferedInputStream.transferTo(bufferedOutputStream);
            } catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public BookCover saveBookCover(BookCover bookCover) {
        return bookCoverRepository.save(bookCover);
    }

    @Override
    public void deleteBookCover(long id) {
        bookCoverRepository.deleteById(id);
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface ReaderService {
    ReaderNoBooksDTO createReader(NewReaderDTO newReader);

    ReaderNoBooksDTO findReader(long id);

    Reader findDBReader(long id);

    ReaderNoBooksDTO editReader(Reader reader);

    ReaderNoBooksDTO deleteReader(long id);

    Collection<ReaderNoBooksDTO> getAllReaders();

    Collection<Reader> getAllDBReaders();

    Collection<ReaderNoBooksDTO> getAllReaders(String partOfNameSecondNameOrSurname);

    Collection<ReaderNoBooksDTO> getReaderByName(String partOfName);

    Collection<ReaderNoBooksDTO> getReaderBySecondName(String partOfSecondName);

    Collection<ReaderNoBooksDTO> getReaderBySurname(String partOfSurname);

    Collection<BookDTO> getReaderBooks(long id);
}
```

```java

@Service
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepository;
    private final BookMapper bookMapper;
    private final ReaderMapper readerMapper;
    private final ManageService manageService;

    public ReaderServiceImpl(ReaderRepository readerRepository,
                             BookMapper bookMapper,
                             ReaderMapper readerMapper,
                             @Lazy @Qualifier(value = "manageServiceImpl") ManageService manageService) {
        this.readerRepository = readerRepository;
        this.bookMapper = bookMapper;
        this.readerMapper = readerMapper;
        this.manageService = manageService;
    }

    @Override
    public ReaderNoBooksDTO createReader(NewReaderDTO newReader) {
        Reader reader = readerMapper.newReaderDTOtoReader(newReader);
        return readerMapper.readerToNoBooksDTO(readerRepository.save(reader));
    }

    @Override
    public ReaderNoBooksDTO findReader(long id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        if (reader != null) {
            reader.setBooks(manageService.findReaderBooks(reader.getId()));
            return readerMapper.readerToNoBooksDTO(reader);
        } else {
            return null;
        }
    }

    @Override
    public Reader findDBReader(long id) {
        Reader reader = readerRepository.findById(id).orElse(null);
        if (reader != null) {
            reader.setBooks(manageService.findReaderBooks(reader.getId()));
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public ReaderNoBooksDTO editReader(Reader reader) {
        if (readerRepository.existsById(reader.getId())) {
            reader = readerRepository.save(reader);
            reader.setBooks(manageService.findReaderBooks(reader.getId()));
            return readerMapper.readerToNoBooksDTO(reader);
        } else {
            return null;
        }
    }

    @Override
    public ReaderNoBooksDTO deleteReader(long id) {
        if (readerRepository.existsById(id)) {
            ReaderNoBooksDTO reader = findReader(id);
            readerRepository.deleteById(id);
            return reader;
        } else {
            return null;
        }
    }

    @Override
    public Collection<ReaderNoBooksDTO> getAllReaders() {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findAll());
    }

    @Override
    public Collection<Reader> getAllDBReaders() {
        return readerRepository.findAll();
    }

    @Override
    public Collection<ReaderNoBooksDTO> getAllReaders(String partOfNameSecondNameOrSurname) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findByNameContainsIgnoreCaseOrSecondNameContainsIgnoreCaseOrSurnameContainsIgnoreCase(partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname, partOfNameSecondNameOrSurname));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderByName(String partOfName) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findByNameContainsIgnoreCase(partOfName));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderBySecondName(String partOfSecondName) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findBySecondNameContainsIgnoreCase(partOfSecondName));
    }

    @Override
    public Collection<ReaderNoBooksDTO> getReaderBySurname(String partOfSurname) {
        return readerMapper.readersToNoBooksDTOs(readerRepository.findBySurnameContainsIgnoreCase(partOfSurname));
    }

    @Override
    public Collection<BookDTO> getReaderBooks(long id) {
        return bookMapper.booksListToBookDTOList(manageService.findReaderBooks(id));
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface ManageService {
    Reader giveBookToReader(long bookId, long readerId);

    Collection<Book> findReaderBooks(long readerId);

    Reader takeBookFromReader(long bookId, long readerId);
}
```

```java

@Service
public class ManageServiceImpl implements ManageService {
    private Logger logger = LoggerFactory.getLogger(ManageServiceImpl.class);
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public ManageServiceImpl(BookRepository bookRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public Reader giveBookToReader(long bookId, long readerId) {
        logger.debug("Give to reader " + readerId + " book " + bookId);
        Reader reader = readerRepository.findById(readerId).get();
        Book book = bookRepository.findById(bookId).get();
        if (book != null && reader != null && book.getReader() == null) {
            book.setReader(reader);
        } else {
            logger.error("Book " + bookId + " or reader " + readerId + " not found");
            return null;
        }
        return setReaderBooks(book, reader);
    }

    @Override
    public Collection<Book> findReaderBooks(long readerId) {
        logger.debug("Find reader " + readerId);
        return bookRepository.findByReaderId(readerId);
    }

    @Override
    public Reader takeBookFromReader(long bookId, long readerId) {
        logger.trace("Take book " + bookId + " from reader " + readerId);
        Reader reader = readerRepository.findById(readerId).get();
        Book book = bookRepository.findById(bookId).get();
        if (book != null && reader != null && reader.equals(book.getReader())) {
            book.setReader(null);
            Collection<Book> books = reader.getBooks();
            books.remove(book);
            reader.setBooks(books);
        } else {
            logger.error("Book " + bookId + " or reader " + readerId + " not found");
            return null;
        }
        return setReaderBooks(book, reader);
    }

    private Reader setReaderBooks(Book book, Reader reader) {
        bookRepository.save(book);
        reader = readerRepository.save(reader);
        logger.info("Book " + book.getId() + " has been edited");
        logger.trace("Reader " + reader.getId() + " had been edited");
        return reader;
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface WhetherService {
    Collection<City> getCityInfo(String city);

    Collection<Whether> getWhether(String city, String country);
}
```

```java

@Service
public class WhetherServiceImpl implements WhetherService {
    @Value("${whether.api.key}")
    private String apiKey;
    @Value("${whether.geo.url}")
    private String geoUrl;
    @Value("${whether.current.url}")
    private String currentWhetherUrl;
    @Value("${whether.geo.result.count}")
    private int resultCount;
    private ObjectMapper mapper = new ObjectMapper();
    private Logger logger = LoggerFactory.getLogger(WhetherServiceImpl.class);
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;

    public WhetherServiceImpl(CityRepository cityRepository, RestTemplate restTemplate) {
        this.cityRepository = cityRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Collection<City> getCityInfo(String city) {
        String uri = geoUrl + "?q=" + city + "&limit=" + resultCount + "&appid=" + apiKey;
        List<City> citiesList = cityRepository.findByLocalNamesContainsIgnoreCase(city);
        if (citiesList.isEmpty()) {
            CityDTO[] cities = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity(HttpHeaders.EMPTY), CityDTO[].class).getBody();
            CityDTO cityDTO;
            HashMap<String, String> localNames = new HashMap<>();
            if (cities.length == 0) {
                return null;
            } else {
                for (int i = 0; i < cities.length; i++) {
                    cityDTO = cities[i];
                    City cityDAO = new City();
                    cityDAO.setCountry(cityDTO.getCountry());
                    cityDAO.setLat(BigDecimal.valueOf(cityDTO.getLat()));
                    cityDAO.setLon(BigDecimal.valueOf(cityDTO.getLon()));
                    cityDAO.setName(cityDTO.getName());
                    if (cityDTO.getState() != null) {
                        cityDAO.setState(cityDTO.getState());
                    } else {
                        cityDAO.setState("N/A");
                    }
                    if (localNames.isEmpty()) {
                        localNames = cityDTO.getLocal_names();
                    }
                    if (cityDTO.getLocal_names() == null || cityDTO.getLocal_names().isEmpty()) {
                        cityDTO.setLocal_names(localNames);
                    }
                    if (cityDTO.getLocal_names().containsValue(localNames.get("en")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("es")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("fr")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("it")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("de")) ||
                            cityDTO.getLocal_names().containsValue(localNames.get("ru")) ||
                            cityDTO.getLocal_names().containsValue(city)) {
                        localNames.putAll(cityDTO.getLocal_names());
                        try {
                            cityDAO.setLocalNames(mapper.writeValueAsString(localNames));
                        } catch (JsonProcessingException e) {
                            logger.warn("Converting value error: " + e.getMessage());
                            continue;
                        }
                    } else {
                        try {
                            cityDAO.setLocalNames(mapper.writeValueAsString(cityDTO.getLocal_names()));
                        } catch (JsonProcessingException e) {
                            logger.warn("Converting value error: " + e.getMessage());
                            continue;
                        }
                    }
                    cityRepository.save(cityDAO);
                }
                return cityRepository.findByLocalNamesContainsIgnoreCase(city);
            }
        } else {
            return citiesList;
        }
    }

    @Override
    public Collection<Whether> getWhether(String city, String country) {
        Collection<City> cities = getCityInfo(city);
        if (cities.isEmpty()) {
            return null;
        }
        cities = cityRepository.findByLocalNamesContainsIgnoreCaseAndCountryEqualsIgnoreCase(city, country);
        if (cities.isEmpty()) {
            return null;
        }
        BigDecimal lat;
        BigDecimal lon;
        List<Whether> whethers = new ArrayList<>();
        Whether whether = new Whether();
        for (City city1 : cities) {
            lat = city1.getLat();
            lon = city1.getLon();
            String uri = currentWhetherUrl + "?units=metric&lat=" + lat + "&lon=" + lon + "&appid=" + apiKey;
            WhetherDTO whetherDTO = restTemplate.getForObject(uri, WhetherDTO.class);
            if (whetherDTO != null) {
                whether.setTemperature(whetherDTO.getMain().getTemp());
                whether.setFeels_like(whetherDTO.getMain().getFeels_like());
                whether.setTemp_min(whetherDTO.getMain().getTemp_min());
                whether.setTemp_max(whetherDTO.getMain().getTemp_max());
                whether.setPressure(whetherDTO.getMain().getPressure().multiply(BigDecimal.valueOf(0.7500615758456601)));
                whether.setHumidity(whetherDTO.getMain().getHumidity());
                whether.setVisibility(whetherDTO.getVisibility());
                whether.setWind_speed(whetherDTO.getWind().getSpeed());
                whether.setWind_deg(whetherDTO.getWind().getDeg());
                whether.setSunrise(new Time(whetherDTO.getSys().getSunrise() * 1000));
                whether.setSunset(new Time(whetherDTO.getSys().getSunset() * 1000));
                whethers.add(whether);
            }
        }
        return whethers;
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface ExpenseService {
    Collection<Expense> getAllExpenses();

    Expense getExpense(int id);

    Expense addExpense(Expense expense);

    Collection<ExpensesByCategory> getExpensesByCategories();

    Collection<Expense> getAllExpenses(int page, int size);
}
```

```java

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Collection<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Collection<Expense> getAllExpenses(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Expense> resultPage = expenseRepository.findAll(pageRequest);
        return resultPage.getContent();
    }

    @Override
    public Expense getExpense(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Collection<ExpensesByCategory> getExpensesByCategories() {
        return expenseRepository.getExpenseByCategories();
    }
}
```

> [[**В начало**]](#пример-1)

```java

@Service
public interface CounterService {
    int getCounter();

    int setCounter(int counter);
}
```

```java

@Service
public class CounterServiceImpl implements CounterService {
    private static int counter = 0;

    public CounterServiceImpl() {
    }

    @Override
    public int getCounter() {
        return ++counter;
    }

    @Override
    public int setCounter(int counter) {
        this.counter = counter;
        return this.counter;
    }
}
```

```java

@Service
public interface RandomService {
    Integer getRandomNextInt();
}
```

```java

@Service
public class RandomServiceImpl implements RandomService {
    private final Random random;

    public RandomServiceImpl(Random random) {
        this.random = random;
    }

    @Override
    public Integer getRandomNextInt() {
        return random.nextInt();
    }
}
```

### Создание контроллеров

> [[**В начало**]](#пример-1)

- перечень _API_ приложения:

```text
0 Приветствие - HelloController
GET    http://localhost:8080

1 Авторизация - AuthorizationController
POST   http://localhost:8080/login
POST   http://localhost:8080/register
POST   http://localhost:8080/logout
GET    http://localhost:8080/users
GET    http://localhost:8080/csrf
PATCH  http://localhost:8080/set-role
PATCH  http://localhost:8080/set-password
DELETE http://localhost:8080/delete
DELETE http://localhost:8080/csrf

2 Книги - BooksController
POST   http://localhost:8080/books
GET    http://localhost:8080/books/{id}
PUT    http://localhost:8080/books
DELETE http://localhost:8080/books/{id}
GET    http://localhost:8080/books
POST   http://localhost:8080/books/{id}/cover
GET    http://localhost:8080/books/{id}/cover/preview
GET    http://localhost:8080/books/{id}/cover

3 Читатели - ReaderController
POST   http://localhost:8080/readers
GET    http://localhost:8080/readers/{id}
PUT    http://localhost:8080/readers
DELETE http://localhost:8080/readers/{id}
GET    http://localhost:8080/readers
GET    http://localhost:8080/readers/name
GET    http://localhost:8080/readers/secondName
GET    http://localhost:8080/readers/surname
GET    http://localhost:8080/readers/books

4 Приём/выдача книг - ManageController
POST   http://localhost:8080/manage
GET    http://localhost:8080/manage

5 Погода - WhetherController
GET    http://localhost:8080/whether/city-info
GET    http://localhost:8080/whether

6 Затраты - ExpensesController
GET    http://localhost:8080/expenses
GET    http://localhost:8080/expenses/{id}
POST   http://localhost:8080/expenses
GET    http://localhost:8080/expenses/categories

7 Счётчик - CounterController
GET    http://localhost:8080/counter
GET    http://localhost:8080/counter/change
GET    http://localhost:8080/counter/change/{counter}

8 Рандом - RandomizeController
GET    http://localhost:8080/random
```

> [[**В начало**]](#пример-1)

- создание контроллера авторизации:

```java

@RestController
@Tag(name = "1 Авторизация", description = "Контроллер для авторизации пользователей")
@Log
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(@Qualifier(value = "authorizationServiceImpl") AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping(path = "/login")                    // http://localhost:8080/login
    @Operation(
            summary = "Войти",
            description = "Пройти авторизацию",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> login(@RequestBody @Parameter(description = "Логин и пароль", required = true, schema = @Schema(implementation = BasicAuthDTO.class)) BasicAuthDTO basicAuthDTO) {
        Login login = authorizationService.login(basicAuthDTO);
        if (login != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(login.getLogin(), login.getPassword());
            return ResponseEntity.ok().headers(headers).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(path = "/register")                 // http://localhost:8080/register
    @Operation(
            summary = "Регистрация",
            description = "Пройти регистрацию",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> register(@RequestBody @Parameter(description = "Логин, пароль и подтверждение пароля", required = true, schema = @Schema(implementation = RegisterDTO.class)) RegisterDTO registerDTO) {
        Login login = authorizationService.register(registerDTO);
        if (login != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(login.getLogin(), login.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping(path = "/users")                     // http://localhost:8080/users
    @Operation(
            summary = "Пользователи",
            description = "Список зарегистрированных пользователей",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = LoginDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<LoginDTO>> getUsers(@RequestParam(required = false) @Parameter(description = "Номер страницы", schema = @Schema(implementation = Integer.class)) Integer page,
                                                         @RequestParam(required = false) @Parameter(description = "Количество пользователей на странице", schema = @Schema(implementation = Integer.class)) Integer size) {
        Collection<LoginDTO> users;
        if (page == null && size == null) {
            users = authorizationService.getAllUsers();
        } else if (page != null && size != null && size > 0 && page > 0) {
            users = authorizationService.getAllUsers(page, size);
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (users.isEmpty()) {
            return ResponseEntity.status(403).build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PatchMapping(path = "/set-role")                // http://localhost:8080/set-role
    @Operation(
            summary = "Права",
            description = "Смена прав для зарегистрированного пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<LoginDTO> setRole(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                            @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id,
                                            @RequestParam @Parameter(description = "Новая роль пользователя", required = true, schema = @Schema(implementation = Role.class)) Role role) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.setRole(userDetails, id, role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PatchMapping(path = "/set-password")            // http://localhost:8080/set-password
    @Operation(
            summary = "Пароль",
            description = "Смена пароля для зарегистрированного пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<LoginDTO> setPassword(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                                @RequestParam @Parameter(description = "ID пользователя", schema = @Schema(implementation = Long.class)) long id,
                                                @RequestParam @Parameter(description = "Новый пароль пользователя", required = true, schema = @Schema(implementation = String.class)) String password) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.setPassword(userDetails, id, password);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping(path = "/delete")                   // http://localhost:8080/delete
    @Operation(
            summary = "Удалить",
            description = "Удалить пользователя",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = LoginDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<LoginDTO> delete(@AuthenticationPrincipal @Parameter(description = "Учётные данные пользователя", required = true, schema = @Schema(implementation = UserDetails.class)) UserDetails userDetails,
                                           @RequestParam @Parameter(description = "ID пользователя", required = true, schema = @Schema(implementation = Long.class)) long id) {
        LoginDTO loginDTO;
        try {
            loginDTO = authorizationService.delete(userDetails, id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
        if (loginDTO != null) {
            return ResponseEntity.ok(loginDTO);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(path = "/csrf")                       // http://localhost:8080/csrf
    @Operation(
            summary = "CSRF-токен",
            description = "Получить CSRF-токен",
            tags = "Безопасность",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    schema = @Schema(implementation = CsrfToken.class)
                            )
                    )
            }
    )
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }
}
```

> [[**В начало**]](#пример-1)

- создание контроллеров для отправки _HTML_-страниц:

```java

@Controller
@RequestMapping
@Tag(name = "0 Приветствие", description = "Стартовая страница")
public class HelloController {
    @GetMapping
    @Operation(
            summary = "Приветствие",
            description = "Увидеть стартовую страницу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_HTML_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ModelAndView getHello() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }
}
```

> [[**В начало**]](#пример-1)

- создание _REST_-контроллеров приложения:

```java

@RestController
@RequestMapping(path = "/books")
@Tag(name = "2 Книги", description = "Контроллер для работы с книгами")
@CacheConfig(cacheNames = "book")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class BooksController {
    private final BookService bookService;
    private final BookCoverService bookCoverService;
    private final BookMapper bookMapper;

    public BooksController(@Qualifier("bookServiceImplDB") BookService bookService, @Qualifier("bookCoverServiceImpl") BookCoverService bookCoverService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookCoverService = bookCoverService;
        this.bookMapper = bookMapper;
    }

    @PostMapping                    // http://localhost:8080/books      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method Not Allowed",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<BookDTO> createBook(@RequestBody NewBookDTO book) {
        try {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(bookService.createBook(book)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(405).build();
        }
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/books/1    R - read
    @Operation(
            summary = "Найти",
            description = "Найти информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @Cacheable(value = "book")
    public ResponseEntity<BookDTO> readBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Book findedBook = bookService.findBook(id);
        if (findedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(findedBook));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/books      U - update
    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о книге",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @CachePut(value = "book", key = "#book.id", unless = "#result.body.id > 100")
    public ResponseEntity<BookDTO> updateBook(@RequestBody BookDTO book) {
        Book updatedBook = bookService.editBook(book);
        if (updatedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(updatedBook));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/books/1    D - delete
    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о книге по ID",
            tags = "Книга",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    @CacheEvict(value = "book")
    public ResponseEntity<BookDTO> deleteBook(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Book deletedBook = bookService.deleteBook(id);
        if (deletedBook != null) {
            return ResponseEntity.ok(bookMapper.bookToBookDTO(deletedBook));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список книг",
            description = "Вывести список всех доступных книг",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<BookDTO>> getAllBooks(@RequestParam(required = false) @Parameter(description = "Часть ФИО автора или названия книги", schema = @Schema(implementation = String.class), example = "Пушкин") String authorOrTitle) {
        Collection<Book> books;
        if (authorOrTitle == null) {
            books = bookService.getAllBooks();
        } else {
            books = bookService.getAllBooks(authorOrTitle);
        }
        if (!books.isEmpty()) {
            return ResponseEntity.status(200).body(bookMapper.booksListToBookDTOList(books));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Загрузить",
            description = "Загрузить обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> uploadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class)) long id, @RequestParam @Parameter(description = "Изображение обложки книги", required = true, schema = @Schema(implementation = MultipartFile.class)) MultipartFile file) {
        if (file.getSize() > 1024 * 1000) {
            return ResponseEntity.badRequest().build();
        }
        BookCover bookCover = bookCoverService.uploadCover(id, file);
        if (bookCover != null) {
            return getCover(bookCover);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{id}/cover/preview")
    @Operation(
            summary = "Посмотреть превью",
            description = "Посмотреть превью обложки для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Byte.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<byte[]> downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        BookCover bookCover = bookCoverService.getBookCover(id);
        return getCover(bookCover);
    }

    private ResponseEntity<byte[]> getCover(BookCover bookCover) {
        byte[] preview = bookCover.getImagePreview();
        if (preview != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(bookCover.getMediaType()));
            httpHeaders.setContentLength(preview.length);
            return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(preview);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping(path = "/{id}/cover")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть обложку для книги",
            tags = "Изображения",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                                    schema = @Schema(implementation = BookCover.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "502",
                            description = "Bad Gateway",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public void downloadCover(@PathVariable @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long id, HttpServletResponse response) {
        bookCoverService.downloadCover(id, response);
    }
}
```

> [[**В начало**]](#пример-1)

```java

@RestController
@RequestMapping(path = "/readers")
@Tag(name = "3 Читатели", description = "Контроллер для работы с читателями")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class ReaderController {
    private final ReaderService readerService;

    public ReaderController(@Qualifier("readerServiceImpl") ReaderService readerService) {
        this.readerService = readerService;
    }

    @PostMapping                    // http://localhost:8080/readers      C - create
    @Operation(
            summary = "Создать",
            description = "Добавить информацию о читателе",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReaderNoBooksDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Method Not Allowed",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReaderNoBooksDTO> createReader(@RequestBody NewReaderDTO reader) {
        try {
            ReaderNoBooksDTO readerDTO = readerService.createReader(reader);
            if (readerDTO != null) {
                return ResponseEntity.ok(readerDTO);
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/readers/1    R - read
    @Operation(
            summary = "Найти",
            description = "Найти информацию о читателе по ID",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReaderNoBooksDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReaderNoBooksDTO> readReader(@PathVariable @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        ReaderNoBooksDTO findedReader = readerService.findReader(id);
        if (findedReader != null) {
            return ResponseEntity.ok(findedReader);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/readers      U - update
    @Operation(
            summary = "Редактировать",
            description = "Отредактировать информацию о читателе",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReaderNoBooksDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReaderNoBooksDTO> updateReader(@RequestBody Reader reader) {
        ReaderNoBooksDTO updatedReader = readerService.editReader(reader);
        if (updatedReader != null) {
            return ResponseEntity.ok(updatedReader);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/readers/1    D - delete
    @Operation(
            summary = "Удалить",
            description = "Удалить информацию о читателе по ID",
            tags = "Читатель",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReaderNoBooksDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<ReaderNoBooksDTO> deleteReader(@PathVariable @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        ReaderNoBooksDTO deletedReader = readerService.deleteReader(id);
        if (deletedReader != null) {
            return ResponseEntity.ok(deletedReader);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Список читателей",
            description = "Вывести список всех зарегистрированных читателей",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReaderNoBooksDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ReaderNoBooksDTO>> getAllReaders(@RequestParam(required = false) @Parameter(description = "Часть ФИО читателя", schema = @Schema(implementation = String.class), example = "Иван") String partOfNameSecondNameOrSurname) {
        Collection<ReaderNoBooksDTO> readers;
        if (partOfNameSecondNameOrSurname == null) {
            readers = readerService.getAllReaders();
        } else {
            readers = readerService.getAllReaders(partOfNameSecondNameOrSurname);
        }
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/name")
    @Operation(
            summary = "Список по имени",
            description = "Вывести список всех зарегистрированных читателей с указанным именем",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReaderNoBooksDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ReaderNoBooksDTO>> getReadersByName(@RequestParam @Parameter(description = "Часть имени читателя", schema = @Schema(implementation = String.class), example = "Иван") String partOfName) {
        Collection<ReaderNoBooksDTO> readers = readerService.getReaderByName(partOfName);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/secondName")
    @Operation(
            summary = "Список по отчеству",
            description = "Вывести список всех зарегистрированных читателей с указанным отчеством",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReaderNoBooksDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ReaderNoBooksDTO>> getReadersBySecondName(@RequestParam @Parameter(description = "Часть отчества читателя", schema = @Schema(implementation = String.class), example = "Иванович") String partOfSecondName) {
        Collection<ReaderNoBooksDTO> readers = readerService.getReaderBySecondName(partOfSecondName);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/surname")
    @Operation(
            summary = "Список по фамилии",
            description = "Вывести список всех зарегистрированных читателей с указанной фамилией",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ReaderNoBooksDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ReaderNoBooksDTO>> getReadersBySurname(@RequestParam @Parameter(description = "Часть фамилии читателя", schema = @Schema(implementation = String.class), example = "Иванов") String partOfSurname) {
        Collection<ReaderNoBooksDTO> readers = readerService.getReaderBySurname(partOfSurname);
        if (!readers.isEmpty()) {
            return ResponseEntity.status(200).body(readers);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/books")
    @Operation(
            summary = "Список книг",
            description = "Вывести список всех книг данного читателя",
            tags = "Поиск",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<BookDTO>> getReaderBooks(@RequestParam @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long id) {
        Collection<BookDTO> books = readerService.getReaderBooks(id);
        if (!books.isEmpty()) {
            return ResponseEntity.status(200).body(books);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```

> [[**В начало**]](#пример-1)>

```java

@RestController
@RequestMapping(path = "/manage")
@Tag(name = "4 Приём/выдача книг", description = "Контроллер для приёма/выдачи книг")
@SecurityRequirement(name = "basicAuth")
public class ManageController {
    private Logger logger = LoggerFactory.getLogger(ManageController.class);
    private final ManageService manageService;

    public ManageController(@Qualifier("manageServiceImpl") ManageService manageService) {
        this.manageService = manageService;
    }

    @PostMapping
    @Operation(
            summary = "Выдать",
            description = "Выдать книгу читателю",
            tags = "Приём/выдача",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Reader> giveBook(@RequestParam @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long bookId, @RequestParam @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long readerId) {
        logger.debug(MessageFormat.format("Give to reader {0} book {1}", readerId, bookId));
        Reader reader = manageService.giveBookToReader(bookId, readerId);
        if (reader == null) {
            logger.error("Give to reader %d book %d failed".formatted(readerId, bookId));
            return ResponseEntity.badRequest().build();
        } else {
            logger.info("Give to reader %d book %d success".formatted(readerId, bookId));
            return ResponseEntity.ok(reader);
        }
    }

    @GetMapping
    @Operation(
            summary = "Принять",
            description = "Принять книгу от читателя",
            tags = "Приём/выдача",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Reader.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Reader> takeBook(@RequestParam(name = "book") @Parameter(description = "ID книги в имеющемся списке книг", required = true, schema = @Schema(implementation = Long.class), example = "1") long bookId, @RequestParam(name = "reader") @Parameter(description = "ID читателя в имеющемся списке читателей", required = true, schema = @Schema(implementation = Long.class), example = "1") long readerId) {
        logger.debug(MessageFormat.format("Take from reader {0} book {1}", readerId, bookId));
        Reader reader = manageService.takeBookFromReader(bookId, readerId);
        if (reader == null) {
            logger.error("Take from reader %d book %d failed".formatted(readerId, bookId));
            return ResponseEntity.badRequest().build();
        } else {
            logger.info("Take from reader %d book %d success".formatted(readerId, bookId));
            return ResponseEntity.ok(reader);
        }
    }
}
```

> [[**В начало**]](#пример-1)

```java

@RestController
@RequestMapping(path = "/whether")
@Tag(name = "5 Погода", description = "Контроллер для получения прогнозов погоды")
@SecurityRequirement(name = "basicAuth")
public class WhetherController {
    private final WhetherService whetherService;

    public WhetherController(@Qualifier(value = "whetherServiceImpl") WhetherService whetherService) {
        this.whetherService = whetherService;
    }

    @GetMapping(path = "/city-info")                    // http://localhost:8080/whether/city-info
    @Operation(
            summary = "Гео",
            description = "Получить гео данные города",
            tags = "Погода",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = City.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<City>> getCityInfo(@RequestParam(name = "city") @Parameter(description = "Название города", required = true, schema = @Schema(implementation = String.class), example = "Москва") String city) {
        Collection<City> cities = whetherService.getCityInfo(city);
        if (cities != null) {
            return ResponseEntity.ok(cities);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping                                             // http://localhost:8080/whether
    @Operation(
            summary = "Погода",
            description = "Получить данные о погоде",
            tags = "Погода",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Whether.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Whether>> getWhether(@RequestParam(name = "city") @Parameter(description = "Название города", required = true, schema = @Schema(implementation = String.class), example = "Москва") String city, @RequestParam(name = "country") @Parameter(description = "Код страны", required = true, schema = @Schema(implementation = String.class), example = "RU") String country) {
        Collection<Whether> whether = whetherService.getWhether(city, country);
        if (whether != null) {
            return ResponseEntity.ok(whether);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
```

> [[**В начало**]](#пример-1)

```java

@RestController
@RequestMapping(path = "/expenses")
@Tag(name = "6 Затраты", description = "Контроллер для работы с затратами")
@SecurityRequirement(name = "basicAuth")
public class ExpensesController {
    private final ExpenseService expenseService;

    public ExpensesController(@Qualifier("expenseServiceImpl") ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @Operation(
            summary = "Все затраты",
            description = "Посмотреть все затраты",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Expense.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Expense>> getExpenses(@RequestParam(required = false) @Parameter(description = "Номер страницы", required = true, schema = @Schema(implementation = Long.class), example = "1") Integer page, @RequestParam(required = false) @Parameter(description = "Размер страницы", required = true, schema = @Schema(implementation = Long.class), example = "4") Integer size) {
        Collection<Expense> expenses;
        if (page == null && size == null) {
            expenses = expenseService.getAllExpenses();
        } else if (page != null && size != null && size > 0 && page > 0) {
            expenses = expenseService.getAllExpenses(page, size);
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть затрату по ID",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") @Parameter(description = "ID записи о затратах", required = true, schema = @Schema(implementation = Long.class), example = "1") int id) {
        Expense expense = expenseService.getExpense(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expense);
        }
    }

    @PostMapping
    @Operation(
            summary = "Внести",
            description = "Внести затрату",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense addedExpense = expenseService.addExpense(expense);
        if (addedExpense == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedExpense);
        }
    }

    @GetMapping(path = "/categories")
    @Operation(
            summary = "Затраты по категориям",
            description = "Посмотреть все затраты по категориям",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExpensesByCategory.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ExpensesByCategory>> getExpensesByCategories() {
        Collection<ExpensesByCategory> expenses = expenseService.getExpensesByCategories();
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }
}
```

> [[**В начало**]](#пример-1)

```java

@RestController
@RequestMapping(path = "/counter")
@Tag(name = "7 Счётчик", description = "Контроллер для работы со счётчиком")
@SecurityRequirement(name = "basicAuth")
public class CounterController {
    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @GetMapping
    @Operation(
            summary = "Получить",
            description = "Получить значение счётчика",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<String> getCounter() {
        return ResponseEntity.ok(String.valueOf(counterService.getCounter()));
    }

    @GetMapping("/change")
    @Operation(
            summary = "Изменить - вариант 1",
            description = "Изменить значение счётчика - вариант 1",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Integer> changeCounter1(@RequestParam(name = "counter") int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }

    @GetMapping("/change/{counter}")
    @Operation(
            summary = "Изменить - вариант 2",
            description = "Изменить значение счётчика - вариант 2",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Integer> changeCounter2(@PathVariable int counter) {
        return ResponseEntity.status(200).body(counterService.setCounter(counter));
    }
}
```

> [[**В начало**]](#пример-1)

```java

@RestController
@RequestMapping(path = "/random")
@Hidden
@Tag(name = "8 Рандом", description = "Контроллер для получения случайных чисел")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class RandomizeController {
    private final RandomService randomService;

    public RandomizeController(RandomServiceImpl randomService) {
        this.randomService = randomService;
    }

    @GetMapping
    public ResponseEntity<Integer> getRandomValue() {
        return ResponseEntity.status(200).body(randomService.getRandomNextInt());
    }
}
```

### Создание хендлеров для контроллеров приложения

> [[**В начало**]](#пример-1)

```java

@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e) {
        System.out.printf("RuntimeException: %s%n", e.getMessage());
    }
}
```

### Применение api-docs.yaml

> [[**В начало**]](#пример-1)

```yaml
openapi: 3.0.1
info:
  title: Конспект по языку Java
  description: Конспект по языку Java на русском языке
  contact:
    name: Токовенко Виктор
    url: https://github.com/Lorgar-Avrelian?tab=repositories
    email: victor-14-244@mail.ru
  version: 0.1.0
servers:
  - url: http://localhost:8080
    description: Generated server url
tags:
  - name: 6 Затраты
    description: Контроллер для работы с затратами
  - name: 7 Счётчик
    description: Контроллер для работы со счётчиком
  - name: 5 Погода
    description: Контроллер для получения прогнозов погоды
  - name: 4 Приём/выдача книг
    description: Контроллер для приёма/выдачи книг
  - name: 3 Читатели
    description: Контроллер для работы с читателями
  - name: 1 Авторизация
    description: Контроллер для авторизации пользователей
  - name: 8 Рандом
    description: Контроллер для получения случайных чисел
  - name: 2 Книги
    description: Контроллер для работы с книгами
paths:
  /readers:
    get:
      tags:
        - Поиск
        - 3 Читатели
      summary: Список читателей
      description: Вывести список всех зарегистрированных читателей
      operationId: getAllReaders
      parameters:
        - name: partOfNameSecondNameOrSurname
          in: query
          description: Часть ФИО читателя
          required: false
          schema:
            type: string
          example: Иван
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/ReaderNoBooksDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
    put:
      tags:
        - 3 Читатели
        - Читатель
      summary: Редактировать
      description: Отредактировать информацию о читателе
      operationId: updateReader
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/Reader"
        required: true
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ReaderNoBooksDTO"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
    post:
      tags:
        - 3 Читатели
        - Читатель
      summary: Создать
      description: Добавить информацию о читателе
      operationId: createReader
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/NewReaderDTO"
        required: true
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ReaderNoBooksDTO"
        "405":
          description: Method Not Allowed
        "500":
          description: Internal Server Error
      security:
        - basicAuth: [ ]
  /books:
    get:
      tags:
        - 2 Книги
        - Поиск
      summary: Список книг
      description: Вывести список всех доступных книг
      operationId: getAllBooks
      parameters:
        - name: authorOrTitle
          in: query
          description: Часть ФИО автора или названия книги
          required: false
          schema:
            type: string
          example: Пушкин
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/BookDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
    put:
      tags:
        - 2 Книги
        - Книга
      summary: Редактировать
      description: Отредактировать информацию о книге
      operationId: updateBook
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/BookDTO"
        required: true
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookDTO"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
    post:
      tags:
        - 2 Книги
        - Книга
      summary: Создать
      description: Добавить информацию о книге
      operationId: createBook
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/NewBookDTO"
        required: true
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookDTO"
        "405":
          description: Method Not Allowed
      security:
        - basicAuth: [ ]
  /register:
    post:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Регистрация
      description: Пройти регистрацию
      operationId: register
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/RegisterDTO"
        required: true
      responses:
        "201":
          description: Created
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /manage:
    get:
      tags:
        - Приём/выдача
        - 4 Приём/выдача книг
      summary: Принять
      description: Принять книгу от читателя
      operationId: takeBook
      parameters:
        - name: book
          in: query
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
        - name: reader
          in: query
          description: ID читателя в имеющемся списке читателей
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Reader"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
    post:
      tags:
        - Приём/выдача
        - 4 Приём/выдача книг
      summary: Выдать
      description: Выдать книгу читателю
      operationId: giveBook
      parameters:
        - name: bookId
          in: query
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
        - name: readerId
          in: query
          description: ID читателя в имеющемся списке читателей
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Reader"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /login:
    post:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Войти
      description: Пройти авторизацию
      operationId: login
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/BasicAuthDTO"
        required: true
      responses:
        "200":
          description: OK
        "401":
          description: Unauthorized
      security:
        - basicAuth: [ ]
  /expenses:
    get:
      tags:
        - 6 Затраты
        - Затраты
      summary: Все затраты
      description: Посмотреть все затраты
      operationId: getExpenses
      parameters:
        - name: page
          in: query
          description: Номер страницы
          required: true
          schema:
            type: integer
            format: int64
          example: 1
        - name: size
          in: query
          description: Размер страницы
          required: true
          schema:
            type: integer
            format: int64
          example: 4
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/Expense"
        "400":
          description: Bad Request
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
    post:
      tags:
        - 6 Затраты
        - Затраты
      summary: Внести
      description: Внести затрату
      operationId: createExpense
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/Expense"
        required: true
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Expense"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /books/{id}/cover:
    get:
      tags:
        - 2 Книги
        - Изображения
      summary: Посмотреть
      description: Посмотреть обложку для книги
      operationId: downloadCover
      parameters:
        - name: id
          in: path
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            multipart/form-data:
              schema:
                $ref: "#/components/schemas/BookCover"
        "404":
          description: Not Found
        "502":
          description: Bad Gateway
      security:
        - basicAuth: [ ]
    post:
      tags:
        - 2 Книги
        - Изображения
      summary: Загрузить
      description: Загрузить обложку для книги
      operationId: uploadCover
      parameters:
        - name: id
          in: path
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
      requestBody:
        content:
          multipart/form-data:
            schema:
              required:
                - file
              type: object
              properties:
                file:
                  type: string
                  description: Изображение обложки книги
                  format: binary
      responses:
        "200":
          description: OK
          content:
            multipart/form-data:
              schema:
                $ref: "#/components/schemas/BookCover"
        "400":
          description: Bad Request
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
  /set-role:
    patch:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Права
      description: Смена прав для зарегистрированного пользователя
      operationId: setRole
      parameters:
        - name: id
          in: query
          description: ID пользователя
          required: true
          schema:
            type: integer
            format: int64
        - name: role
          in: query
          description: Новая роль пользователя
          required: true
          schema:
            type: string
            enum:
              - ROLE_USER
              - ROLE_ADMIN
              - ROLE_OWNER
      responses:
        "200":
          description: Ok
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/LoginDTO"
        "403":
          description: Forbidden
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /set-password:
    patch:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Пароль
      description: Смена пароля для зарегистрированного пользователя
      operationId: setPassword
      parameters:
        - name: id
          in: query
          description: ID пользователя
          required: true
          schema:
            type: integer
            format: int64
        - name: password
          in: query
          description: Новый пароль пользователя
          required: true
          schema:
            type: string
      responses:
        "200":
          description: Ok
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/LoginDTO"
        "403":
          description: Forbidden
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /whether:
    get:
      tags:
        - 5 Погода
        - Погода
      summary: Погода
      description: Получить данные о погоде
      operationId: getWhether
      parameters:
        - name: city
          in: query
          description: Название города
          required: true
          schema:
            type: string
          example: Москва
        - name: country
          in: query
          description: Код страны
          required: true
          schema:
            type: string
          example: RU
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/Whether"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /whether/city-info:
    get:
      tags:
        - 5 Погода
        - Погода
      summary: Гео
      description: Получить гео данные города
      operationId: getCityInfo
      parameters:
        - name: city
          in: query
          description: Название города
          required: true
          schema:
            type: string
          example: Москва
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/City"
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /users:
    get:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Пользователи
      description: Список зарегистрированных пользователей
      operationId: getUsers
      parameters:
        - name: page
          in: query
          description: Номер страницы
          required: false
          schema:
            type: integer
            format: int32
        - name: size
          in: query
          description: Количество пользователей на странице
          required: false
          schema:
            type: integer
            format: int32
      responses:
        "200":
          description: Ok
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/LoginDTO"
        "400":
          description: Bad Request
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
  /readers/{id}:
    get:
      tags:
        - 3 Читатели
        - Читатель
      summary: Найти
      description: Найти информацию о читателе по ID
      operationId: readReader
      parameters:
        - name: id
          in: path
          description: ID читателя в имеющемся списке читателей
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ReaderNoBooksDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
    delete:
      tags:
        - 3 Читатели
        - Читатель
      summary: Удалить
      description: Удалить информацию о читателе по ID
      operationId: deleteReader
      parameters:
        - name: id
          in: path
          description: ID читателя в имеющемся списке читателей
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ReaderNoBooksDTO"
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
  /readers/surname:
    get:
      tags:
        - Поиск
        - 3 Читатели
      summary: Список по фамилии
      description: Вывести список всех зарегистрированных читателей с указанной фамилией
      operationId: getReadersBySurname
      parameters:
        - name: partOfSurname
          in: query
          description: Часть фамилии читателя
          required: true
          schema:
            type: string
          example: Иванов
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/ReaderNoBooksDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /readers/secondName:
    get:
      tags:
        - Поиск
        - 3 Читатели
      summary: Список по отчеству
      description: Вывести список всех зарегистрированных читателей с указанным отчеством
      operationId: getReadersBySecondName
      parameters:
        - name: partOfSecondName
          in: query
          description: Часть отчества читателя
          required: true
          schema:
            type: string
          example: Иванович
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/ReaderNoBooksDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /readers/name:
    get:
      tags:
        - Поиск
        - 3 Читатели
      summary: Список по имени
      description: Вывести список всех зарегистрированных читателей с указанным именем
      operationId: getReadersByName
      parameters:
        - name: partOfName
          in: query
          description: Часть имени читателя
          required: true
          schema:
            type: string
          example: Иван
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/ReaderNoBooksDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /readers/books:
    get:
      tags:
        - Поиск
        - 3 Читатели
      summary: Список книг
      description: Вывести список всех книг данного читателя
      operationId: getReaderBooks
      parameters:
        - name: id
          in: query
          description: ID читателя в имеющемся списке читателей
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/BookDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /random:
    get:
      tags:
        - 8 Рандом
      operationId: getRandomValue
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                type: integer
                format: int32
      security:
        - basicAuth: [ ]
  /expenses/{id}:
    get:
      tags:
        - 6 Затраты
        - Затраты
      summary: Посмотреть
      description: Посмотреть затрату по ID
      operationId: getExpenseById
      parameters:
        - name: id
          in: path
          description: ID записи о затратах
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Expense"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /expenses/categories:
    get:
      tags:
        - 6 Затраты
        - Затраты
      summary: Затраты по категориям
      description: Посмотреть все затраты по категориям
      operationId: getExpensesByCategories
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: "#/components/schemas/ExpensesByCategory"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
  /csrf:
    get:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: CSRF-токен
      description: Получить CSRF-токен
      operationId: csrf
      parameters:
        - name: csrfToken
          in: query
          required: true
          schema:
            $ref: "#/components/schemas/CsrfToken"
      responses:
        "200":
          description: Ok
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/CsrfToken"
      security:
        - basicAuth: [ ]
  /counter:
    get:
      tags:
        - 7 Счётчик
      summary: Получить
      description: Получить значение счётчика
      operationId: getCounter
      responses:
        "200":
          description: OK
          content:
            text/plain: { }
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /counter/change:
    get:
      tags:
        - 7 Счётчик
      summary: Изменить - вариант 1
      description: Изменить значение счётчика - вариант 1
      operationId: changeCounter1
      parameters:
        - name: counter
          in: query
          required: true
          schema:
            type: integer
            format: int32
      responses:
        "200":
          description: OK
          content:
            text/plain: { }
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /counter/change/{counter}:
    get:
      tags:
        - 7 Счётчик
      summary: Изменить - вариант 2
      description: Изменить значение счётчика - вариант 2
      operationId: changeCounter2
      parameters:
        - name: counter
          in: path
          required: true
          schema:
            type: integer
            format: int32
      responses:
        "200":
          description: OK
          content:
            text/plain: { }
        "400":
          description: Bad Request
      security:
        - basicAuth: [ ]
  /books/{id}:
    get:
      tags:
        - 2 Книги
        - Книга
      summary: Найти
      description: Найти информацию о книге по ID
      operationId: readBook
      parameters:
        - name: id
          in: path
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookDTO"
        "404":
          description: Not Found
      security:
        - basicAuth: [ ]
    delete:
      tags:
        - 2 Книги
        - Книга
      summary: Удалить
      description: Удалить информацию о книге по ID
      operationId: deleteBook
      parameters:
        - name: id
          in: path
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/BookDTO"
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
  /books/{id}/cover/preview:
    get:
      tags:
        - 2 Книги
        - Изображения
      summary: Посмотреть превью
      description: Посмотреть превью обложки для книги
      operationId: downloadCover_1
      parameters:
        - name: id
          in: path
          description: ID книги в имеющемся списке книг
          required: true
          schema:
            type: integer
            format: int64
          example: 1
      responses:
        "200":
          description: OK
          content:
            multipart/form-data:
              schema:
                type: array
                items:
                  type: string
                  format: byte
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
  /delete:
    delete:
      tags:
        - 1 Авторизация
        - Безопасность
      summary: Удалить
      description: Удалить пользователя
      operationId: delete
      parameters:
        - name: id
          in: query
          description: ID пользователя
          required: true
          schema:
            type: integer
            format: int64
      responses:
        "200":
          description: Ok
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/LoginDTO"
        "400":
          description: Bad Request
        "403":
          description: Forbidden
      security:
        - basicAuth: [ ]
components:
  schemas:
    ReaderNoBooksDTO:
      title: Читатель
      maximum: 9223372036854775807
      minimum: 1
      required:
        - id
        - name
        - personalNumber
        - surname
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID читателя
          format: int64
          default: 1
        name:
          title: Имя
          maxLength: 12
          minLength: 2
          type: string
          description: Имя читателя
          default: Иван
        secondName:
          title: Отчество
          maxLength: 16
          minLength: 2
          type: string
          description: Отчество читателя
          default: Иванович
        surname:
          title: Фамилия
          maxLength: 30
          minLength: 2
          type: string
          description: Фамилия читателя
          default: Иванов
        personalNumber:
          title: Номер билета
          maximum: 2147483648
          minimum: 1
          type: integer
          description: Номер читательского билета
          format: int32
          default: 1
      description: Сущность читателя
      default: null
    Reader:
      title: Читатель
      maximum: 9223372036854775807
      minimum: 1
      required:
        - id
        - name
        - personalNumber
        - surname
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID читателя
          format: int64
          default: 1
        name:
          title: Имя
          maxLength: 12
          minLength: 2
          type: string
          description: Имя читателя
          default: Иван
        secondName:
          title: Отчество
          maxLength: 16
          minLength: 2
          type: string
          description: Отчество читателя
          default: Иванович
        surname:
          title: Фамилия
          maxLength: 30
          minLength: 2
          type: string
          description: Фамилия читателя
          default: Иванов
        personalNumber:
          title: Номер билета
          maximum: 2147483648
          minimum: 1
          type: integer
          description: Номер читательского билета
          format: int32
          default: 1
      description: Сущность читателя
      default: null
    BookDTO:
      title: Книга
      required:
        - author
        - id
        - title
        - year
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID книги
          format: int64
          default: 1
        title:
          title: Название
          maxLength: 30
          minLength: 3
          type: string
          description: Название книги
          default: Война и мир
        author:
          title: Автор
          maxLength: 24
          minLength: 8
          type: string
          description: Автор книги
          default: Л.Н. Толстой
        year:
          title: Год
          minimum: 1970
          type: integer
          description: Год публикации книги
          format: int32
          default: 1986
        reader:
          $ref: "#/components/schemas/ReaderNoBooksDTO"
      description: Сущность книги
    RegisterDTO:
      title: Регистрационные данные
      required:
        - login
        - password
        - passwordConfirmation
      type: object
      properties:
        login:
          title: Логин
          maxLength: 30
          minLength: 3
          type: string
          description: Логин пользователя
          default: user
        password:
          title: Пароль
          minLength: 3
          type: string
          description: Пароль пользователя
          default: "123"
        passwordConfirmation:
          title: Подтверждение пароля
          minLength: 3
          type: string
          description: Подтверждение пароля пользователя
          default: "123"
      description: "Логин, пароль и подтверждение пароля"
    NewReaderDTO:
      title: Новый читатель
      required:
        - name
        - personalNumber
        - surname
      type: object
      properties:
        name:
          title: Имя
          maxLength: 12
          minLength: 2
          type: string
          description: Имя читателя
          default: Иван
        secondName:
          title: Отчество
          maxLength: 16
          minLength: 2
          type: string
          description: Отчество читателя
          default: Иванович
        surname:
          title: Фамилия
          maxLength: 30
          minLength: 2
          type: string
          description: Фамилия читателя
          default: Иванов
        personalNumber:
          title: Номер билета
          maximum: 2147483648
          minimum: 1
          type: integer
          description: Номер читательского билета
          format: int32
          default: 1
      description: Сущность нового читателя
    BasicAuthDTO:
      title: Basic-аутентификация
      required:
        - login
        - password
      type: object
      properties:
        login:
          title: Логин
          maxLength: 30
          minLength: 3
          type: string
          description: Логин пользователя
          default: user
        password:
          title: Пароль
          minLength: 3
          type: string
          description: Пароль пользователя
          default: "123"
      description: Логин и пароль
    Expense:
      title: Расход
      required:
        - amount
        - category
        - date
        - id
        - title
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID траты
          format: int32
          default: 1
        title:
          title: Название
          maxLength: 30
          minLength: 3
          type: string
          description: Название траты
          default: Поездка на транспорте
        date:
          title: Дата
          type: string
          description: Дата траты
          format: date
        category:
          title: Категория
          maxLength: 30
          minLength: 3
          type: string
          description: Категория затрат
          default: Транспорт
        amount:
          title: Количество
          minimum: 0
          type: number
          description: Количество затрат
          format: float
          default: 1000
      description: Сущность затрат
    NewBookDTO:
      title: Новая книга
      required:
        - authorFIO
        - title
        - year
      type: object
      properties:
        title:
          title: Название
          maxLength: 30
          minLength: 3
          type: string
          description: Название книги
          default: Война и мир
        authorFIO:
          title: Автор
          maxLength: 24
          minLength: 8
          type: string
          description: Автор книги
          default: Л.Н. Толстой
        year:
          title: Год
          minimum: 1970
          type: integer
          description: Год публикации книги
          format: int32
          default: 1986
      description: Сущность новой книги
    Book:
      title: Книга
      maximum: 9223372036854775807
      minimum: 1
      required:
        - author
        - id
        - title
        - year
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID книги
          format: int64
          default: 1
        title:
          title: Название
          maxLength: 30
          minLength: 3
          type: string
          description: Название книги
          default: Война и мир
        author:
          title: Автор
          maxLength: 24
          minLength: 8
          type: string
          description: Автор книги
          default: Л.Н. Толстой
        year:
          title: Год
          minimum: 1970
          type: integer
          description: Год публикации книги
          format: int32
          default: 1986
        reader:
          $ref: "#/components/schemas/Reader"
      description: Сущность книги
      default: null
    BookCover:
      title: Обложка книги
      required:
        - filePath
        - fileSize
        - id
        - imagePreview
        - mediaType
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID обложки книги
          format: int64
          default: 1
        filePath:
          title: Путь
          type: string
          description: Путь сохранения обложки книги
        fileSize:
          title: Размер
          type: integer
          description: Размер сохранённой обложки книги
          format: int32
        mediaType:
          title: Тип
          type: string
          description: Тип сохранённой обложки книги
        imagePreview:
          title: Превью
          type: string
          description: Превью обложки книги
          format: byte
        book:
          $ref: "#/components/schemas/Book"
      description: Сущность обложки для книг
    LoginDTO:
      title: Логин
      required:
        - id
        - login
        - role
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID пользователя
          format: int64
          default: 1
        login:
          title: Логин
          maxLength: 30
          minLength: 3
          type: string
          description: Логин пользователя
          default: user
        role:
          title: Роль
          maxLength: 30
          minLength: 3
          type: string
          description: Роль пользователя в системе
          default: USER
          enum:
            - ROLE_USER
            - ROLE_ADMIN
            - ROLE_OWNER
      description: Сущность логина пользователя
    Whether:
      title: Погода
      type: object
      properties:
        temperature:
          title: "Температура, ºC"
          maximum: 400
          minimum: -273
          type: number
          description: "Значение температуры, ºC"
          default: 0
        feels_like:
          title: "Ощущается, ºC"
          maximum: 400
          minimum: -273
          type: number
          description: "Ощущается как температура, ºC"
          default: 0
        temp_min:
          title: "Минимальная температура, ºC"
          maximum: 400
          minimum: -273
          type: number
          description: "Минимальное зафиксированное значение температуры, ºC"
          default: 0
        temp_max:
          title: "Максимальная температура, ºC"
          maximum: 400
          minimum: -273
          type: number
          description: "Максимальное зафиксированное значение температуры, ºC"
          default: 0
        pressure:
          title: "Давление, мм рт. ст."
          maximum: 1000
          minimum: 0
          type: number
          description: "Значение атмосферного давления, мм рт. ст."
          default: 746
        humidity:
          title: "Влажность, %"
          maximum: 100
          minimum: 0
          type: number
          description: "Значение влажности воздуха, %"
          default: 50
        visibility:
          title: "Видимость, м"
          maximum: 10000
          minimum: 0
          type: number
          description: "Значение прямой видимости, м"
          default: 5000
        wind_speed:
          title: "Скорость ветра, м/с"
          maximum: 100
          minimum: 0
          type: number
          description: "Значение скорости ветра, м/с"
          default: 3
        wind_deg:
          title: "Направление ветра, º"
          maximum: 360
          minimum: 0
          type: number
          description: "Значение направления ветра, º"
          default: 0
        sunrise:
          title: Восход
          type: string
          description: Время восхода солнца
          format: date-time
        sunset:
          title: Закат
          type: string
          description: Время заката солнца
          format: date-time
      description: Информация о погоде
    City:
      title: Город
      required:
        - country
        - id
        - lat
        - localNames
        - lon
        - name
        - state
      type: object
      properties:
        id:
          title: ID
          maximum: 9223372036854775807
          minimum: 1
          type: integer
          description: ID города
          format: int64
          default: 1
        name:
          title: Название
          maxLength: 30
          minLength: 2
          type: string
          description: Название города
          default: Москва
        lat:
          title: Широта
          maximum: 180
          minimum: 0
          type: number
          description: Географическая широта города
          default: 55.7504461
        lon:
          title: Долгота
          maximum: 180
          minimum: 0
          type: number
          description: Географическая долгота города
          default: 37.6174943
        country:
          title: Страна
          maxLength: 30
          minLength: 2
          type: string
          description: Код страны
          default: RU
        state:
          title: Регион
          maxLength: 30
          minLength: 2
          type: string
          description: Регион страны
          default: Moscow
        localNames:
          title: Варианты написания
          type: string
          description: Варианты написания названия города на различных языках
          default: "{\"feature_name\":\"Moscow\",\"no\":\"Moskva\",\"bi\":\"Moskow\"\
            ,\"na\":\"Moscow\",\"io\":\"Moskva\",\"bs\":\"Moskva\",\"jv\":\"Moskwa\"\
            ,\"el\":\"Μόσχα\",\"mg\":\"Moskva\",\"ja\":\"モスクワ\",\"su\":\"Moskwa\"\
            ,\"eo\":\"Moskvo\",\"ab\":\"Москва\",\"co\":\"Moscù\",\"is\":\"Moskva\"\
            ,\"az\":\"Moskva\",\"hr\":\"Moskva\",\"iu\":\"ᒨᔅᑯ\",\"sk\":\"Moskva\"\
            ,\"hy\":\"Մոսկվա\",\"sl\":\"Moskva\",\"uk\":\"Москва\",\"an\":\"Moscú\"\
            ,\"sm\":\"Moscow\",\"yi\":\"מאסקווע\",\"be\":\"Масква\",\"ie\":\"Moskwa\"\
            ,\"ro\":\"Moscova\",\"tr\":\"Moskova\",\"tt\":\"Мәскәү\",\"sr\":\"Моск\
            ва\",\"mr\":\"मॉस्को\",\"kk\":\"Мәскеу\",\"mn\":\"Москва\",\"ca\":\"Moscou\"\
            ,\"zh\":\"莫斯科\",\"ce\":\"Москох\",\"es\":\"Moscú\",\"vo\":\"Moskva\",\"\
            av\":\"Москва\",\"gd\":\"Moscobha\",\"dz\":\"མོསི་ཀོ\",\"yo\":\"Mọsko\"\
            ,\"nn\":\"Moskva\",\"bo\":\"མོ་སི་ཁོ།\",\"cy\":\"Moscfa\",\"ka\":\"მოს\
            კოვი\",\"ug\":\"Moskwa\",\"sc\":\"Mosca\",\"cs\":\"Moskva\",\"ss\":\"\
            Moscow\",\"lg\":\"Moosko\",\"dv\":\"މޮސްކޯ\",\"se\":\"Moskva\",\"ascii\"\
            :\"Moscow\",\"gv\":\"Moscow\",\"fr\":\"Moscou\",\"mt\":\"Moska\",\"am\"\
            :\"ሞስኮ\",\"sh\":\"Moskva\",\"it\":\"Mosca\",\"br\":\"Moskov\",\"ko\":\"\
            모스크바\",\"ur\":\"ماسکو\",\"kv\":\"Мӧскуа\",\"et\":\"Moskva\",\"fo\":\"\
            Moskva\",\"zu\":\"IMoskwa\",\"gl\":\"Moscova - Москва\",\"hi\":\"मास्क\
            ो\",\"sg\":\"Moscow\",\"ru\":\"Москва\",\"kw\":\"Moskva\",\"da\":\"Moskva\"\
            ,\"ln\":\"Moskú\",\"th\":\"มอสโก\",\"bg\":\"Москва\",\"li\":\"Moskou\"\
            ,\"ku\":\"Moskow\",\"de\":\"Moskau\",\"my\":\"မော်စကိုမြို့\",\"ky\":\"\
            Москва\",\"wa\":\"Moscou\",\"ga\":\"Moscó\",\"ak\":\"Moscow\",\"fi\":\"\
            Moskova\",\"sw\":\"Moscow\",\"fa\":\"مسکو\",\"id\":\"Moskwa\",\"ht\":\"\
            Moskou\",\"mk\":\"Москва\",\"uz\":\"Moskva\",\"tl\":\"Moscow\",\"mi\"\
            :\"Mohikau\",\"so\":\"Moskow\",\"wo\":\"Mosku\",\"sq\":\"Moska\",\"nl\"\
            :\"Moskou\",\"cu\":\"Москъва\",\"ps\":\"مسکو\",\"tg\":\"Маскав\",\"kn\"\
            :\"ಮಾಸ್ಕೋ\",\"fy\":\"Moskou\",\"st\":\"Moscow\",\"qu\":\"Moskwa\",\"ml\"\
            :\"മോസ്കോ\",\"ta\":\"மாஸ்கோ\",\"he\":\"מוסקווה\",\"ay\":\"Mosku\",\"cv\"\
            :\"Мускав\",\"ch\":\"Moscow\",\"ms\":\"Moscow\",\"lv\":\"Maskava\",\"\
            la\":\"Moscua\",\"af\":\"Moskou\",\"lt\":\"Maskva\",\"za\":\"Moscow\"\
            ,\"kg\":\"Moskva\",\"kl\":\"Moskva\",\"gn\":\"Mosku\",\"pt\":\"Moscou\"\
            ,\"ia\":\"Moscova\",\"os\":\"Мæскуы\",\"oc\":\"Moscòu\",\"vi\":\"Mát-xcơ\
            -va\",\"te\":\"మాస్కో\",\"sv\":\"Moskva\",\"ar\":\"موسكو\",\"pl\":\"Moskwa\"\
            ,\"tk\":\"Moskwa\",\"eu\":\"Mosku\",\"en\":\"Moscow\",\"ty\":\"Moscou\"\
            ,\"hu\":\"Moszkva\",\"bn\":\"মস্কো\",\"ba\":\"Мәскәү\",\"nb\":\"Moskva\"\
            }"
      description: Информация о городе
    ExpensesByCategory:
      title: Расходы по категориям
      type: object
      properties:
        amount:
          title: Количество
          minimum: 0
          type: number
          description: Количество затрат
          format: float
          default: 1000
        category:
          title: Категория
          type: string
          description: Название категории
          default: Транспорт
      description: Сущность расходов по категориям
    CsrfToken:
      type: object
      properties:
        parameterName:
          type: string
        token:
          type: string
        headerName:
          type: string
  securitySchemes:
    basicAuth:
      type: http
      scheme: basic

```

### Написание дополнительных фильтров безопасности

> [[**В начало**]](#пример-1)

- для разрешения использования учётных данных в межсайтовых запросах:

```java

@Component
public class BasicAuthCorsFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);
    }
}
```

- для тестирования с помощью _Postman_:

```java

@Component
public class CSRFTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.print("method: " + request.getMethod());
        System.out.println(" " + request.getRequestURL());
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }
        System.out.println();
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        System.out.println(token.getHeaderName());
        System.out.println(token.getParameterName());
        System.out.println(token.getToken());
        filterChain.doFilter(request, response);
    }
}
```

### Написание конфигурации безопасности

> [[**В начало**]](#пример-1)

```java

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
```

### Написание тестов

> [[**В начало**]](#пример-1)

- написание конфигурации для тестов:

```java

@Configuration
@Profile("test")
public class TestConfiguration {
    @Autowired
    private BookMapper bookMapper;

    @Bean(name = "bookServiceImplDB")
    public BookService bookService(BookRepository bookRepository) {
        return new BookServiceImplDB(bookRepository, bookMapper);
    }

    @Bean(name = "bookCoverServiceImpl")
    public BookCoverService bookCoverService(BookService bookService, BookCoverRepository bookcoverRepository) {
        return new BookCoverServiceImpl(bookService, bookcoverRepository);
    }
}
```

- unit-тесты:
- интеграционные тесты: