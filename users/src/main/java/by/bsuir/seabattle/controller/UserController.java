package by.bsuir.seabattle.controller;

import by.bsuir.seabattle.model.User;
import by.bsuir.seabattle.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User addUser(@RequestBody User player) {
        userService.save(player);
        return player;
    }

    @GetMapping
    public Page<User> findUsers(Pageable pageable, Sort sort){
        return userService.find(pageable, sort);
    }

}
