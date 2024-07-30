package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Film {
    static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Null(groups = CreateGroup.class, message = "ID must be null when creating a new film")
    @NotNull(groups = UpdateGroup.class, message = "ID cannot be null when updating a film")
    Long id;

    @NotBlank(message = "Name cannot be empty")
    String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    String description;

    @NotNull(message = "Release date cannot be null")
    LocalDate releaseDate;

    @Positive(message = "Duration must be a positive number")
    Integer duration;

    @AssertTrue(message = "Release date invalid")
    public boolean isValidReleaseDate() {
        return releaseDate == null || !releaseDate.isBefore(MIN_RELEASE_DATE);
    }
}