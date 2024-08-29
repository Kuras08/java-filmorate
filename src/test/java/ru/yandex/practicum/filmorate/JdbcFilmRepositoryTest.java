package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.film.JdbcFilmRepository;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcFilmRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepositoryTest")
public class JdbcFilmRepositoryTest {
    private static final long TEST_FILM_ID = 1L;
    private final JdbcFilmRepository filmRepository;

    private static Film getTestFilm() {
        Film film = new Film();
        film.setId(TEST_FILM_ID);
        film.setName("Test Film 1");
        film.setDescription("Description for Test Film 1");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(120);
        film.setMpa(new Mpa(1, "G"));
        film.setGenres(new LinkedHashSet<>(List.of(
                new Genre(1, "Action"),
                new Genre(2, "Drama")
        )));
        return film;
    }

    @Test
    @DisplayName("should return film by id")
    public void shouldReturnFilmById() {
        Film film = filmRepository.getFilmById(TEST_FILM_ID);
        assertThat(film)
                .usingRecursiveComparison()
                .isEqualTo(getTestFilm());
    }

    @Test
    @DisplayName("should add a new film")
    public void shouldAddFilm() {
        Film newFilm = new Film();
        newFilm.setName("New Film");
        newFilm.setDescription("Description for New Film");
        newFilm.setReleaseDate(LocalDate.of(2024, 3, 1));
        newFilm.setDuration(140);
        newFilm.setMpa(new Mpa(2, "R"));
        newFilm.setGenres(new LinkedHashSet<>(List.of(new Genre(3, "Comedy"))));

        Film addedFilm = filmRepository.addFilm(newFilm);

        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isNotNull();
        assertThat(addedFilm)
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    @DisplayName("should update an existing film")
    public void shouldUpdateFilm() {
        Film existingFilm = filmRepository.getFilmById(TEST_FILM_ID);
        existingFilm.setName("Updated Film Name");

        filmRepository.updateFilm(existingFilm);
        Film film = filmRepository.getFilmById(TEST_FILM_ID);

        assertThat(film.getName()).isEqualTo("Updated Film Name");
    }

    @Test
    @DisplayName("should delete film genres when updating film")
    public void shouldDeleteFilmGenresWhenUpdatingFilm() {
        Film film = filmRepository.getFilmById(TEST_FILM_ID);
        film.setGenres(new LinkedHashSet<>());

        filmRepository.updateFilm(film);
        Film updatedFilm = filmRepository.getFilmById(TEST_FILM_ID);

        assertThat(updatedFilm.getGenres()).isEmpty();
    }

    @Test
    @DisplayName("should return all films")
    public void shouldReturnAllFilms() {
        List<Film> films = (List<Film>) filmRepository.getAllFilms();
        assertThat(films).isNotEmpty();
    }

    @Test
    @DisplayName("should return popular films")
    public void shouldReturnPopularFilms() {
        filmRepository.addLike(TEST_FILM_ID, 1L);

        LinkedHashSet<Film> popularFilms = (LinkedHashSet<Film>) filmRepository.getPopularMovies(1L);

        assertThat(popularFilms).isNotEmpty();
        assertThat(popularFilms.getFirst().getId()).isEqualTo(TEST_FILM_ID);
    }

    @Test
    @DisplayName("should add a like to a film")
    public void shouldAddLike() {
        filmRepository.addLike(TEST_FILM_ID, 2L);
        assertThat(filmRepository.getFilmById(TEST_FILM_ID)).isNotNull();
    }

    @Test
    @DisplayName("should remove a like from a film")
    public void shouldRemoveLike() {
        filmRepository.addLike(TEST_FILM_ID, 1L);
        filmRepository.removeLike(TEST_FILM_ID, 1L);

        assertThat(filmRepository.getFilmById(TEST_FILM_ID)).isNotNull();
    }
}
