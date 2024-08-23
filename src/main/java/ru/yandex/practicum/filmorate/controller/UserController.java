package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Validated(CreateGroup.class)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid User user) {
        log.info("Received request to create user with details: {}", user);
        User createdUser = userService.createUser(user);
        log.info("Created user with id: {}", createdUser.getId());
        return createdUser;
    }

    @PutMapping
    @Validated(UpdateGroup.class)
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Received request to update user with id: {}", user.getId());
        User updatedUser = userService.updateUser(user);
        log.info("Updated user with id: {}", updatedUser.getId());
        return updatedUser;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long id) {
        log.info("Received request to get user with id: {}", id);
        User user = userService.getUserById(id);
        log.info("Returning user with id: {}", user.getId());
        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {
        log.info("Fetching all users");
        Collection<User> users = userService.getAllUsers();
        log.info("Returning {} users", users.size());
        return users;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Received request to add friend with id: {} to user with id: {}", friendId, id);
        userService.addFriend(id, friendId);
        log.info("Added friend with id: {} to user with id: {}", friendId, id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Received request to remove friend with id: {} from user with id: {}", friendId, id);
        userService.removeFriend(id, friendId);
        log.info("Removed friend with id: {} from user with id: {}", friendId, id);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllFriends(@PathVariable Long id) {
        log.info("Received request to get friends of user with id: {}", id);
        Collection<User> friends = userService.getAllFriends(id);
        if (friends.isEmpty()) {
            log.info("User with id {} has no friends.", id);
        }
        log.info("Returning {} friends for user with id: {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Received request to get common friends of user with id: {} and user with id: {}", id, otherId);
        Collection<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Returning {} common friends for users with id: {} and id: {}", commonFriends.size(), id, otherId);
        return commonFriends;
    }
}

