package by.bsuir.seabattle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    private long id;

    private Set<String> players;

    private Set<Step> steps;

    private long going;

    private long winner;

    private Status status;

    public enum Status {
        CREATING,
        JOINING,
        RUNNING,
        ENDED
    }
}