package ru.yandex.practicum.filmorate.repository.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> movies = new HashMap<>();
    private final Map<Long, Set<Long>> movieLikes = new HashMap<>();
    private long idCounter = 0L;

    @Override
    public Film addFilm(Film film) {
        Long id = getNextId();
        film.setId(id);
        movies.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        movies.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        return movies.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return movies.values();
    }

    @Override
    public Collection<Film> getPopularMovies(Long count) {
        return movies.keySet().stream()
                .map(film -> new AbstractMap.SimpleEntry<>(film,
                        movieLikes.getOrDefault(film, Collections.emptySet()).size()))
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .limit(count)
                .map(entry -> movies.get(entry.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        movieLikes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Set<Long> likes = movieLikes.get(filmId);
        if (likes != null) {
            likes.remove(userId);
            if (likes.isEmpty()) {
                movieLikes.remove(filmId);
            }
        }
    }

    private long getNextId() {
        return ++idCounter;
    }
}
