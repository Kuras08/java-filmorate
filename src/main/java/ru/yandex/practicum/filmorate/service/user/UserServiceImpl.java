package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("jdbcUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        log.info("Creating user with details: {}", user);
        User createdUser = userRepository.createUser(user);
        log.info("Created user with id: {}", createdUser.getId());
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user with id: {}", user.getId());
        checkUserExists(user.getId());
        User updatedUser = userRepository.updateUser(user);
        log.info("Updated user with id: {}", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        checkUserExists(id);
        User user = userRepository.getUserById(id);
        log.info("Returning user with id: {}", id);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Fetching all users");
        Collection<User> users = userRepository.getAllUsers();
        log.info("Fetched {} users", users.size());
        return users;
    }

    @Override
    public Set<User> getAllFriends(Long id) {
        log.info("Fetching all friends for user with id: {}", id);
        checkUserExists(id);
        Set<User> friends = userRepository.getAllFriends(id);
        if (friends.isEmpty()) {
            log.info("User with id {} has no friends.", id);
        } else {
            log.info("Fetched {} friends for user with id: {}", friends.size(), id);
        }
        return friends;
    }

    @Override
    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        log.info("Fetching common friends between user with id: {} and user with id: {}", userId1, userId2);
        checkUserExists(userId1);
        checkUserExists(userId2);
        Set<User> commonFriends = userRepository.getCommonFriends(userId1, userId2);
        log.info("Fetched {} common friends between user with id: {} and user with id: {}", commonFriends.size(), userId1, userId2);
        return commonFriends;
    }

    @Override
    public void addFriend(Long userId1, Long userId2) {
        log.info("Adding user with id: {} as friend to user with id: {}", userId2, userId1);
        checkUserExists(userId1);
        checkUserExists(userId2);
        userRepository.addFriend(userId1, userId2);
        log.info("Added user with id: {} as friend to user with id: {}", userId2, userId1);
    }

    @Override
    public void removeFriend(Long userId1, Long userId2) {
        log.info("Removing user with id: {} as friend from user with id: {}", userId2, userId1);
        checkUserExists(userId1);
        checkUserExists(userId2);
        userRepository.removeFriend(userId1, userId2);
        log.info("Removed user with id: {} as friend from user with id: {}", userId2, userId1);
    }

    private void checkUserExists(Long userId) {
        try {
            userRepository.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            log.warn("User with id {} not found", userId);
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }
}



