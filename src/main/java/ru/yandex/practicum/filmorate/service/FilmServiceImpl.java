package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        Long id = film.getId();
        checkFilmExists(id);
        return filmRepository.updateFilm(film);
    }

    @Override
    public Film getFilmById(Long id) {
        return checkFilmExists(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularMovies(Long count) {
        return filmRepository.getPopularMovies(count);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmRepository.addLike(filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmRepository.removeLike(filmId, userId);
    }

    private Film checkFilmExists(Long filmId) {
        return Optional.ofNullable(filmRepository.getFilmById(filmId))
                .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
    }

    private void checkUserExists(Long userId) {
        Optional.ofNullable(userRepository.getUserById(userId))
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
    }
}

