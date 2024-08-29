package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreRepository {
    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    boolean existsById(Integer id);
}
