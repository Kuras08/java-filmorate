package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        checkUserExists(user.getId());
        userRepository.updateUser(user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return Optional.ofNullable(userRepository.getUserById(id))
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Set<User> getAllFriends(Long id) {
        checkUserExists(id);
        return userRepository.getAllFriends(id);
    }

    @Override
    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        checkUserExists(userId1);
        checkUserExists(userId2);
        return userRepository.getCommonFriends(userId1, userId2);
    }

    @Override
    public void addFriend(Long userId1, Long userId2) {
        checkUserExists(userId1);
        checkUserExists(userId2);
        userRepository.addFriend(userId1, userId2);
    }

    @Override
    public void removeFriend(Long userId1, Long userId2) {
        checkUserExists(userId1);
        checkUserExists(userId2);
        userRepository.removeFriend(userId1, userId2);
    }

    private void checkUserExists(Long userId) {
        Optional.ofNullable(userRepository.getUserById(userId))
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}

