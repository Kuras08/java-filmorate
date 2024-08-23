package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserRepository {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    Collection<User> getAllUsers();

    Set<User> getAllFriends(Long userId);

    Set<User> getCommonFriends(Long userId1, Long userId2);

    void addFriend(Long userId1, Long userId2);

    void removeFriend(Long userId1, Long userId2);

    boolean existsById(Long id);
}
