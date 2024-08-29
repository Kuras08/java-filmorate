package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreService {
    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    boolean existsById(Integer id);
}
