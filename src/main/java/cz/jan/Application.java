package cz.jan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaRepositories
@EnableRetry(order = Ordered.LOWEST_PRECEDENCE - 4) // To be before @Transactional
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}