package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DisplayName("UserTests")
class UserTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    @DisplayName("Should fail validation when email is null")
    void shouldFailValidationWhenEmailIsNull() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Email cannot be empty");
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Email must contain '@' symbol and be a valid email address");
    }

    @Test
    @DisplayName("Should fail validation when login contains spaces")
    void shouldFailValidationWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Login cannot contain spaces");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should use login as display name if name is empty")
    void shouldUseLoginAsDisplayNameIfNameIsEmpty() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setName(null);
        user.setBirthday(LocalDate.now());

        assertEquals("validLogin", user.getDisplayName());
    }

    @Test
    @DisplayName("Should fail validation when birthday is in future")
    void shouldFailValidationWhenBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Birthday cannot be in the future");
    }

    private void assertValidationMessage(Set<ConstraintViolation<User>> violations, String message) {
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(message)));
    }
}

