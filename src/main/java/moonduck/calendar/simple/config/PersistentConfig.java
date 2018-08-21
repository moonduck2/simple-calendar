package moonduck.calendar.simple.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "moonduck.calendar.simple")
public class PersistentConfig {

}
