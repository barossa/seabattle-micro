package by.bsuir.seabattle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@Table(name = "games")
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany
    private Set<Player> players;

    @OneToMany
    private Set<Step> steps;

    @ManyToOne
    private Player going;

    @ManyToOne
    private Player winner;

    private Status status;

    public enum Status {
        JOINING,
        RUNNING,
        ENDED
    }
}
