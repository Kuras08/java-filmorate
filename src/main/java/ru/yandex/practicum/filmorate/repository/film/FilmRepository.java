package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmRepository {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularMovies(Long count);

    void addLike(Long filmId, Long userId);

    void removeLike(Long id, Long userId);

    boolean existsById(Long id);
}
