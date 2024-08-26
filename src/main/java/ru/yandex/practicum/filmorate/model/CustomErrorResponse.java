package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomErrorResponse {
    private String message;

    public CustomErrorResponse(String message) {
        this.message = message;
    }
}
