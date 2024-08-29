package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcUserRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepositoryTest")
public class JdbcUserRepositoryTest {

    private static final long TEST_USER_ID = 1L;
    private static final long TEST_FRIEND_ID = 2L;
    private static final long COMMON_FRIEND_ID = 3L;

    private final JdbcUserRepository userRepository;

    private static User getTestUser() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail("testUser@example.com");
        user.setLogin("testUser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        return user;
    }

    private static User getTestFriend() {
        User friend = new User();
        friend.setId(TEST_FRIEND_ID);
        friend.setEmail("testFriend@example.com");
        friend.setLogin("testFriend");
        friend.setName("Test Friend");
        friend.setBirthday(LocalDate.of(1990, 1, 1));
        return friend;
    }

    private static User getCommonFriend() {
        User commonFriend = new User();
        commonFriend.setId(COMMON_FRIEND_ID);
        commonFriend.setEmail("commonFriend@example.com");
        commonFriend.setLogin("commonFriend");
        commonFriend.setName("Common Friend");
        commonFriend.setBirthday(LocalDate.of(1985, 1, 1));
        return commonFriend;
    }

    @Test
    @DisplayName("should create a new user")
    public void shouldCreateNewUser() {
        User newUser = new User();
        newUser.setEmail("newUser@example.com");
        newUser.setLogin("newUser");
        newUser.setName("New User");
        newUser.setBirthday(LocalDate.of(1995, 5, 5));

        User createdUser = userRepository.createUser(newUser);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(createdUser.getLogin()).isEqualTo(newUser.getLogin());
        assertThat(createdUser.getName()).isEqualTo(newUser.getName());
        assertThat(createdUser.getBirthday()).isEqualTo(newUser.getBirthday());
    }

    @Test
    @DisplayName("should update an existing user")
    public void shouldUpdateExistingUser() {
        User existingUser = userRepository.getUserById(TEST_USER_ID);
        existingUser.setEmail("updated@example.com");
        existingUser.setLogin("updatedUser");

        userRepository.updateUser(existingUser);
        User user = userRepository.getUserById(TEST_USER_ID);

        assertThat(user.getEmail()).isEqualTo("updated@example.com");
        assertThat(user.getLogin()).isEqualTo("updatedUser");
    }

    @Test
    @DisplayName("should get user by id")
    public void shouldGetUserById() {
        User user = userRepository.getUserById(TEST_USER_ID);

        assertThat(user).isNotNull();
        assertThat(user).usingRecursiveComparison().isEqualTo(getTestUser());
    }

    @Test
    @DisplayName("should return all users")
    public void shouldReturnAllUsers() {
        Collection<User> users = userRepository.getAllUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    @DisplayName("should get all friends of a user")
    public void shouldGetAllFriends() {
        userRepository.addFriend(TEST_USER_ID, COMMON_FRIEND_ID);
        userRepository.addFriend(TEST_FRIEND_ID, COMMON_FRIEND_ID);

        Set<User> friends = userRepository.getAllFriends(TEST_USER_ID);

        assertThat(friends).isNotEmpty();
        assertThat(friends).contains(getCommonFriend());
    }

    @Test
    @DisplayName("should get common friends between two users")
    public void shouldGetCommonFriends() {
        userRepository.addFriend(TEST_USER_ID, COMMON_FRIEND_ID);
        userRepository.addFriend(TEST_FRIEND_ID, COMMON_FRIEND_ID);

        Set<User> commonFriends = userRepository.getCommonFriends(TEST_USER_ID, TEST_FRIEND_ID);

        assertThat(commonFriends).isNotEmpty();
        assertThat(commonFriends).contains(getCommonFriend());
    }

    @Test
    @DisplayName("should add a friend to a user")
    public void shouldAddFriend() {
        User newUser = new User();
        newUser.setEmail("newUser2@example.com");
        newUser.setLogin("newUser2");
        newUser.setName("New User 2");
        newUser.setBirthday(LocalDate.of(1995, 5, 5));
        User addedUser = userRepository.createUser(newUser);

        userRepository.addFriend(TEST_USER_ID, addedUser.getId());

        Set<User> friends = userRepository.getAllFriends(TEST_USER_ID);
        assertThat(friends).contains(addedUser);
    }

    @Test
    @DisplayName("should remove a friend from a user")
    public void shouldRemoveFriend() {
        userRepository.addFriend(TEST_USER_ID, TEST_FRIEND_ID);
        userRepository.removeFriend(TEST_USER_ID, TEST_FRIEND_ID);

        Set<User> friends = userRepository.getAllFriends(TEST_USER_ID);
        assertThat(friends).doesNotContain(getTestFriend());
    }
}


