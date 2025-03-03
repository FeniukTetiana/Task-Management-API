package ua.shpp.feniuk.entity;

import jakarta.persistence.*;
import lombok.*;
import ua.shpp.feniuk.Status;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String title;

    private String description;

    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;
}
