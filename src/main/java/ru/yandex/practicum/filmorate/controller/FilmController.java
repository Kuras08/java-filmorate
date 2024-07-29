package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.CreateGroup;
import ru.yandex.practicum.filmorate.validation.UpdateGroup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final Map<Long, Film> storage = new HashMap<>();
    private long idCounter = 0L;

    @PostMapping
    @Validated(CreateGroup.class)
    public Film addFilm(@RequestBody @Valid Film film) {
        log.info("Received request to add film with details: {}", film);

        Long id = getNextId();
        film.setId(id);
        storage.put(id, film);

        log.info("Added film with id: {}", film.getId());
        return film;
    }

    @PutMapping
    @Validated(UpdateGroup.class)
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Received request to update film with id: {}", film.getId());

        Long id = film.getId();
        Film savedFilm = storage.get(id);
        if (savedFilm == null) {
            log.error("Film with id {} not found", id);
            throw new NotFoundException("Film with id " + id + " not found");
        }
        storage.put(id, film);

        log.info("Updated film with id: {}", id);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Received request to get all films");

        Collection<Film> films = storage.values();

        log.info("Returning {} films", films.size());
        return films;
    }

    private long getNextId() {
        return ++idCounter;
    }
}