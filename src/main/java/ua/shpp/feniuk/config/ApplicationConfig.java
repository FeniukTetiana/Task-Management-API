package ua.shpp.feniuk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.repository.TaskRepository;

import java.time.LocalDate;

@Configuration
public class ApplicationConfig {

    @Value("${application.version}")
    private String appVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API")
                        .version(appVersion)
                        .description("Documentation for the API"));
    }

    @Bean
    CommandLineRunner initDatabase(TaskRepository taskRepository) {
        return args -> {
            if (taskRepository.count() == 0) {
                TaskEntity taskEntity = TaskEntity.builder()
                        .title("string")
                        .description("string")
                        .createdAt(LocalDate.now())
                        .status(Status.PLANNED)
                        .build();
                taskRepository.save(taskEntity);
            }
        };
    }
}
