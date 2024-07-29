package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
    private static final Validator validator;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    @DisplayName("Should fail validation when email is null")
    void shouldFailValidationWhenEmailIsNull() {
        User user = new User();
        user.setEmail(null);  // Email is null
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email cannot be empty")));
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");  // Invalid email
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email must contain '@' symbol and be a valid email address")));
    }

    @Test
    @DisplayName("Should fail validation when login contains spaces")
    void shouldFailValidationWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("invalid login");  // Login with spaces
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Login cannot contain spaces")));
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().minusYears(1)); // Past date

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should use login as display name if name is empty")
    void shouldUseLoginAsDisplayNameIfNameIsEmpty() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setName(null);  // No name provided
        user.setBirthday(LocalDate.now().minusYears(1));

        assertEquals("validLogin", user.getDisplayName());
    }

    @Test
    @DisplayName("Should fail validation when birthday is in future")
    void shouldFailValidationWhenBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("valid@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1)); // Future date

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Birthday cannot be in the future")));
    }
}

