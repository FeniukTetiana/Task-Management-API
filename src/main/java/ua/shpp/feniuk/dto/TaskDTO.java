package ua.shpp.feniuk.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ua.shpp.feniuk.Status;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class TaskDTO {
    private Integer id;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false)
    private String description;

    @NotBlank(message = "Data cannot be blank")
    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private Status status;
}
