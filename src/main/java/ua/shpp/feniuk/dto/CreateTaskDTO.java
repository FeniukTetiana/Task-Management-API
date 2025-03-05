package ua.shpp.feniuk.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDTO {
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 150)
    private String title;

    private String description;
}
