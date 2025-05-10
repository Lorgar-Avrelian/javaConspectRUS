package lorgar.avrelian.spring.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Random;

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