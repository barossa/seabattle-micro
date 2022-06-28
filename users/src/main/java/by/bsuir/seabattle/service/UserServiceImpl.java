package by.bsuir.seabattle.service;

import by.bsuir.seabattle.exception.UserNotFoundException;
import by.bsuir.seabattle.model.User;
import by.bsuir.seabattle.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository playerRepository;

    @Override
    public Page<User> find(Pageable pageable, Sort sort) {
        return playerRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
    }

    @Override
    public void save(User player) {
        playerRepository.save(player);
    }

    @Override
    public User find(long id) {
        return playerRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
