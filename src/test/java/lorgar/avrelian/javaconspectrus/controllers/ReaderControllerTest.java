package lorgar.avrelian.javaconspectrus.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReaderControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ReaderController controller;

    @Test
    void createReader() {
    }

    @Test
    void readReader() {
    }

    @Test
    void updateReader() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void getAllReaders() {
    }

    @Test
    void getReadersByName() {
    }

    @Test
    void getReadersBySecondName() {
    }

    @Test
    void getReadersBySurname() {
    }

    @Test
    void getReaderBooks() {
    }
}