package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final Map<Long, User> storage = new HashMap<>();
    private long idCounter = 0L;

    @PostMapping
    @Validated(CreateGroup.class)
    public User createUser(@RequestBody @Valid User user) {
        log.info("Received request to create user with details: {}", user);

        Long id = getIdNext();
        user.setName(user.getDisplayName());
        user.setId(id);
        storage.put(id, user);

        log.info("Created user with id: {}", id);
        return user;
    }

    @PutMapping
    @Validated(UpdateGroup.class)
    public User updateUser(@RequestBody @Valid User user) {
        Long id = user.getId();
        log.info("Received request to update user with id: {}", id);

        User savedUser = storage.get(id);
        if (savedUser == null) {
            log.error("User with id {} not found", id);
            throw new NotFoundException("User with id " + id + " not found");
        }
        storage.put(id, user);

        log.info("Updated user with id: {}", id);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Fetching all users");
        return storage.values();
    }

    private long getIdNext() {
        return ++idCounter;
    }
}
