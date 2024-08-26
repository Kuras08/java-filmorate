package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;


    @Override
    public Film addFilm(Film film) {
        log.info("Adding film with details: {}", film);
        Film addedFilm = filmRepository.addFilm(film);
        log.info("Added film with id: {}", addedFilm.getId());
        return addedFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        Long id = film.getId();
        log.info("Updating film with id: {}", id);
        checkFilmExists(id);
        Film updatedFilm = filmRepository.updateFilm(film);
        log.info("Updated film with id: {}", id);
        return updatedFilm;
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Fetching film by id: {}", id);
        checkFilmExists(id);
        Film film = filmRepository.getFilmById(id);
        log.info("Returning film with id: {}", id);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Fetching all films");
        Collection<Film> films = filmRepository.getAllFilms();
        log.info("Returning {} films", films.size());
        return films;
    }

    @Override
    public Collection<Film> getPopularMovies(Long count) {
        log.info("Fetching top {} popular films", count);
        Collection<Film> popularFilms = filmRepository.getPopularMovies(count);
        log.info("Returning {} popular films", popularFilms.size());
        return popularFilms;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Adding like from user {} to film {}", userId, filmId);
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmRepository.addLike(filmId, userId);
        log.info("User {} liked film {}", userId, filmId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        log.info("Removing like from user {} to film {}", userId, filmId);
        checkFilmExists(filmId);
        checkUserExists(userId);
        filmRepository.removeLike(filmId, userId);
        log.info("User {} removed like from film {}", userId, filmId);
    }

    private void checkFilmExists(Long filmId) {
        if (filmRepository.existsById(filmId)) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
    }

    private void checkUserExists(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }
}


