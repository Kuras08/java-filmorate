package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Repository
@Qualifier("jdbcUserRepository")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("user_name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    };

    @Override
    public User createUser(User user) {
        String sql = "INSERT INTO users (email, login, user_name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, params, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE users SET email = :email, login = :login, user_name = :name, birthday = :birthday " +
                "WHERE user_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        jdbc.update(sql, params);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        return jdbc.queryForObject(sql, params, userRowMapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users";

        return jdbc.query(sql, userRowMapper);
    }

    @Override
    public Set<User> getAllFriends(Long userId) {
        String sql = "SELECT u.* FROM users u " +
                "JOIN friends f ON u.user_id = f.friend_id " +
                "WHERE f.user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);

        return new HashSet<>(jdbc.query(sql, params, userRowMapper));
    }

    @Override
    public Set<User> getCommonFriends(Long userId1, Long userId2) {
        String sql = """
                SELECT u.user_id, u.email, u.login, u.user_name, u.birthday
                FROM friends f1
                JOIN friends f2 ON f1.friend_id = f2.friend_id
                JOIN users u ON f1.friend_id = u.user_id
                WHERE f1.user_id = :userId1
                  AND f2.user_id = :userId2
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId1", userId1)
                .addValue("userId2", userId2);

        return new HashSet<>(jdbc.query(sql, params, userRowMapper));
    }

    @Override
    public void addFriend(Long userId1, Long userId2) {
        String sql = "MERGE INTO friends (user_id, friend_id) VALUES (:userId1, :userId2)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId1", userId1)
                .addValue("userId2", userId2);

        jdbc.update(sql, params);
    }

    @Override
    public void removeFriend(Long userId1, Long userId2) {
        String sql = "DELETE FROM friends WHERE user_id = :userId1 AND friend_id = :userId2";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId1", userId1)
                .addValue("userId2", userId2);

        jdbc.update(sql, params);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);

        Integer count = jdbc.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

}
