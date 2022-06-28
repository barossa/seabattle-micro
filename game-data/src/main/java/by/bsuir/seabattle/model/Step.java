package by.bsuir.seabattle.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "steps")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Player player;

    private int x;

    private int y;

    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
