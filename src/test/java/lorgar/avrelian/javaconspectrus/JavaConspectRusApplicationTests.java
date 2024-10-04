package lorgar.avrelian.javaconspectrus;

import lorgar.avrelian.javaconspectrus.controllers.BooksController;
import lorgar.avrelian.javaconspectrus.controllers.ReaderController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
