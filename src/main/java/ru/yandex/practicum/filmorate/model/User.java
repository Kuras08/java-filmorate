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
public class User {
    @Null(groups = CreateGroup.class, message = "ID must be null when creating a new user")
    @NotNull(groups = UpdateGroup.class, message = "ID cannot be null when updating a user")
    Long id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must contain '@' symbol and be a valid email address")
    String email;

    @NotBlank(message = "Login cannot be empty and cannot contain spaces")
    @Pattern(regexp = "\\S+", message = "Login cannot contain spaces")
    String login;

    String name;

    @PastOrPresent(message = "Birthday cannot be in the future")
    LocalDate birthday;

    public String getDisplayName() {
        return (name != null && !name.isEmpty()) ? name : login;
    }
}
