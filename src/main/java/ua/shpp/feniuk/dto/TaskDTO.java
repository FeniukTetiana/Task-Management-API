package ua.shpp.feniuk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
    @JsonIgnore
    @Schema(hidden = true)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 5, max = 150)
    @Column(nullable = false)
    private String title;

    private String description;

    @NotBlank(message = "Data cannot be blank")
    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private Status status;
}
