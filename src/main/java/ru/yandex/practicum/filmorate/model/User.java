package ru.yandex.practicum.filmorate.model;

import io.micrometer.common.util.StringUtils;
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
    private Long id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must contain '@' symbol and be a valid email address")
    private String email;

    @NotBlank(message = "Login cannot be empty and cannot contain spaces")
    @Pattern(regexp = "\\S+", message = "Login cannot contain spaces")
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday cannot be in the future")
    private LocalDate birthday;

    public String getDisplayName() {
        return StringUtils.isNotBlank(name) ? name : login;
    }
}
