package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @Validated(CreateGroup.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@RequestBody @Valid Film film) {
        log.info("Received request to add film with details: {}", film);
        filmService.addFilm(film);
        log.info("Added film with id: {}", film.getId());
        return film;
    }

    @PutMapping
    @Validated(UpdateGroup.class)
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Received request to update film with id: {}", film.getId());
        filmService.updateFilm(film);
        log.info("Updated film with id: {}", film.getId());
        return film;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable Long id) {
        log.info("Received request to get film by id: {}", id);
        Film film = filmService.getFilmById(id);
        log.info("Returning film with id: {}", id);
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        log.info("Received request to get all films");
        Collection<Film> films = filmService.getAllFilms();
        log.info("Returning {} films", films.size());
        return films;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Received request to add like from user {} to film {}", userId, id);
        filmService.addLike(id, userId);
        log.info("User {} liked film {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Received request to remove like from user {} to film {}", userId, id);
        filmService.removeLike(id, userId);
        log.info("User {} removed like from film {}", userId, id);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("Received request to get top {} popular films", count);
        Collection<Film> popularFilms = filmService.getPopularMovies(count);
        log.info("Returning {} popular films", popularFilms.size());
        return popularFilms;
    }
}