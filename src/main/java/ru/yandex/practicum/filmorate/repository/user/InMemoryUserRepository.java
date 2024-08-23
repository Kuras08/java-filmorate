package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> friends = new HashMap<>();
    long currentId = 0;


    @Override
    public User createUser(User user) {
        Long id = getIdNext();
        user.setName(user.getDisplayName());
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }


    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Set<User> getAllFriends(Long userId) {
        Set<Long> friendIds = friends.get(userId);
        if (friendIds == null) {
            return Collections.emptySet();
        }

        return friendIds.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        Set<Long> friends1 = friends.getOrDefault(userId1, Collections.emptySet());
        Set<Long> friends2 = friends.getOrDefault(userId2, Collections.emptySet());

        Set<Long> commonFriends = new HashSet<>(friends1);
        commonFriends.retainAll(friends2);

        return commonFriends.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }


    @Override
    public void addFriend(Long userId1, Long userId2) {
        friends.computeIfAbsent(userId1, k -> new HashSet<>()).add(userId2);
        friends.computeIfAbsent(userId2, k -> new HashSet<>()).add(userId1);
    }

    @Override
    public void removeFriend(Long userId1, Long userId2) {
        friends.computeIfAbsent(userId1, k -> new HashSet<>()).remove(userId2);
        friends.computeIfAbsent(userId2, k -> new HashSet<>()).remove(userId1);
    }

    private long getIdNext() {
        return ++currentId;
    }
}
