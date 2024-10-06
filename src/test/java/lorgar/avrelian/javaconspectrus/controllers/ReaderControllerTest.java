package lorgar.avrelian.javaconspectrus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lorgar.avrelian.javaconspectrus.models.Reader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReaderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName(value = "POST http://localhost:8080/readers test")
    @Order(1)
    void createReader() throws Exception {
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
        // 3. Проверка, что новая сущность (читатель) создаётся в базе данных (Способ 3: метод TestRestTemplate.postForEntity())
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
        Assertions.assertThat(responseEntity.getBody().getBooks()).isNull();
        //
        // 5. Проверка статуса и содержания ошибочного HTTP-ответа
        // 5.1. получение новой заведомо некорректной сущности (читателя) из констант
        expectedReader = TEST_READER_4;
        // 5.2. получение HTTP-ответа
        responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/readers", expectedReader, Reader.class);
        // 5.3. сравнение полученного статуса HTTP-ответа с ожидаемым
        Assertions.assertThat(responseEntity.getStatusCode().value()).isEqualTo(405);
        // 5.4. проверка, что тело HTTP-ответа отсутствует
        Assertions.assertThat(responseEntity.getBody()).isNull();
        //
        // 6. Проведение всех вышеуказанных проверок с помощью метода TestRestTemplate.exchange()
        expectedReader = TEST_READER_5;
        responseEntity = restTemplate.exchange("http://localhost:" + port + "/readers", HttpMethod.POST, new HttpEntity<>(expectedReader), Reader.class);
        actualReader = responseEntity.getBody();
        Assertions.assertThat(actualReader).isEqualTo(expectedReader);
        //
        // 7. Проверка того, что все верные новые сущности (читатели) добавлены в БД
        // 7.1. создание ожидаемой коллекции сущностей (читателей)
        Collection<Reader> expectedReaders = new ArrayList<>(List.of(TEST_READER_1, TEST_READER_2, TEST_READER_3, TEST_READER_5));
        // 7.2. получение всех сущностей (читателей), имеющихся в БД (можно использовать методы TestRestTemplate.getForEntity() и TestRestTemplate.getForObject())
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/readers",HttpMethod.GET, HttpEntity.EMPTY, String.class);
        // 7.3. преобразование тела полученного HTTP-ответа в коллекцию объектов
        Collection<Reader> actualReaders = Arrays.asList(mapper.readValue(response.getBody(), Reader[].class));
        // 7.4. сравнение полученной коллекции сущностей с ожидаемой
        Assertions.assertThat(actualReaders).isEqualTo(expectedReaders);
        // 7.5 проверка того, что заведомо некорректная сущность отсутствует в полученной коллекции объектов
        Assertions.assertThat(actualReaders).doesNotContain(TEST_READER_4);
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/{id} test")
    @Order(2)
    void readReader() {
    }

    @Test
    @DisplayName(value = "PUT http://localhost:8080/readers test")
    @Order(3)
    void updateReader() {
    }

    @Test
    @DisplayName(value = "DELETE http://localhost:8080/readers/{id} test")
    @Order(4)
    void deleteBook() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers test")
    @Order(5)
    void getAllReaders() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/name test")
    @Order(6)
    void getReadersByName() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/secondName test")
    @Order(7)
    void getReadersBySecondName() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/surname test")
    @Order(8)
    void getReadersBySurname() {
    }

    @Test
    @DisplayName(value = "GET http://localhost:8080/readers/books test")
    @Order(9)
    void getReaderBooks() {
    }
}