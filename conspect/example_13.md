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
- [добавление плагинов для подключённых зависимостей](#добавление-плагинов-для-подключённых-зависимостей);
- [настройка параметров приложения](#настройка-параметров-приложения) в файле _application.properties_:
    * приложения в целом;
    * параметров отображения _Swagger UI_;
    * параметров подключения к БД и _Hibernate_;
    * задание файла _change_-лога для _Liquibase_;
    * определение локальных параметров, необходимых для работы сервисов приложения;
- [настройка _Liquibase_](#настройка-_liquibase_):
    * создание файла _change_-лога для _Liquibase_ (_src/main/resources/liquibase/changelog-master.yml_);
    * написание _SQL_-скрипта (_src/main/resources/liquibase/scripts/conspectus.sql_) для создания таблиц в БД;
- [создание сущностей _DAO_](#создание-сущностей-_dao_);

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
                </annotationProcessorPaths>
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

### Настройка _Liquibase_

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

### Создание сущностей _DAO_

> [[**В начало**]](#пример-1)


