package lorgar.avrelian.javaconspectrus;

import lorgar.avrelian.javaconspectrus.controllers.BooksController;
import lorgar.avrelian.javaconspectrus.controllers.ReaderController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JavaConspectRusApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ReaderController readerController;
	@Autowired
	private BooksController booksController;

	@Test
	void contextLoads() {
		Assertions.assertThat(readerController).isNotNull();
		Assertions.assertThat(booksController).isNotNull();
	}

}
