package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Collection<User> getAllUsers();

    Set<User> getAllFriends(Long id);

    Set<User> getCommonFriends(Long userId1, Long userId2);

    void addFriend(Long userId1, Long userId2);

    void removeFriend(Long userId1, Long userId2);
}
