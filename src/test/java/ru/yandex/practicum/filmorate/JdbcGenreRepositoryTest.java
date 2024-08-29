package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.JdbcGenreRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcGenreRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcGenreRepositoryTest")
public class JdbcGenreRepositoryTest {

    private static final int TEST_GENRE_ID = 1;

    private final JdbcGenreRepository genreRepository;

    private static Genre getTestGenre() {
        return new Genre(TEST_GENRE_ID, "Action");
    }

    @Test
    @DisplayName("should get all genres")
    public void shouldGetAllGenres() {
        Collection<Genre> genres = genreRepository.getAllGenres();

        assertThat(genres).isNotEmpty();
        assertThat(genres.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should get genre by id")
    public void shouldGetGenreById() {
        Genre genre = genreRepository.getGenreById(TEST_GENRE_ID);

        assertThat(genre).isNotNull();
        assertThat(genre).usingRecursiveComparison().isEqualTo(getTestGenre());
    }
}
