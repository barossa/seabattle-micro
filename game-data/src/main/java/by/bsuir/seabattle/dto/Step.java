package by.bsuir.seabattle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {

    private long id;

    private String player;

    private int x;

    private int y;

    private LocalDateTime createdAt;

}
