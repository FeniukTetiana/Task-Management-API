package ua.shpp.feniuk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ua.shpp.feniuk.Status;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
//    @JsonIgnore
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 150)
    private String title;

    private String description;

    private LocalDate createdAt;

    private Status status;
}
