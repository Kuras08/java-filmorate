package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreRepository {
    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Set<Integer> getAllGenreIds();
}
