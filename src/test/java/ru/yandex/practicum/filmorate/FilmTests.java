package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("FilmTests")
class FilmTests {
    private static final Validator validator;

    static {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.usingContext().getValidator();
        }
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
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Name cannot be empty")));
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
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Description cannot exceed 200 characters")));
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
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Release date invalid")));
    }

    @Test
    @DisplayName("Should fail validation when duration is not positive")
    void shouldFailValidationWhenDurationIsNotPositive() {
        Film film = new Film();
        film.setName("A film");
        film.setDescription("A description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(-10);  // Negative duration

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Duration must be a positive number")));
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
        film.setReleaseDate(LocalDate.of(2000, 1, 1));  // Valid release date
        film.setDuration(120);

        assertEquals(LocalDate.of(2000, 1, 1), film.getReleaseDate());
    }
}

