## Пример 1:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.1 Тестирование с помощью TestRestTemplate]**](/conspect/5.md/#541-тестирование-с-помощью-testresttemplate)

- установка области видимости основной БД в файле _pom.xml_:

```xml
        <!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
    <scope>runtime</scope>
</dependency>
```

- добавление _JDBC_-провайдера для тестовой БД в файле _pom.xml_:

```xml
        <!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.3.232</version>
    <scope>test</scope>
</dependency>
```

- создание _properties_-файла и добавление в него настроек для тестовой БД:

```properties
# Application properties
spring.application.name=Java conspectus RUS
# Specific parameter for BookCoverService implementations
books.covers.dir.path=src/test/resources/testImages/result
# Spring Data JPA parameters for DB connection
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:library
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
# Hibernate settings
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=create
```

- написание тестов для стартера приложения с помощью методов класса _Assertions_:

```java

@DisplayName(value = "Test of JavaConspectRusApplication")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JavaConspectRusApplicationTests {

    @Autowired
    private ReaderController readerController;
    @Autowired
    private BooksController booksController;

    @Test
    @DisplayName(value = "Context load test")
    @Order(1)
    void contextLoads() {
        Assertions.assertThat(readerController).isNotNull();
        Assertions.assertThat(booksController).isNotNull();
    }
}
```
- написание тестов для методов контроллера с помощью методов классов _TestRestTemplate_ и _Assertions_:
```java

```

## Пример 2:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.1 Тестирование с помощью TestRestTemplate]**](/conspect/5.md/#541-тестирование-с-помощью-testresttemplate)
