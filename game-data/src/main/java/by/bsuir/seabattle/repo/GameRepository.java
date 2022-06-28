package by.bsuir.seabattle.repo;

import by.bsuir.seabattle.model.Game;
import by.bsuir.seabattle.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findGameByPlayersContainsAndStatusContains(Player player, Game.Status status);

    Page<Game> findGamesByStatusContains(Game.Status status, Pageable pageable);
}
