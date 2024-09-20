package lorgar.avrelian.javaconspectrus.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.Random;

@Configuration
public class RandomizeConfig {
    @Value("${rand.diapazon}")
    private long seed;

    @Bean
    @Scope(value = "prototype")
    public Random myRandomInstance() {
        return new Random(seed);
    }
    @Bean
    @Primary
    @Scope(value = "prototype")
    public Random defaultInstance() {
        return new Random();
    }
}
