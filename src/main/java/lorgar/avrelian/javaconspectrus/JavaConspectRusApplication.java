package lorgar.avrelian.javaconspectrus;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
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
@EnableScheduling
@EnableMethodSecurity(securedEnabled = true)
public class JavaConspectRusApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaConspectRusApplication.class, args);
	}
}
