package lorgar.avrelian.javaconspectrus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;

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