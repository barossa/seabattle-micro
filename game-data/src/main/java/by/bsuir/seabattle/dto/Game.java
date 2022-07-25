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

    private String id;

    private Set<String> players;

    private Set<Step> steps;

    private String going;

    private String winner;

    private Status status;

    public enum Status {
        CREATING,
        JOINING,
        RUNNING,
        ENDED
    }
}
