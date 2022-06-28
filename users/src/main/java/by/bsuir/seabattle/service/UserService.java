package by.bsuir.seabattle.service;

import by.bsuir.seabattle.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface UserService {

    Page<User> find(Pageable pageable, Sort sort);

    void save(User player);

    User find(long id);

}
