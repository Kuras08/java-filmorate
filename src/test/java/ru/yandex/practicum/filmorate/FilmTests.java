package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("FilmTests")
class FilmTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Test
    @DisplayName("Should fail validation when creating a film with non-null ID")
    void shouldFailValidationWhenCreatingWithNonNullId() {
        Film film = new Film();
        film.setId(1L);
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "ID must be null when creating a new film");
    }

    @Test
    @DisplayName("Should fail validation when updating a film with null ID")
    void shouldFailValidationWhenUpdatingWithNullId() {
        Film film = new Film();
        film.setId(null);
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "ID cannot be null when updating a film");
    }

    @Test
    @DisplayName("Should fail validation when name is empty")
    void shouldFailValidationWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Name cannot be empty");
    }

    @Test
    @DisplayName("Should fail validation when description exceeds 200 characters")
    void shouldFailValidationWhenDescriptionExceeds200Characters() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Description cannot exceed 200 characters");
    }

    @Test
    @DisplayName("Should fail validation when release date is before 28 December 1895")
    void shouldFailValidationWhenReleaseDateIsBefore1895() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(1890, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Release date invalid");
    }

    @Test
    @DisplayName("Should fail validation when duration is not positive")
    void shouldFailValidationWhenDurationIsNotPositive() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertValidationMessage(violations, "Duration must be a positive number");
    }

    @Test
    @DisplayName("Should pass validation with valid data")
    void shouldPassValidationWithValidData() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should have a valid release date when set correctly")
    void shouldHaveValidReleaseDateWhenSetCorrectly() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertEquals(LocalDate.of(2000, 1, 1), film.getReleaseDate());
    }

    private void assertValidationMessage(Set<ConstraintViolation<Film>> violations, String message) {
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals(message)));
    }
}

