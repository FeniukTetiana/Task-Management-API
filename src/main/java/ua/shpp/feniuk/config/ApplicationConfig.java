package ua.shpp.feniuk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.repository.TaskRepository;

import java.time.LocalDate;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${api.version}")
    private String appVersion;

    private final MessageSource messageSource;

    @Bean
    public OpenAPI customOpenAPI() {
        String title = messageSource.getMessage("api.title", null, Locale.getDefault());
        String description = messageSource.getMessage("api.description", null, Locale.getDefault());
        String tagTasks = messageSource.getMessage("api.tag.tasks", null, Locale.getDefault());

        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(appVersion)
                        .description(description))
                .addTagsItem(new Tag().name("tasks").description(tagTasks));
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
