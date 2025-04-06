## Пример 1:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.1 Тестирование с помощью TestRestTemplate]**](/conspect/05.md/#541-тестирование-с-помощью-testresttemplate)

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName(value = "http://localhost:8080/readers")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReaderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName(value = "POST http://localhost:8080/readers")
    @Order(1)
    void createReader() throws Exception {
        // Подготовительный этап - проверка того, что в базе данных отсутствуют какие-либо сущности (читатели)
        // - получение списка сущностей из БД
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        // - проверка того, что полученный список сущностей пуст
        Assertions.assertThat(response.getBody()).isNull();
        //
        // 1. Проверка, что новая сущность (читатель) создаётся в базе данных (Способ 1: метод TestRestTemplate.postForObject())
        // 1.1. получение новой сущности (читателя) из констант
        Reader expectedReader = TEST_READER_1;
        // 1.2. получение JSON-объекта из новой сущности (читателя)
        String expectedJson = mapper.writeValueAsString(expectedReader);
        // 1.3. проверка, что возвращаемый в результате запроса JSON-объект совпадает с отправленной в запросе новой сущностью (читателя)
        Assertions.assertThat(restTemplate.postForObject("http://localhost:" + port + "/readers", expectedReader, String.class))
                  .isEqualTo(expectedJson);
        //
        // 2. Проверка, что новая сущность (читатель) создаётся в базе данных (Способ 2: метод TestRestTemplate.postForObject())
        // 2.1. получение новой сущности (читателя) из констант
        expectedReader = TEST_READER_2;
        // 2.2. получение новой сущности (читателя) из HTTP-ответа
        Reader actualReader = restTemplate.postForObject("http://localhost:" + port + "/readers", expectedReader, Reader.class);
        // 2.3. сравнение отправленной новой сущности (читателя) с полученной
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 3. Проверка, что новая сущность (читатель) создаётся в базе данных (Способ 3 - ОСНОВНОЙ: метод TestRestTemplate.postForEntity())
        // 3.1. получение новой сущности (читателя) из констант
        expectedReader = TEST_READER_3;
        // 3.2. получение HTTP-ответа
        ResponseEntity<Reader> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/readers", expectedReader, Reader.class);
        // 3.3. получение новой сущности (читателя) из HTTP-ответа
        actualReader = responseEntity.getBody();
        // 3.4. сравнение отправленной новой сущности (читателя) с полученной
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 4. Проверка статуса и содержания успешного HTTP-ответа
        // 4.1. выполнение всех операций, указанных в п. 3
        // 4.2. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 4.3. сравнение содержания HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(TEST_READER_3);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(TEST_READER_3.getId());
        Assertions.assertThat(responseEntity.getBody().getName()).isEqualTo(TEST_READER_3.getName());
        Assertions.assertThat(responseEntity.getBody().getSecondName()).isEqualTo(TEST_READER_3.getSecondName());
        Assertions.assertThat(responseEntity.getBody().getSurname()).isEqualTo(TEST_READER_3.getSurname());
        Assertions.assertThat(responseEntity.getBody().getPersonalNumber()).isEqualTo(TEST_READER_3.getPersonalNumber());
        Assertions.assertThat(responseEntity.getBody().getBooks()).isEmpty();
        //
        // 5. Проведение всех вышеуказанных проверок с помощью метода TestRestTemplate.exchange()
        expectedReader = TEST_READER_4;
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.POST, new HttpEntity<>(expectedReader), Reader.class);
        actualReader = responseEntity.getBody();
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 6. Проверка статуса и содержания ошибочного HTTP-ответа
        // 6.1. получение новой заведомо некорректной сущности (читателя) из констант
        expectedReader = TEST_READER_UNSUPPORTED;
        // 6.2. получение HTTP-ответа
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/readers", expectedReader, Reader.class);
        // 6.3. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode().value()).isEqualTo(405);
        // 6.4. проверка, что тело HTTP-ответа отсутствует
        Assertions.assertThat(responseEntity.getBody()).isNull();
        //
        // 7. Проверка того, что все верные новые сущности (читатели) добавлены в БД
        // 7.1. создание ожидаемой коллекции сущностей (читателей)
        Collection<Reader> expectedReaders = new ArrayList<>(List.of(TEST_READER_1, TEST_READER_2, TEST_READER_3, TEST_READER_4));
        // 7.2. получение всех сущностей (читателей), имеющихся в БД (можно использовать методы TestRestTemplate.getForEntity() и TestRestTemplate.getForObject())
        response = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        // 7.3. преобразование тела полученного HTTP-ответа в коллекцию объектов
        Collection<Reader> actualReaders = Arrays.asList(mapper.readValue(response.getBody(), Reader[].class));
        // 7.4. сравнение полученной коллекции сущностей с ожидаемой
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        // 7.5 проверка того, что заведомо некорректная сущность отсутствует в полученной коллекции объектов
        Assertions.assertThat(actualReaders).doesNotContain(TEST_READER_UNSUPPORTED);
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/{id}")
    @Order(2)
    void readReader() throws Exception {
        // Подготовительный этап - проверка того, что в базе данных присутствуют добавленные сущности (читатели)
        // - создание ожидаемой коллекции сущностей (читателей) в БД
        Collection<Reader> expectedReaders = new ArrayList<>(List.of(TEST_READER_1, TEST_READER_2, TEST_READER_3, TEST_READER_4));
        // - проверка того, что все сущности (читатели) добавлены в БД
        //     * получение всех сущностей (читателей), имеющихся в БД (можно использовать методы TestRestTemplate.exchange() и TestRestTemplate.getForObject())
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/readers", String.class);
        //     * преобразование тела полученного HTTP-ответа в коллекцию объектов
        Collection<Reader> actualReaders = Arrays.asList(mapper.readValue(response.getBody(), Reader[].class));
        //     * сравнение полученной коллекции сущностей с ожидаемой
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        //     * проверка того, что заведомо некорректная сущность отсутствует в полученной коллекции объектов
        Assertions.assertThat(actualReaders).doesNotContain(TEST_READER_UNSUPPORTED);
        //
        // 1. Проверка, что сущность пользователя можно получить по ID (Способ 1: метод TestRestTemplate.getForObject())
        // 1.1. получение сущности (читателя) из констант
        Reader expectedReader = TEST_READER_1;
        // 1.2. получение JSON-объекта из сущности (читателя)
        String expectedJson = mapper.writeValueAsString(expectedReader);
        // 1.3. проверка, что возвращаемый в результате запроса JSON-объект совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), String.class))
                  .isEqualTo(expectedJson);
        //
        // 2. Проверка, что сущность пользователя можно получить по ID (Способ 2: метод TestRestTemplate.getForObject())
        // 2.1. получение сущности (читателя) из констант
        expectedReader = TEST_READER_2;
        // 2.2. получение сущности (читателя) из HTTP-ответа
        Reader actualReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        // 2.3. проверка, что возвращаемая в результате запроса сущность (читатель) совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 3. Проверка, что сущность пользователя можно получить по ID (Способ 3 - ОСНОВНОЙ: метод TestRestTemplate.getForEntity())
        // 3.1. получение сущности (читателя) из констант
        expectedReader = TEST_READER_3;
        // 3.2. получение HTTP-ответа
        ResponseEntity<Reader> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        // 3.3. получение сущности (читателя) из HTTP-ответа
        actualReader = responseEntity.getBody();
        // 3.4. проверка, что возвращаемая в результате запроса сущность (читатель) совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 4. Проверка статуса и содержания успешного HTTP-ответа
        // 4.1. выполнение всех операций, указанных в п. 3
        // 4.2. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 4.3. сравнение содержания HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(TEST_READER_3);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(TEST_READER_3.getId());
        Assertions.assertThat(responseEntity.getBody().getName()).isEqualTo(TEST_READER_3.getName());
        Assertions.assertThat(responseEntity.getBody().getSecondName()).isEqualTo(TEST_READER_3.getSecondName());
        Assertions.assertThat(responseEntity.getBody().getSurname()).isEqualTo(TEST_READER_3.getSurname());
        Assertions.assertThat(responseEntity.getBody().getPersonalNumber()).isEqualTo(TEST_READER_3.getPersonalNumber());
        Assertions.assertThat(responseEntity.getBody().getBooks()).isEmpty();
        //
        // 5. Проведение всех вышеуказанных проверок с помощью метода TestRestTemplate.exchange()
        expectedReader = TEST_READER_4;
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/" + expectedReader.getId(), HttpMethod.GET, HttpEntity.EMPTY, Reader.class);
        actualReader = responseEntity.getBody();
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 6. Проверка статуса и содержания ошибочного HTTP-ответа
        // 6.1. получение заведомо отсутствующей сущности (читателя)
        long unavailableID = TEST_READER_4.getId() + 1;
        // 6.2. получение HTTP-ответа
        responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + unavailableID, Reader.class);
        // 6.3. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
        // 6.4. проверка, что тело HTTP-ответа отсутствует
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/readers")
    @Order(3)
    void updateReader() throws Exception {
        // Подготовительный этап - проверка того, что в базе данных присутствуют добавленные сущности (читатели)
        // - создание ожидаемой коллекции сущностей (читателей) в БД
        Collection<Reader> expectedReaders = new ArrayList<>(List.of(TEST_READER_1, TEST_READER_2, TEST_READER_3, TEST_READER_4));
        // - проверка того, что все сущности (читатели) добавлены в БД
        //     * получение всех сущностей (читателей), имеющихся в БД (можно использовать методы TestRestTemplate.exchange() и TestRestTemplate.getForEntity())
        Reader[] response = restTemplate.getForObject("http://localhost:" + port + "/readers", Reader[].class);
        //     * преобразование тела полученного HTTP-ответа в коллекцию объектов
        Collection<Reader> actualReaders = Arrays.asList(response);
        //     * сравнение полученной коллекции сущностей с ожидаемой
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        //     * проверка того, что заведомо некорректная сущность отсутствует в полученной коллекции объектов
        Assertions.assertThat(actualReaders).doesNotContain(TEST_READER_UNSUPPORTED);
        //
        // 1. Проверка, что сущность пользователя можно отредактировать (Способ 1: методы TestRestTemplate.put() и TestRestTemplate.getForObject())
        // 1.1. получение сущности (читателя) из базы данных
        Reader expectedReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + 1, Reader.class);
        // 1.2. изменение значений полей сущности (читателя)
        expectedReader.setName("New name");
        expectedReader.setSecondName("New second name");
        expectedReader.setSurname("New surname");
        expectedReader.setPersonalNumber(666);
        // 1.3. получение JSON-объекта из сущности (читателя)
        String expectedJson = mapper.writeValueAsString(expectedReader);
        // 1.4. проверка, что возвращаемый в результате запроса JSON-объект совпадает с отправленной сущностью (читателя)
        //     * отправка PUT-запроса на редактирование полей сущности (читателя)
        restTemplate.put("http://localhost:" + port + "/readers", expectedReader);
        //     * повторное получение сущности (читателя) из БД и проверка, что возвращаемый в результате запроса JSON-объект
        //       совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), String.class))
                  .isEqualTo(expectedJson);
        //
        // 2. Проверка, что сущность пользователя можно отредактировать (Способ 2: методы TestRestTemplate.put() и TestRestTemplate.getForObject())
        // 2.1. получение сущности (читателя) из базы данных
        expectedReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + 2, Reader.class);
        // 2.2. изменение значений полей сущности (читателя)
        expectedReader.setName("New name");
        expectedReader.setSecondName("New second name");
        expectedReader.setSurname("New surname");
        expectedReader.setPersonalNumber(777);
        // 2.3. проверка, что возвращаемый в результате запроса JSON-объект совпадает с отправленной сущностью (читателя)
        //     * отправка PUT-запроса на редактирование полей сущности (читателя)
        restTemplate.put("http://localhost:" + port + "/readers", expectedReader);
        //     * повторное получение сущности (читателя) из БД и проверка, что возвращаемый в результате запроса JSON-объект
        //       совпадает с запрошенной по ID сущностью (читателя)
        Reader actualReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        //     * проверка, что возвращаемая в результате запроса сущность (читатель) совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 3. Проверка, что сущность пользователя можно отредактировать (Способ 3: методы TestRestTemplate.put() и TestRestTemplate.getForEntity())
        // 3.1. получение сущности (читателя) из базы данных
        ResponseEntity<Reader> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + 3, Reader.class);
        expectedReader = responseEntity.getBody();
        // 3.2. изменение значений полей сущности (читателя)
        expectedReader.setName("New name");
        expectedReader.setSecondName("New second name");
        expectedReader.setSurname("New surname");
        expectedReader.setPersonalNumber(888);
        // 3.3. проверка, что возвращаемый в результате запроса JSON-объект совпадает с отправленной сущностью (читателя)
        //     * отправка PUT-запроса на редактирование полей сущности (читателя)
        restTemplate.put("http://localhost:" + port + "/readers", expectedReader);
        //     * повторное получение сущности (читателя) из БД и проверка, что возвращаемый в результате запроса JSON-объект
        //       совпадает с запрошенной по ID сущностью (читателя)
        responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        actualReader = responseEntity.getBody();
        //     * проверка, что возвращаемая в результате запроса сущность (читатель) совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 4. Проверка статуса и содержания успешного HTTP-ответа
        // 4.1. выполнение всех операций, указанных в п. 3
        // 4.2. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 4.3. сравнение содержания HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(expectedReader);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedReader.getId());
        Assertions.assertThat(responseEntity.getBody().getName()).isEqualTo(expectedReader.getName());
        Assertions.assertThat(responseEntity.getBody().getSecondName()).isEqualTo(expectedReader.getSecondName());
        Assertions.assertThat(responseEntity.getBody().getSurname()).isEqualTo(expectedReader.getSurname());
        Assertions.assertThat(responseEntity.getBody().getPersonalNumber()).isEqualTo(expectedReader.getPersonalNumber());
        Assertions.assertThat(responseEntity.getBody().getBooks()).isEmpty();
        //
        // 5. Проведение всех вышеуказанных проверок с помощью метода TestRestTemplate.exchange() - ОСНОВНОЙ
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/" + 4, HttpMethod.GET, HttpEntity.EMPTY, Reader.class);
        expectedReader = responseEntity.getBody();
        expectedReader.setName("New name");
        expectedReader.setSecondName("New second name");
        expectedReader.setSurname("New surname");
        expectedReader.setPersonalNumber(999);
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.PUT, new HttpEntity<>(expectedReader), Reader.class);
        actualReader = responseEntity.getBody();
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 6. Проверка статуса и содержания ошибочного HTTP-ответа
        // 6.1. получение заведомо отсутствующей сущности (читателя)
        Reader unavailableReader = TEST_READER_4;
        unavailableReader.setId(5L);
        // 6.2. отправка PUT-запроса на редактирование полей отсутствующей сущности (читателя)
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.PUT, new HttpEntity<>(unavailableReader), Reader.class);
        // 6.3. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        // 6.4. проверка, что тело HTTP-ответа отсутствует
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/readers/{id}")
    @Order(4)
    void deleteBook() throws Exception {
        // Подготовительный этап - проверка того, что в базе данных присутствуют добавленные сущности (читатели)
        // - получение всех сущностей (читателей), имеющихся в БД (можно использовать методы TestRestTemplate.exchange() и TestRestTemplate.getForEntity())
        Reader[] response = restTemplate.getForObject("http://localhost:" + port + "/readers", Reader[].class);
        // - преобразование тела полученного HTTP-ответа в коллекцию объектов
        Collection<Reader> actualReaders = Arrays.asList(response);
        // - проверка того, что коллекция сущностей (читателей) в БД не равна нулю
        Assertions.assertThat(actualReaders).isNotNull();
        // - проверка того, что размер коллекции сущностей (читателей) в БД больше нуля
        Assertions.assertThat(actualReaders).isNotEmpty();
        //
        // 1. Проверка, что сущность (читателя) можно удалить (Способ 1: методы TestRestTemplate.delete() и TestRestTemplate.getForObject())
        // 1.1. получение сущности (читателя) из базы данных
        Reader expectedReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + 1, Reader.class);
        // 1.2. проверка, что в результате запроса сущностью (читателя) удаляется из БД
        //     * отправка DELETE-запроса на удаление сущности (читателя)
        restTemplate.delete("http://localhost:" + port + "/readers/" + expectedReader.getId());
        //     * повторное получение сущности (читателя) из БД и проверка, что он отсутствует в БД
        Assertions.assertThat(restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), String.class))
                  .isNull();
        // 1.3. проверка, что в результате повторного запроса на удаление получен ожидаемый HTTP-статус
        ResponseEntity<Reader> responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/" + expectedReader.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Reader.class);
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        Assertions.assertThat(responseEntity.getBody()).isNull();
        //
        // 2. Проверка, что сущность (читателя) можно удалить (Способ 2: методы TestRestTemplate.delete() и TestRestTemplate.getForObject())
        // 2.1. получение сущности (читателя) из базы данных
        expectedReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + 2, Reader.class);
        // 2.2. проверка, что в результате запроса сущностью (читателя) удаляется из БД
        //     * отправка DELETE-запроса на удаление сущности (читателя)
        restTemplate.delete("http://localhost:" + port + "/readers/" + expectedReader.getId());
        //     * повторное получение сущности (читателя) из БД и проверка, что в результате возвращается null
        Reader actualReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        Assertions.assertThat(actualReader).isNull();
        //
        // 3. Проверка, что сущность (читателя) можно удалить (Способ 3: методы TestRestTemplate.delete() и TestRestTemplate.getForEntity())
        // 3.1. получение сущности (читателя) из базы данных
        responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + 3, Reader.class);
        expectedReader = responseEntity.getBody();
        // 3.2. проверка, что в результате запроса сущностью (читателя) удаляется из БД
        //     * отправка DELETE-запроса на удаление сущности (читателя)
        restTemplate.delete("http://localhost:" + port + "/readers/" + expectedReader.getId());
        //     * повторное получение сущности (читателя) из БД и проверка, что в результате возвращается null
        responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/readers/" + expectedReader.getId(), Reader.class);
        actualReader = responseEntity.getBody();
        //     * проверка, что возвращаемая в результате запроса сущность (читатель) совпадает с запрошенной по ID сущностью (читателя)
        Assertions.assertThat(actualReader).isNull();
        //     * проверка HTTP-статуса GET-запроса
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        //
        // 4. Проверка, что сущность (читателя) можно удалить (Способ 4 - ОСНОВНОЙ: метод TestRestTemplate.exchange())
        // 4.1. получение сущности (читателя) из базы данных
        expectedReader = restTemplate.getForObject("http://localhost:" + port + "/readers/" + 4, Reader.class);
        // 4.2. отправка DELETE-запроса на удаление сущности (читателя)
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/" + expectedReader.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Reader.class);
        // 4.3. сравнение статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 4.3. сравнение содержания HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(expectedReader);
        Assertions.assertThat(responseEntity.getBody().getId()).isEqualTo(expectedReader.getId());
        Assertions.assertThat(responseEntity.getBody().getName()).isEqualTo(expectedReader.getName());
        Assertions.assertThat(responseEntity.getBody().getSecondName()).isEqualTo(expectedReader.getSecondName());
        Assertions.assertThat(responseEntity.getBody().getSurname()).isEqualTo(expectedReader.getSurname());
        Assertions.assertThat(responseEntity.getBody().getPersonalNumber()).isEqualTo(expectedReader.getPersonalNumber());
        Assertions.assertThat(responseEntity.getBody().getBooks()).isEmpty();
        //
        // 5. Проверка, что отсутствующую сущность (читателя) нельзя удалить
        // 5.1. отправка повторного DELETE-запроса на удаление сущности (читателя)
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/" + expectedReader.getId(), HttpMethod.DELETE, HttpEntity.EMPTY, Reader.class);
        // 4.3. сравнение статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        // 4.3. сравнение содержания HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers")
    @Order(5)
    void getAllReaders() throws Exception {
        // 1. Проверка статуса HTTP-ответа при пустой БД
        ResponseEntity<String> readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(readersResponseEntity.getBody()).isNull();
        // 2. Заполнение БД сущностями (читателями) и проверка, что все они внесены в БД (ID сущностей присваиваются
        //    автоматически, поэтому не совпадают с указанными в константах)
        Collection<Reader> expectedReaders = new ArrayList<>();
        Reader expectedReader = restTemplate.postForObject("http://localhost:" + port + "/readers", TEST_READER_1, Reader.class);
        expectedReaders.add(expectedReader);
        expectedReader = restTemplate.postForObject("http://localhost:" + port + "/readers", TEST_READER_2, Reader.class);
        expectedReaders.add(expectedReader);
        expectedReader = restTemplate.postForObject("http://localhost:" + port + "/readers", TEST_READER_3, Reader.class);
        expectedReaders.add(expectedReader);
        expectedReader = restTemplate.postForObject("http://localhost:" + port + "/readers", TEST_READER_4, Reader.class);
        expectedReaders.add(expectedReader);
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Collection<Reader> actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        // 3. Проверка нахождения коллекции сущностей (читателей) по содержимому одного из полей
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers?partOfNameSecondNameOrSurname=" + expectedReader.getName(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
        actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        expectedReaders = new ArrayList<>(List.of(expectedReader));
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers?partOfNameSecondNameOrSurname=" + expectedReader.getSecondName(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
        actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers?partOfNameSecondNameOrSurname=" + expectedReader.getSurname(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
        actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        // 4. Проверка статуса HTTP-ответа при отсутствии в БД искомой сущности (читателя)
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers?partOfNameSecondNameOrSurname=" + "random string", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(readersResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/name")
    @Order(6)
    void getReadersByName() throws Exception {
        ResponseEntity<String> readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Collection<Reader> expectedReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Collection<Reader> actualReaders;
        Collection<Reader> expectedCollection;
        for (Reader expectedReader : expectedReaders) {
            readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/name?partOfName=" + expectedReader.getName(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
            actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
            expectedCollection = new ArrayList<>(List.of(expectedReader));
            Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
            Assertions.assertThat(actualReaders).isEqualTo(expectedCollection);
        }
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/name?partOfName=" + "random string", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(readersResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/secondName")
    @Order(7)
    void getReadersBySecondName() throws Exception {
        ResponseEntity<String> readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Collection<Reader> expectedReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Collection<Reader> actualReaders;
        Collection<Reader> expectedCollection;
        for (Reader expectedReader : expectedReaders) {
            readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/secondName?partOfSecondName=" + expectedReader.getSecondName(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
            actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
            expectedCollection = new ArrayList<>(List.of(expectedReader));
            Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
            Assertions.assertThat(actualReaders).isEqualTo(expectedCollection);
        }
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/secondName?partOfSecondName=" + "random string", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(readersResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/surname")
    @Order(8)
    void getReadersBySurname() throws Exception {
        ResponseEntity<String> readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Collection<Reader> expectedReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        Collection<Reader> actualReaders;
        Collection<Reader> expectedCollection;
        for (Reader expectedReader : expectedReaders) {
            readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/surname?partOfSurname=" + expectedReader.getSurname(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
            actualReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
            expectedCollection = new ArrayList<>(List.of(expectedReader));
            Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(readersResponseEntity.getBody()).isNotNull();
            Assertions.assertThat(actualReaders).isEqualTo(expectedCollection);
        }
        readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/surname?partOfSurname=" + "random string", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(readersResponseEntity.getBody()).isNull();
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/books")
    @Order(9)
    void getReaderBooks() throws Exception {
        ResponseEntity<String> readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        Collection<Reader> expectedReaders = Arrays.asList(mapper.readValue(readersResponseEntity.getBody(), Reader[].class));
        for (Reader expectedReader : expectedReaders) {
            readersResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/books?id=" + expectedReader.getId(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
            Assertions.assertThat(readersResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            Assertions.assertThat(readersResponseEntity.getBody()).isNull();
        }
        restTemplate.exchange("http://localhost:" + port + "/books", HttpMethod.POST, new HttpEntity<>(TEST_BOOK_1), String.class);
        restTemplate.exchange("http://localhost:" + port + "/books", HttpMethod.POST, new HttpEntity<>(TEST_BOOK_2), String.class);
        restTemplate.exchange("http://localhost:" + port + "/books", HttpMethod.POST, new HttpEntity<>(TEST_BOOK_3), String.class);
        restTemplate.exchange("http://localhost:" + port + "/books", HttpMethod.POST, new HttpEntity<>(TEST_BOOK_4), String.class);
        restTemplate.exchange("http://localhost:" + port + "/books", HttpMethod.POST, new HttpEntity<>(TEST_BOOK_5), String.class);
        Collection<Book> actualBooks;
        Collection<Book> expectedCollection;
        ResponseEntity<String> booksResponseEntity;
        int counter = 1;
        for (Reader expectedReader : expectedReaders) {
            restTemplate.exchange("http://localhost:" + port + "/manage?bookId=" + counter + "&readerId=" + expectedReader.getId(), HttpMethod.POST, HttpEntity.EMPTY, String.class);
            Book expectedBook = restTemplate.exchange("http://localhost:" + port + "/books/" + counter, HttpMethod.GET, HttpEntity.EMPTY, Book.class).getBody();
            expectedCollection = new ArrayList<>(List.of(expectedBook));
            booksResponseEntity = restTemplate.exchange("http://localhost:" + port + "/readers/books?id=" + expectedReader.getId(), HttpMethod.GET, HttpEntity.EMPTY, String.class);
            Assertions.assertThat(booksResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(booksResponseEntity.getBody()).isNotNull();
            actualBooks = Arrays.asList(mapper.readValue(booksResponseEntity.getBody(), Book[].class));
            Assertions.assertThat(actualBooks).isEqualTo(expectedCollection);
            counter++;
        }
    }
}
```

## Пример 2:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.2 Тестирование с помощью WebMvcTest]**](/conspect/05.md/#542-тестирование-с-помощью-webmvctest)

- изображение загружается с помощью аннотации `@RequestParam`:

```java

@WebMvcTest(controllers = BooksController.class, properties = "books.covers.dir.path=src/test/resources/testImages/result")
@DisplayName(value = "http://localhost:8080/books")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BooksController controller;
    @SpyBean
    private BookService bookService;
    @SpyBean
    private BookCoverService bookCoverService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCoverRepository bookcoverRepository;
    private final String sourceImageDir = "src/test/resources/testImages/result";

    @TestConfiguration
    public static class BooksControllerTestConfiguration {
        @Bean(name = "bookServiceImplDB")
        public BookService bookService(BookRepository bookRepository) {
            return new BookServiceImplDB(bookRepository);
        }

        @Bean(name = "bookCoverServiceImpl")
        public BookCoverService bookCoverService(BookService bookService, BookCoverRepository bookcoverRepository) {
            return new BookCoverServiceImpl(bookService, bookcoverRepository);
        }
    }

    @Test
    @DisplayName(value = "POST http://localhost:8080/books")
    @Order(1)
    void createBook() throws Exception {
        // 1. Подготовительный этап
        // 1.1. получение сущности из констант
        Book expectedBook = TEST_BOOK_1;
        // 1.2. получение JSON-объекта из сущности при помощи статического метода toJsonObject() (в конце класса)
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        // 1.3. приведение значений сущности в соответствии с логикой сервиса
        expectedBook.setId(0);
        // 1.4. изменение поведения репозитория при вызове метода save() и получении на вход изменённой в соответствии
        //      с логикой сервиса сущности
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        //
        // 2. Проведение теста
        // 2.1. формирование запроса
        mockMvc.perform(
                       // 2.1.1. определение типа и адреса запроса
                       MockMvcRequestBuilders.post("/books")
                                             // 2.1.2. добавление контента запроса
                                             .content(expectedBookJson.toString())
                                             // 2.1.3. добавление типа контента запроса
                                             .contentType(MediaType.APPLICATION_JSON)
                                             // 2.1.3. добавление типа контента ответа
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               // 2.2. проверка ответа
               // 2.2.1. проверка статуса ответа
               .andExpect(MockMvcResultMatchers.status().isOk())
               // 2.2.2. проверка содержимого ответа
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook.setId(1);
        expectedBook = TEST_BOOK_2;
        expectedBookJson = toJsonObject(expectedBook);
        expectedBook.setId(0);
        when(bookRepository.save(eq(expectedBook))).thenThrow(new RuntimeException());
        mockMvc.perform(
                       MockMvcRequestBuilders.post("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        expectedBook.setId(2);
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}")
    @Order(2)
    void readBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.findById(eq(expectedBook.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_3;
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(null);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/books")
    @Order(3)
    void updateBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_4;
        expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/books/{id}")
    @Order(4)
    void deleteBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_5;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books")
    @Order(5)
    void getAllBooks() throws Exception {
        List<Book> expectedBookCollection = new ArrayList<>(List.of(TEST_BOOK_1, TEST_BOOK_2, TEST_BOOK_3, TEST_BOOK_4, TEST_BOOK_5));
        when(bookRepository.findAll()).thenReturn(expectedBookCollection);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(TEST_BOOK_2.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(TEST_BOOK_2.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value(TEST_BOOK_2.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(TEST_BOOK_2.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(TEST_BOOK_3.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value(TEST_BOOK_3.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value(TEST_BOOK_3.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].year").value(TEST_BOOK_3.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].year").value(TEST_BOOK_4.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].id").value(TEST_BOOK_5.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].title").value(TEST_BOOK_5.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].author").value(TEST_BOOK_5.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].year").value(TEST_BOOK_5.getYear()));
        List<Book> bookCollection1 = new ArrayList<>(List.of(TEST_BOOK_1));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_1.getAuthor()), eq(TEST_BOOK_1.getAuthor()))).thenReturn(bookCollection1);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_1.getAuthor())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()));
        List<Book> bookCollection2 = new ArrayList<>(List.of(TEST_BOOK_4));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_4.getTitle()), eq(TEST_BOOK_4.getTitle()))).thenReturn(bookCollection2);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_4.getTitle())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_4.getYear()));
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(anyString(), anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=random string")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test // файл передаётся с помощью аннотации @RequestParam
    @DisplayName(value = "POST http://localhost:8080/books/{id}/cover")
    @Order(6)
    void uploadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Задание поведения репозитория родительского сервиса (bookRepository) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        // 2. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                                                "1.jpg",                   // оригинальное название файла
                                                                MediaType.IMAGE_JPEG_VALUE,// тип файла
                                                                inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        // 7. Создание массива байт из загруженного изображения и сравнение его с оригиналом
        byte[] outputImage = Files.readAllBytes(Path.of(sourceImageDir + "/1.jpg"));
        Assertions.assertArrayEquals(inputImage, outputImage);
        //
        //                          Проверка запроса при отсутствующем родительском объекте
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookService.findBook(eq(TEST_BOOK_2.getId()))).thenReturn(null);
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                              sourceImageDir + "/2.jpg",
                              inputImage.length,
                              MediaType.IMAGE_JPEG.toString(),
                              generatePreview(TEST_BOOK_IMAGE_PATH_2),
                              TEST_BOOK_2);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "2.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/2.jpg")));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_3.getId()))).thenReturn(Optional.of(TEST_BOOK_3));
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_3);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(null);
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "3.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_3.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/3.jpg")));
        // 8. Удаление ненужной директории с результатами теста, после его завершения
        FileUtils.deleteDirectory(new File(sourceImageDir));
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover/preview")
    @Order(7)
    void downloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(new BookCover()));
        // 2. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_3.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover")
    @Order(8)
    void testDownloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        TEST_BOOK_IMAGE_PATH_1.toString(),
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1)));
        //
        //                          Проверка запроса при отсутствующем файле
        // 1. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                              TEST_BOOK_IMAGE_PATH_2 + "random",
                              inputImage.length,
                              MediaType.IMAGE_JPEG.toString(),
                              generatePreview(TEST_BOOK_IMAGE_PATH_2),
                              TEST_BOOK_2);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadGateway())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 2. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 3. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    private static JSONObject toJsonObject(Book book) throws JSONException {
        JSONObject bookJson = new JSONObject();
        bookJson.put("id", book.getId());
        bookJson.put("title", book.getTitle());
        bookJson.put("author", book.getAuthor());
        bookJson.put("year", book.getYear());
        bookJson.put("reader", book.getReader());
        return bookJson;
    }

    private static byte[] generatePreview(Path path) {
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
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
                preview = new BufferedImage(100, height, bufferedImage.getType());
                width = 100;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 100);
                preview = new BufferedImage(width, 100, bufferedImage.getType());
                height = 100;
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

    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
```

## Пример 3:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.2 Тестирование с помощью WebMvcTest]**](/conspect/05.md/#542-тестирование-с-помощью-webmvctest)

- изображение загружается с помощью аннотаций `@RequestPart` и `@RequestBody`, тестирование ответа с _HashMap_:

```java

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class HomeworkApplicationTest {
    @Value("${user.image.dir.path}")
    private String userImageDir;
    @Value("${ad.image.dir.path}")
    private String adsImageDir;
    @Value("${source.image.dir.path}")
    private String sourceImageDir;
    private AdDTO AD_1_DTO = new AdDTO();
    private AdDTO AD_2_DTO = new AdDTO();
    private AdDTO AD_3_DTO = new AdDTO();
    private AdsDTO ADS_DTO = new AdsDTO();
    private AdsDTO ADS_USER_DTO = new AdsDTO();
    private AdsDTO ADS_ADMIN_DTO = new AdsDTO();
    private CommentDTO COMMENT_1_DTO = new CommentDTO();
    private CommentDTO COMMENT_2_DTO = new CommentDTO();
    private CommentDTO COMMENT_3_DTO = new CommentDTO();
    private CommentsDTO COMMENTS_DTO = new CommentsDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_1_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_2_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateAdDTO CREATE_OR_UPDATE_AD_3_DTO = new CreateOrUpdateAdDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_1_DTO = new CreateOrUpdateCommentDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_2_DTO = new CreateOrUpdateCommentDTO();
    private CreateOrUpdateCommentDTO CREATE_OR_UPDATE_COMMENT_3_DTO = new CreateOrUpdateCommentDTO();
    private ExtendedAdDTO EXTENDED_AD_1_DTO = new ExtendedAdDTO();
    private ExtendedAdDTO EXTENDED_AD_2_DTO = new ExtendedAdDTO();
    private ExtendedAdDTO EXTENDED_AD_3_DTO = new ExtendedAdDTO();
    private LoginDTO LOGIN_USER_DTO = new LoginDTO();
    private LoginDTO LOGIN_ADMIN_DTO = new LoginDTO();
    private LoginDTO LOGIN_ANOTHER_USER_DTO = new LoginDTO();
    private NewPasswordDTO NEW_PASSWORD_USER_DTO = new NewPasswordDTO();
    private NewPasswordDTO NEW_PASSWORD_ANOTHER_USER_DTO = new NewPasswordDTO();
    private NewPasswordDTO NEW_PASSWORD_ADMIN_DTO = new NewPasswordDTO();
    private RegisterDTO REGISTER_USER_DTO = new RegisterDTO();
    private RegisterDTO REGISTER_ADMIN_DTO = new RegisterDTO();
    private RegisterDTO REGISTER_ANOTHER_USER_DTO = new RegisterDTO();
    private UpdateUserDTO UPDATE_USER_DTO = new UpdateUserDTO();
    private UpdateUserDTO UPDATE_ADMIN_DTO = new UpdateUserDTO();
    private UserDTO USER_DTO = new UserDTO();
    private UserDTO ADMIN_DTO = new UserDTO();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @MockBean
    PasswordEncoderConfig passwordEncoderConfig;
    @MockBean
    DaoAuthenticationProvider daoAuthenticationProvider;
    @MockBean
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    SecurityFilterChainConfig securityFilterChainConfig;
    @MockBean
    AdRepository adRepository;
    @MockBean
    CommentRepository commentRepository;
    @MockBean
    UserRepository userRepository;
    @Autowired
    AdMapper adMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;
    @SpyBean
    AdsServiceImpl adsService;
    @SpyBean
    AuthenticationServiceImpl authenticationService;
    @SpyBean
    ImageServiceImpl imageService;
    @SpyBean
    UsersServiceImpl usersService;
    @Autowired
    AdsController adsController;
    @Autowired
    AuthenticationController authenticationController;
    @Autowired
    ImageController imageController;
    @Autowired
    UsersController usersController;
    @MockBean
    Clock clock;
    @MockBean
    ClockConfig clockConfig;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        AD_1_DTO.setAuthor(AD_1.getAuthor().getId());
        AD_1_DTO.setImage("/" + AD_1.getImage());
        AD_1_DTO.setPk(AD_1.getPk());
        AD_1_DTO.setPrice(AD_1.getPrice());
        AD_1_DTO.setTitle(AD_1.getTitle());
        AD_2_DTO.setAuthor(AD_2.getAuthor().getId());
        AD_2_DTO.setImage("/" + AD_2.getImage());
        AD_2_DTO.setPk(AD_2.getPk());
        AD_2_DTO.setPrice(AD_2.getPrice());
        AD_2_DTO.setTitle(AD_2.getTitle());
        AD_3_DTO.setAuthor(AD_3.getAuthor().getId());
        AD_3_DTO.setImage("/" + AD_3.getImage());
        AD_3_DTO.setPk(AD_3.getPk());
        AD_3_DTO.setPrice(AD_3.getPrice());
        AD_3_DTO.setTitle(AD_3.getTitle());
        ADS_DTO.setCount(3);
        ADS_DTO.setResults(List.of(AD_1_DTO, AD_2_DTO, AD_3_DTO));
        ADS_USER_DTO.setCount(2);
        ADS_USER_DTO.setResults(List.of(AD_1_DTO, AD_2_DTO));
        ADS_ADMIN_DTO.setCount(1);
        ADS_ADMIN_DTO.setResults(List.of(AD_3_DTO));
        COMMENT_1_DTO.setAuthor(COMMENT_1.getAuthor().getId());
        COMMENT_1_DTO.setAuthorImage("/" + COMMENT_1.getAuthorImage());
        COMMENT_1_DTO.setAuthorFirstName(COMMENT_1.getAuthorFirstName());
        COMMENT_1_DTO.setCreatedAt(COMMENT_1.getCreatedAt());
        COMMENT_1_DTO.setPk(COMMENT_1.getPk());
        COMMENT_1_DTO.setText(COMMENT_1.getText());
        COMMENT_2_DTO.setAuthor(COMMENT_2.getAuthor().getId());
        COMMENT_2_DTO.setAuthorImage("/" + COMMENT_2.getAuthorImage());
        COMMENT_2_DTO.setAuthorFirstName(COMMENT_2.getAuthorFirstName());
        COMMENT_2_DTO.setCreatedAt(COMMENT_2.getCreatedAt());
        COMMENT_2_DTO.setPk(COMMENT_2.getPk());
        COMMENT_2_DTO.setText(COMMENT_2.getText());
        COMMENT_3_DTO.setAuthor(COMMENT_3.getAuthor().getId());
        COMMENT_3_DTO.setAuthorImage("/" + COMMENT_3.getAuthorImage());
        COMMENT_3_DTO.setAuthorFirstName(COMMENT_3.getAuthorFirstName());
        COMMENT_3_DTO.setCreatedAt(COMMENT_3.getCreatedAt());
        COMMENT_3_DTO.setPk(COMMENT_3.getPk());
        COMMENT_3_DTO.setText(COMMENT_3.getText());
        COMMENTS_DTO.setCount(3);
        COMMENTS_DTO.setResults(List.of(COMMENT_1_DTO, COMMENT_2_DTO, COMMENT_3_DTO));
        CREATE_OR_UPDATE_AD_1_DTO.setTitle(AD_1.getTitle());
        CREATE_OR_UPDATE_AD_1_DTO.setPrice(AD_1.getPrice());
        CREATE_OR_UPDATE_AD_1_DTO.setDescription(AD_1.getDescription());
        CREATE_OR_UPDATE_AD_2_DTO.setTitle(AD_2.getTitle());
        CREATE_OR_UPDATE_AD_2_DTO.setPrice(AD_2.getPrice());
        CREATE_OR_UPDATE_AD_2_DTO.setDescription(AD_2.getDescription());
        CREATE_OR_UPDATE_AD_3_DTO.setTitle(AD_3.getTitle());
        CREATE_OR_UPDATE_AD_3_DTO.setPrice(AD_3.getPrice());
        CREATE_OR_UPDATE_AD_3_DTO.setDescription(AD_3.getDescription());
        CREATE_OR_UPDATE_COMMENT_1_DTO.setText(COMMENT_1.getText());
        CREATE_OR_UPDATE_COMMENT_2_DTO.setText(COMMENT_2.getText());
        CREATE_OR_UPDATE_COMMENT_3_DTO.setText(COMMENT_3.getText());
        EXTENDED_AD_1_DTO.setPk(AD_1.getPk());
        EXTENDED_AD_1_DTO.setAuthorFirstName(AD_1.getAuthor().getFirstName());
        EXTENDED_AD_1_DTO.setAuthorLastName(AD_1.getAuthor().getLastName());
        EXTENDED_AD_1_DTO.setDescription(AD_1.getDescription());
        EXTENDED_AD_1_DTO.setEmail(AD_1.getAuthor().getEmail());
        EXTENDED_AD_1_DTO.setImage("/" + AD_1.getImage());
        EXTENDED_AD_1_DTO.setPhone(AD_1.getAuthor().getPhone());
        EXTENDED_AD_1_DTO.setPrice(AD_1.getPrice());
        EXTENDED_AD_1_DTO.setTitle(AD_1.getTitle());
        EXTENDED_AD_2_DTO.setPk(AD_2.getPk());
        EXTENDED_AD_2_DTO.setAuthorFirstName(AD_2.getAuthor().getFirstName());
        EXTENDED_AD_2_DTO.setAuthorLastName(AD_2.getAuthor().getLastName());
        EXTENDED_AD_2_DTO.setDescription(AD_2.getDescription());
        EXTENDED_AD_2_DTO.setEmail(AD_2.getAuthor().getEmail());
        EXTENDED_AD_2_DTO.setImage("/" + AD_2.getImage());
        EXTENDED_AD_2_DTO.setPhone(AD_2.getAuthor().getPhone());
        EXTENDED_AD_2_DTO.setPrice(AD_2.getPrice());
        EXTENDED_AD_2_DTO.setTitle(AD_2.getTitle());
        EXTENDED_AD_3_DTO.setPk(AD_3.getPk());
        EXTENDED_AD_3_DTO.setAuthorFirstName(AD_3.getAuthor().getFirstName());
        EXTENDED_AD_3_DTO.setAuthorLastName(AD_3.getAuthor().getLastName());
        EXTENDED_AD_3_DTO.setDescription(AD_3.getDescription());
        EXTENDED_AD_3_DTO.setEmail(AD_3.getAuthor().getEmail());
        EXTENDED_AD_3_DTO.setImage("/" + AD_3.getImage());
        EXTENDED_AD_3_DTO.setPhone(AD_3.getAuthor().getPhone());
        EXTENDED_AD_3_DTO.setPrice(AD_3.getPrice());
        EXTENDED_AD_3_DTO.setTitle(AD_3.getTitle());
        LOGIN_USER_DTO.setUsername(USER.getEmail());
        LOGIN_USER_DTO.setPassword(USER.getPassword());
        LOGIN_ADMIN_DTO.setUsername(ADMIN.getEmail());
        LOGIN_ADMIN_DTO.setPassword(ADMIN.getPassword());
        LOGIN_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        LOGIN_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        NEW_PASSWORD_USER_DTO.setCurrentPassword(USER.getPassword());
        NEW_PASSWORD_USER_DTO.setNewPassword(ADMIN.getPassword());
        NEW_PASSWORD_ADMIN_DTO.setCurrentPassword(ADMIN.getPassword());
        NEW_PASSWORD_ADMIN_DTO.setNewPassword(USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setCurrentPassword(ANOTHER_USER.getPassword());
        NEW_PASSWORD_ANOTHER_USER_DTO.setNewPassword(ADMIN.getPassword());
        REGISTER_USER_DTO.setUsername(USER.getEmail());
        REGISTER_USER_DTO.setPassword(USER.getPassword());
        REGISTER_USER_DTO.setFirstName(USER.getFirstName());
        REGISTER_USER_DTO.setLastName(USER.getLastName());
        REGISTER_USER_DTO.setPhone(USER.getPhone());
        REGISTER_USER_DTO.setRole(USER.getRole());
        REGISTER_ADMIN_DTO.setUsername(ADMIN.getEmail());
        REGISTER_ADMIN_DTO.setPassword(ADMIN.getPassword());
        REGISTER_ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        REGISTER_ADMIN_DTO.setLastName(ADMIN.getLastName());
        REGISTER_ADMIN_DTO.setPhone(ADMIN.getPhone());
        REGISTER_ADMIN_DTO.setRole(ADMIN.getRole());
        REGISTER_ANOTHER_USER_DTO.setUsername(ANOTHER_USER.getEmail());
        REGISTER_ANOTHER_USER_DTO.setPassword(ANOTHER_USER.getPassword());
        REGISTER_ANOTHER_USER_DTO.setFirstName(ANOTHER_USER.getFirstName());
        REGISTER_ANOTHER_USER_DTO.setLastName(ANOTHER_USER.getLastName());
        REGISTER_ANOTHER_USER_DTO.setPhone(ANOTHER_USER.getPhone());
        REGISTER_ANOTHER_USER_DTO.setRole(ANOTHER_USER.getRole());
        UPDATE_USER_DTO.setFirstName(USER.getFirstName());
        UPDATE_USER_DTO.setLastName(USER.getLastName());
        UPDATE_USER_DTO.setPhone(USER.getPhone());
        UPDATE_ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        UPDATE_ADMIN_DTO.setLastName(ADMIN.getLastName());
        UPDATE_ADMIN_DTO.setPhone(ADMIN.getPhone());
        USER_DTO.setId(USER.getId());
        USER_DTO.setEmail(USER.getEmail());
        USER_DTO.setFirstName(USER.getFirstName());
        USER_DTO.setLastName(USER.getLastName());
        USER_DTO.setPhone(USER.getPhone());
        USER_DTO.setRole(USER.getRole());
        USER_DTO.setImage("/" + USER.getImage());
        ADMIN_DTO.setId(ADMIN.getId());
        ADMIN_DTO.setEmail(ADMIN.getEmail());
        ADMIN_DTO.setFirstName(ADMIN.getFirstName());
        ADMIN_DTO.setLastName(ADMIN.getLastName());
        ADMIN_DTO.setPhone(ADMIN.getPhone());
        ADMIN_DTO.setRole(ADMIN.getRole());
        ADMIN_DTO.setImage("/" + ADMIN.getImage());
        lenient().when(clockConfig.clock()).thenReturn(clock);
        lenient().when(clock.millis()).thenReturn(111111L);
        lenient().when(userRepository.findByEmail(USER.getEmail())).thenReturn(Optional.of(USER));
        lenient().when(userRepository.findByEmail(ADMIN.getEmail())).thenReturn(Optional.of(ADMIN));
        lenient().when(userRepository.save(USER)).thenReturn(USER);
        lenient().when(userRepository.save(ADMIN)).thenReturn(ADMIN);
        lenient().when(userRepository.save(ANOTHER_USER_REGISTER)).thenReturn(ANOTHER_USER);
        lenient().when(passwordEncoderConfig.passwordEncoder()).thenReturn(passwordEncoder);
        lenient().when(passwordEncoder.encode(USER.getPassword())).thenReturn(USER.getPassword());
        lenient().when(passwordEncoder.encode(ADMIN.getPassword())).thenReturn(ADMIN.getPassword());
        lenient().when(passwordEncoder.encode(ANOTHER_USER.getPassword())).thenReturn(ANOTHER_USER.getPassword());
        lenient().when(passwordEncoder.matches(USER.getPassword(), USER.getPassword())).thenReturn(true);
        lenient().when(passwordEncoder.matches(ADMIN.getPassword(), ADMIN.getPassword())).thenReturn(true);
        lenient().when(adRepository.findAll()).thenReturn(ADS);
        lenient().when(adRepository.findByPk(AD_1.getPk())).thenReturn(Optional.of(AD_1));
        lenient().when(adRepository.findByPk(AD_2.getPk())).thenReturn(Optional.of(AD_2));
        lenient().when(adRepository.findByPk(AD_3.getPk())).thenReturn(Optional.of(AD_3));
        lenient().doNothing().when(adRepository).delete(any(Ad.class));
        lenient().when(adRepository.save(AD_1)).thenReturn(AD_1);
        lenient().when(adRepository.save(AD_2)).thenReturn(AD_2);
        lenient().when(adRepository.save(AD_3)).thenReturn(AD_3);
        lenient().when(adRepository.save(AD_1_CREATE)).thenReturn(AD_1);
        lenient().when(adRepository.save(AD_2_CREATE)).thenReturn(AD_2);
        lenient().when(adRepository.save(AD_3_CREATE)).thenReturn(AD_3);
        lenient().when(adRepository.findByAuthor(USER)).thenReturn(ADS_USER);
        lenient().when(adRepository.findByAuthor(ADMIN)).thenReturn(ADS_ADMIN);
        lenient().when(commentRepository.findByAd(AD_1)).thenReturn(COMMENTS);
        lenient().when(commentRepository.save(COMMENT_1_SAVE)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2_SAVE)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3_SAVE)).thenReturn(COMMENT_3);
        lenient().when(commentRepository.save(COMMENT_1)).thenReturn(COMMENT_1);
        lenient().when(commentRepository.save(COMMENT_2)).thenReturn(COMMENT_2);
        lenient().when(commentRepository.save(COMMENT_3)).thenReturn(COMMENT_3);
        lenient().when(commentRepository.findByPk(COMMENT_1.getPk())).thenReturn(Optional.of(COMMENT_1));
        lenient().when(commentRepository.findByPk(COMMENT_2.getPk())).thenReturn(Optional.of(COMMENT_2));
        lenient().when(commentRepository.findByPk(COMMENT_3.getPk())).thenReturn(Optional.of(COMMENT_3));
        lenient().doNothing().when(commentRepository).delete(any(Comment.class));
    }

    @Test
    void setPassword() throws Exception {
        mockMvc.perform(post("/users/set_password")
                                .with(user(USER.getEmail()).password(USER.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_USER_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/users/set_password")
                                .with(user(ADMIN.getEmail()).password(ADMIN.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ADMIN_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/users/set_password")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ANOTHER_USER_DTO)))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/users/set_password")
                                .with(user(ADMIN.getEmail()).password(ADMIN.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(NEW_PASSWORD_ANOTHER_USER_DTO)))
               .andExpect(status().isForbidden());
    }

    @Test
    void me() throws Exception {
        mockMvc.perform(get("/users/me")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(USER_DTO.getId()))
               .andExpect(jsonPath("$.email").value(USER_DTO.getEmail()))
               .andExpect(jsonPath("$.firstName").value(USER_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(USER_DTO.getLastName()))
               .andExpect(jsonPath("$.role").value(String.valueOf(USER_DTO.getRole())))
               .andExpect(jsonPath("$.image").value(USER_DTO.getImage()));
        mockMvc.perform(get("/users/me")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(ADMIN_DTO.getId()))
               .andExpect(jsonPath("$.email").value(ADMIN_DTO.getEmail()))
               .andExpect(jsonPath("$.firstName").value(ADMIN_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(ADMIN_DTO.getLastName()))
               .andExpect(jsonPath("$.role").value(String.valueOf(ADMIN_DTO.getRole())))
               .andExpect(jsonPath("$.image").value(ADMIN_DTO.getImage()));
        mockMvc.perform(get("/users/me")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void meUpdate() throws Exception {
        mockMvc.perform(patch("/users/me")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_USER_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value(UPDATE_USER_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(UPDATE_USER_DTO.getLastName()))
               .andExpect(jsonPath("$.phone").value(UPDATE_USER_DTO.getPhone()));
        mockMvc.perform(patch("/users/me")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_ADMIN_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value(UPDATE_ADMIN_DTO.getFirstName()))
               .andExpect(jsonPath("$.lastName").value(UPDATE_ADMIN_DTO.getLastName()))
               .andExpect(jsonPath("$.phone").value(UPDATE_ADMIN_DTO.getPhone()));
        mockMvc.perform(patch("/users/me")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(UPDATE_ADMIN_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void meImage() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "user.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "user.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(user("user@test.com").password("123")))
               .andExpect(status().isOk());
        byte[] result = Files.readAllBytes(Path.of(userImageDir, USER.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(result);
                InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "admin.jpg"));
        multipartFile = new MockMultipartFile("image", "admin.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(user("admin@test.com").password("321")))
               .andExpect(status().isOk());
        result = Files.readAllBytes(Path.of(userImageDir, ADMIN.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(result);
                InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "user.jpg"));
        multipartFile = new MockMultipartFile("image", "user.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users/me/image").file(multipartFile)
                                                                      .with(anonymous()))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_USER_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_ADMIN_DTO)))
               .andExpect(status().isOk());
        mockMvc.perform(post("/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(LOGIN_ANOTHER_USER_DTO)))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void register() throws Exception {
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_USER_DTO)))
               .andExpect(status().isBadRequest());
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_ADMIN_DTO)))
               .andExpect(status().isBadRequest());
        mockMvc.perform(post("/register")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(REGISTER_ANOTHER_USER_DTO)))
               .andExpect(status().isCreated());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/ads")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_1_DTO.getTitle()))
               .andExpect(jsonPath("$.results[1].author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.results[1].pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.results[1].title").value(AD_2_DTO.getTitle()))
               .andExpect(jsonPath("$.results[2].author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.results[2].pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.results[2].title").value(AD_3_DTO.getTitle()));
    }

    @Test
        // файл передаётся с помощью аннотации @RequestPart
    void postAd() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        MockMultipartFile propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_1_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("user@test.com").password("123"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_1_DTO.getTitle()));
        byte[] outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("user@test.com").password("123"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_3_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(user("admin@test.com").password("321"))
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_3_DTO.getTitle()));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_3.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        propertiesFile = new MockMultipartFile("properties", "properties", MediaType.APPLICATION_JSON_VALUE, new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_1_DTO).getBytes());
        mockMvc.perform(multipart(HttpMethod.POST, "/ads").file(multipartFile).file(propertiesFile)
                                                          .with(anonymous())
                                                          .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                          .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void getAd() throws Exception {
        mockMvc.perform(get("/ads/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pk").value(EXTENDED_AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.authorFirstName").value(EXTENDED_AD_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.authorLastName").value(EXTENDED_AD_1_DTO.getAuthorLastName()))
               .andExpect(jsonPath("$.description").value(EXTENDED_AD_1_DTO.getDescription()))
               .andExpect(jsonPath("$.email").value(EXTENDED_AD_1_DTO.getEmail()))
               .andExpect(jsonPath("$.image").value(EXTENDED_AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.phone").value(EXTENDED_AD_1_DTO.getPhone()))
               .andExpect(jsonPath("$.price").value(EXTENDED_AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(EXTENDED_AD_1_DTO.getTitle()));
        mockMvc.perform(get("/ads/1")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.pk").value(EXTENDED_AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.authorFirstName").value(EXTENDED_AD_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.authorLastName").value(EXTENDED_AD_1_DTO.getAuthorLastName()))
               .andExpect(jsonPath("$.description").value(EXTENDED_AD_1_DTO.getDescription()))
               .andExpect(jsonPath("$.email").value(EXTENDED_AD_1_DTO.getEmail()))
               .andExpect(jsonPath("$.image").value(EXTENDED_AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.phone").value(EXTENDED_AD_1_DTO.getPhone()))
               .andExpect(jsonPath("$.price").value(EXTENDED_AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(EXTENDED_AD_1_DTO.getTitle()));
        mockMvc.perform(get("/ads/1")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/ads/4")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteAd() throws Exception {
        mockMvc.perform(delete("/ads/1")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isNoContent());
        mockMvc.perform(delete("/ads/1")
                                .with(user("admin@test.com").password("321")))
               .andExpect(status().isNoContent());
        mockMvc.perform(delete("/ads/1")
                                .with(anonymous()))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/ads/3")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isForbidden());
        mockMvc.perform(delete("/ads/4")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateAd() throws Exception {
        mockMvc.perform(patch("/ads/2")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(patch("/ads/2")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(patch("/ads/2")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/ads/3")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(patch("/ads/4")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_AD_2_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void getMe() throws Exception {
        mockMvc.perform(get("/ads/me")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_USER_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_1_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_1_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_1_DTO.getTitle()))
               .andExpect(jsonPath("$.results[1].author").value(AD_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].image").value(AD_2_DTO.getImage()))
               .andExpect(jsonPath("$.results[1].pk").value(AD_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].price").value(AD_2_DTO.getPrice()))
               .andExpect(jsonPath("$.results[1].title").value(AD_2_DTO.getTitle()));
        mockMvc.perform(get("/ads/me")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(ADS_ADMIN_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(AD_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].image").value(AD_3_DTO.getImage()))
               .andExpect(jsonPath("$.results[0].pk").value(AD_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].price").value(AD_3_DTO.getPrice()))
               .andExpect(jsonPath("$.results[0].title").value(AD_3_DTO.getTitle()));
        mockMvc.perform(get("/ads/me")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
    }

    @Test
        // файл передаётся с помощью аннотации @RequestBody
    void patchAdImage() throws Exception {
        byte[] inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        MockMultipartFile multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/1/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_1.getImage()))));
        byte[] outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/2/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_2.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/1/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_1.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_1.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_2.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_2.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/2/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_2.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_2.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(user("admin@test.com").password("321"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(content().string(String.valueOf(Path.of(adsImageDir, AD_3.getImage()))));
        outputImage = Files.readAllBytes(Path.of(adsImageDir, AD_3.getImage()));
        try (
                InputStream inputStream1 = new ByteArrayInputStream(inputImage);
                InputStream inputStream2 = new ByteArrayInputStream(outputImage)
        ) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_3.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_3.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/3/image").file(multipartFile)
                                                                   .with(anonymous())
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        inputImage = Files.readAllBytes(Path.of(sourceImageDir, "ad_1.jpg"));
        multipartFile = new MockMultipartFile("image", "ad_1.jpg", MediaType.IMAGE_JPEG_VALUE, inputImage);
        mockMvc.perform(multipart(HttpMethod.PATCH, "/ads/4/image").file(multipartFile)
                                                                   .with(user("user@test.com").password("123"))
                                                                   .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
        // тестирование содержания HashMap
    void getAdComments() throws Exception {
        mockMvc.perform(get("/ads/1/comments")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(COMMENTS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[0].authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[0].createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[0].pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].text").value(COMMENT_1_DTO.getText()))
               .andExpect(jsonPath("$.results[1].author").value(COMMENT_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].authorImage").value(COMMENT_2_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[1].authorFirstName").value(COMMENT_2_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[1].createdAt").value(COMMENT_2_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[1].pk").value(COMMENT_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].text").value(COMMENT_2_DTO.getText()))
               .andExpect(jsonPath("$.results[2].author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[2].authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[2].createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[2].pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(get("/ads/1/comments")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.count").value(COMMENTS_DTO.getCount()))
               .andExpect(jsonPath("$.results[0].author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[0].authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[0].authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[0].createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[0].pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.results[0].text").value(COMMENT_1_DTO.getText()))
               .andExpect(jsonPath("$.results[1].author").value(COMMENT_2_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[1].authorImage").value(COMMENT_2_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[1].authorFirstName").value(COMMENT_2_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[1].createdAt").value(COMMENT_2_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[1].pk").value(COMMENT_2_DTO.getPk()))
               .andExpect(jsonPath("$.results[1].text").value(COMMENT_2_DTO.getText()))
               .andExpect(jsonPath("$.results[2].author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.results[2].authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.results[2].authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.results[2].createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.results[2].pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.results[2].text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(get("/ads/1/comments")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/ads/2/comments")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void postAdComment() throws Exception {
        mockMvc.perform(post("/ads/1/comments")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(post("/ads/1/comments")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_3_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_3_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_3_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_3_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_3_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_3_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_3_DTO.getText()));
        mockMvc.perform(post("/ads/3/comments")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/ads/4/comments")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void deleteAdComment() throws Exception {
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(user("admin@test.com").password("321"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk());
        mockMvc.perform(delete("/ads/1/comments/1")
                                .with(anonymous())
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(delete("/ads/1/comments/3")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(delete("/ads/2/comments/1")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        mockMvc.perform(delete("/ads/1/comments/4")
                                .with(user("user@test.com").password("123"))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void updateAdComment() throws Exception {
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(user("admin@test.com").password("321"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.author").value(COMMENT_1_DTO.getAuthor()))
               .andExpect(jsonPath("$.authorImage").value(COMMENT_1_DTO.getAuthorImage()))
               .andExpect(jsonPath("$.authorFirstName").value(COMMENT_1_DTO.getAuthorFirstName()))
               .andExpect(jsonPath("$.createdAt").value(COMMENT_1_DTO.getCreatedAt()))
               .andExpect(jsonPath("$.pk").value(COMMENT_1_DTO.getPk()))
               .andExpect(jsonPath("$.text").value(COMMENT_1_DTO.getText()));
        mockMvc.perform(patch("/ads/1/comments/1")
                                .with(anonymous())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isUnauthorized());
        mockMvc.perform(patch("/ads/1/comments/3")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_3_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isForbidden());
        mockMvc.perform(patch("/ads/2/comments/1")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        mockMvc.perform(patch("/ads/1/comments/4")
                                .with(user("user@test.com").password("123"))
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(new ObjectMapper().writeValueAsString(CREATE_OR_UPDATE_COMMENT_1_DTO))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
    }

    @Test
    void downloadImage() throws Exception {
        byte[] result = mockMvc.perform(get("/" + USER.getImage())
                                                .with(user("user@test.com").password("123")))
                               .andExpect(status().isOk())
                               .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + ADMIN.getImage())
                                         .with(user("user@test.com").password("123")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + USER.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, USER.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + ADMIN.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(userImageDir, ADMIN.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_1.getImage())
                                         .with(user("user@test.com").password("123")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_1.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_2.getImage())
                                         .with(user("admin@test.com").password("321")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_2.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        result = mockMvc.perform(get("/" + AD_3.getImage())
                                         .with(anonymous()))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsByteArray();
        try (InputStream inputStream1 = new ByteArrayInputStream(result);
             InputStream inputStream2 = new FileInputStream(Path.of(adsImageDir, AD_3.getImage()).toFile())) {
            assertTrue(IOUtils.contentEquals(inputStream1, inputStream2));
        }
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(user("user@test.com").password("123")))
               .andExpect(status().isBadRequest());
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(user("admin@test.com").password("321")))
               .andExpect(status().isBadRequest());
        mockMvc.perform(get("/wrong_image_name.jpg")
                                .with(anonymous()))
               .andExpect(status().isBadRequest());
    }
}
```

## Пример 4:

> [[_оглавление_]](../README.md/#54-тестирование-web-приложений)

> [**[5.4.2 Тестирование с помощью WebMvcTest]**](/conspect/05.md/#542-тестирование-с-помощью-webmvctest)

- тестирование с использованием профиля тестирования:

```java
@WebMvcTest(controllers = BooksController.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration
@DisplayName(value = "http://localhost:8080/books")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BooksControllerTestWithProfile {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private BooksController controller;
    @SpyBean
    private BookService bookService;
    @SpyBean
    private BookCoverService bookCoverService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private BookCoverRepository bookcoverRepository;
    private final String sourceImageDir = "src/test/resources/testImages/result";

    @TestConfiguration
    public static class BooksControllerTestConfiguration {
        @Bean(name = "bookServiceImplDB")
        public BookService bookService(BookRepository bookRepository) {
            return new BookServiceImplDB(bookRepository);
        }

        @Bean(name = "bookCoverServiceImpl")
        public BookCoverService bookCoverService(BookService bookService, BookCoverRepository bookcoverRepository) {
            return new BookCoverServiceImpl(bookService, bookcoverRepository);
        }
    }

    @Test
    @DisplayName(value = "POST http://localhost:8080/books")
    @Order(1)
    void createBook() throws Exception {
        // 1. Подготовительный этап
        // 1.1. получение сущности из констант
        Book expectedBook = TEST_BOOK_1;
        // 1.2. получение JSON-объекта из сущности при помощи статического метода toJsonObject() (в конце класса)
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        // 1.3. приведение значений сущности в соответствии с логикой сервиса
        expectedBook.setId(0);
        // 1.4. изменение поведения репозитория при вызове метода save() и получении на вход изменённой в соответствии
        //      с логикой сервиса сущности
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        //
        // 2. Проведение теста
        // 2.1. формирование запроса
        mockMvc.perform(
                       // 2.1.1. определение типа и адреса запроса
                       MockMvcRequestBuilders.post("/books")
                                             // 2.1.2. добавление контента запроса
                                             .content(expectedBookJson.toString())
                                             // 2.1.3. добавление типа контента запроса
                                             .contentType(MediaType.APPLICATION_JSON)
                                             // 2.1.3. добавление типа контента ответа
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               // 2.2. проверка ответа
               // 2.2.1. проверка статуса ответа
               .andExpect(MockMvcResultMatchers.status().isOk())
               // 2.2.2. проверка содержимого ответа
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook.setId(1);
        expectedBook = TEST_BOOK_2;
        expectedBookJson = toJsonObject(expectedBook);
        expectedBook.setId(0);
        when(bookRepository.save(eq(expectedBook))).thenThrow(new RuntimeException());
        mockMvc.perform(
                       MockMvcRequestBuilders.post("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        expectedBook.setId(2);
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}")
    @Order(2)
    void readBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.findById(eq(expectedBook.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_3;
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(null);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/books")
    @Order(3)
    void updateBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        JSONObject expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookRepository.save(eq(expectedBook))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_4;
        expectedBookJson = toJsonObject(expectedBook);
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.put("/books")
                                             .content(expectedBookJson.toString())
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/books/{id}")
    @Order(4)
    void deleteBook() throws Exception {
        Book expectedBook = TEST_BOOK_1;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(true);
        when(bookService.findBook(eq(expectedBook.getId()))).thenReturn(TEST_BOOK_1);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.year").value(TEST_BOOK_1.getYear()));
        expectedBook = TEST_BOOK_5;
        when(bookRepository.existsById(eq(expectedBook.getId()))).thenReturn(false);
        mockMvc.perform(
                       MockMvcRequestBuilders.delete("/books/" + expectedBook.getId())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books")
    @Order(5)
    void getAllBooks() throws Exception {
        List<Book> expectedBookCollection = new ArrayList<>(List.of(TEST_BOOK_1, TEST_BOOK_2, TEST_BOOK_3, TEST_BOOK_4, TEST_BOOK_5));
        when(bookRepository.findAll()).thenReturn(expectedBookCollection);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(TEST_BOOK_2.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value(TEST_BOOK_2.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value(TEST_BOOK_2.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[1].year").value(TEST_BOOK_2.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(TEST_BOOK_3.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value(TEST_BOOK_3.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].author").value(TEST_BOOK_3.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[2].year").value(TEST_BOOK_3.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[3].year").value(TEST_BOOK_4.getYear()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].id").value(TEST_BOOK_5.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].title").value(TEST_BOOK_5.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].author").value(TEST_BOOK_5.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[4].year").value(TEST_BOOK_5.getYear()));
        List<Book> bookCollection1 = new ArrayList<>(List.of(TEST_BOOK_1));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_1.getAuthor()), eq(TEST_BOOK_1.getAuthor()))).thenReturn(bookCollection1);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_1.getAuthor())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_1.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_1.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_1.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_1.getYear()));
        List<Book> bookCollection2 = new ArrayList<>(List.of(TEST_BOOK_4));
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(eq(TEST_BOOK_4.getTitle()), eq(TEST_BOOK_4.getTitle()))).thenReturn(bookCollection2);
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=" + TEST_BOOK_4.getTitle())
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
               .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(TEST_BOOK_4.getId()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value(TEST_BOOK_4.getTitle()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value(TEST_BOOK_4.getAuthor()))
               .andExpect(MockMvcResultMatchers.jsonPath("$[0].year").value(TEST_BOOK_4.getYear()));
        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        when(bookRepository.findByAuthorContainsIgnoreCaseOrTitleContainsIgnoreCase(anyString(), anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books?authorOrTitle=random string")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .accept(MediaType.APPLICATION_JSON)
                       )
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test // файл передаётся с помощью аннотации @RequestParam
    @DisplayName(value = "POST http://localhost:8080/books/{id}/cover")
    @Order(6)
    void uploadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Задание поведения репозитория родительского сервиса (bookRepository) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(TEST_BOOK_1));
        // 2. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                                                "1.jpg",                   // оригинальное название файла
                                                                MediaType.IMAGE_JPEG_VALUE,// тип файла
                                                                inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        // 7. Создание массива байт из загруженного изображения и сравнение его с оригиналом
        byte[] outputImage = Files.readAllBytes(Path.of(sourceImageDir + "/1.jpg"));
        Assertions.assertArrayEquals(inputImage, outputImage);
        //
        //                          Проверка запроса при отсутствующем родительском объекте
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookService.findBook(eq(TEST_BOOK_2.getId()))).thenReturn(null);
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                              sourceImageDir + "/2.jpg",
                              inputImage.length,
                              MediaType.IMAGE_JPEG.toString(),
                              generatePreview(TEST_BOOK_IMAGE_PATH_2),
                              TEST_BOOK_2);
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(cover);
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "2.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadRequest())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/2.jpg")));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задание поведения родительского сервиса (bookServiceImplDB) для случая поиска сущности (книги)
        //    дочерним сервисом bookCoverServiceImpl
        when(bookRepository.findById(eq(TEST_BOOK_3.getId()))).thenReturn(Optional.of(TEST_BOOK_3));
        // 2. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_3);
        // 3. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 4. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.save(any(BookCover.class))).thenReturn(null);
        // 5. Создание макета передаваемого в запросе файла класса MultipartFile
        multipartFile = new MockMultipartFile("file",              // имя параметра в HTTP-запросе
                                              "3.jpg",                   // оригинальное название файла
                                              MediaType.IMAGE_JPEG_VALUE,// тип файла
                                              inputImage);               // байт-код файла
        // 6. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.multipart(HttpMethod.POST, "/books/" + TEST_BOOK_3.getId() + "/cover")
                                             .file(multipartFile)
                                             .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        // 7. Проверка того, что файл не загрузился
        Assertions.assertTrue(Files.notExists(Path.of(sourceImageDir + "/3.jpg")));
        // 8. Удаление ненужной директории с результатами теста, после его завершения
        FileUtils.deleteDirectory(new File(sourceImageDir));
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover/preview")
    @Order(7)
    void downloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        sourceImageDir + "/1.jpg",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Objects.requireNonNull(generatePreview(TEST_BOOK_IMAGE_PATH_1))));
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(new BookCover()));
        // 2. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_3.getId() + "/cover/preview")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isForbidden())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/books/{id}/cover")
    @Order(8)
    void testDownloadCover() throws Exception {
        //                                  Проверка успешного запроса
        // 1. Создания массива байт из источника изображения
        byte[] inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        BookCover cover = new BookCover(TEST_BOOK_1.getId(),
                                        TEST_BOOK_IMAGE_PATH_1.toString(),
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_1),
                                        TEST_BOOK_1);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_1.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_1.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.IMAGE_JPEG_VALUE))
               .andExpect(MockMvcResultMatchers.content().bytes(Files.readAllBytes(TEST_BOOK_IMAGE_PATH_1)));
        //
        //                          Проверка запроса при отсутствующем файле
        // 1. Создания массива байт из источника изображения
        inputImage = Files.readAllBytes(TEST_BOOK_IMAGE_PATH_2);
        // 2. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover(TEST_BOOK_2.getId(),
                                        TEST_BOOK_IMAGE_PATH_2 + "random",
                                        inputImage.length,
                                        MediaType.IMAGE_JPEG.toString(),
                                        generatePreview(TEST_BOOK_IMAGE_PATH_2),
                                        TEST_BOOK_2);
        // 3. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 4. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isBadGateway())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
        //
        //                       Проверка запроса при обработке которого возникла ошибка
        // 1. Создание возвращаемой сущности для хранения превью изображения и пути к нему на жёстком диске (BookCover)
        cover = new BookCover();
        // 2. Задания поведения репозитория дочернего сервиса (bookCoverRepository) для случаев работы с сущностью для
        //    хранения данных изображения (BookCover)
        when(bookcoverRepository.findByBookId(eq(TEST_BOOK_2.getId()))).thenReturn(Optional.of(cover));
        // 3. Создание запроса и проверка его ответа
        mockMvc.perform(
                       MockMvcRequestBuilders.get("/books/" + TEST_BOOK_2.getId() + "/cover")
                                             .content("")
                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                             .accept(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE))
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    private static JSONObject toJsonObject(Book book) throws JSONException {
        JSONObject bookJson = new JSONObject();
        bookJson.put("id", book.getId());
        bookJson.put("title", book.getTitle());
        bookJson.put("author", book.getAuthor());
        bookJson.put("year", book.getYear());
        bookJson.put("reader", book.getReader());
        return bookJson;
    }

    private static byte[] generatePreview(Path path) {
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
                height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
                preview = new BufferedImage(100, height, bufferedImage.getType());
                width = 100;
            } else {
                width = bufferedImage.getWidth() / (bufferedImage.getHeight() / 100);
                preview = new BufferedImage(width, 100, bufferedImage.getType());
                height = 100;
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

    private static String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
```