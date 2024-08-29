package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Repository
@Qualifier("jdbcFilmRepository")
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Film> filmRowMapper = (rs, rowNum) -> {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
        film.setGenres(loadGenres(rs.getLong("film_id")));
        return film;
    };

    @Override
    public Film addFilm(Film film) {
        String insertFilmSql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpaId)";

        MapSqlParameterSource filmParams = new MapSqlParameterSource()
                .addValue("filmId", film.getId())
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(insertFilmSql, filmParams, keyHolder);
        Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);

        String insertFilmGenresSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
        List<MapSqlParameterSource> genreParams = film.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("filmId", filmId)
                        .addValue("genreId", genre.getId())
                )
                .toList();

        jdbc.batchUpdate(insertFilmGenresSql, genreParams.toArray(new SqlParameterSource[0]));

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String updateFilmSql = "UPDATE films SET name = :name, description = :description, release_date = :releaseDate, duration = :duration, mpa_id = :mpaId " +
                "WHERE film_id = :filmId";

        MapSqlParameterSource filmParams = new MapSqlParameterSource()
                .addValue("filmId", film.getId())
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpa().getId());

        jdbc.update(updateFilmSql, filmParams);

        String deleteFilmGenresSql = "DELETE FROM film_genres WHERE film_id = :filmId";
        jdbc.update(deleteFilmGenresSql, new MapSqlParameterSource("filmId", film.getId()));

        String insertFilmGenresSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
        List<MapSqlParameterSource> genreParams = film.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("filmId", film.getId())
                        .addValue("genreId", genre.getId())
                )
                .toList();
        jdbc.batchUpdate(insertFilmGenresSql, genreParams.toArray(new SqlParameterSource[0]));

        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = :filmId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", id);

        return jdbc.queryForObject(sql, params, filmRowMapper);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name AS mpa_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.mpa_id";

        return jdbc.query(sql, filmRowMapper);
    }

    @Override
    public Collection<Film> getPopularMovies(Long count) {
        String sql = """
                SELECT gf.count, f.film_id, f.name, f.description, f.release_date, f.duration,
                       f.mpa_id, m.name AS mpa_name,
                       g.genre_id, g.name AS genre_name
                FROM (
                    SELECT film_id, COUNT(user_id) AS count
                    FROM likes
                    GROUP BY film_id
                    ORDER BY count DESC
                    LIMIT :count
                ) gf
                JOIN films f ON gf.film_id = f.film_id
                LEFT JOIN film_genres fg ON f.film_id = fg.film_id
                LEFT JOIN genres g ON fg.genre_id = g.genre_id
                LEFT JOIN mpa m ON f.mpa_id = m.mpa_id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("count", count);

        List<Film> films = jdbc.query(sql, params, filmRowMapper);

        return new LinkedHashSet<>(films);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "MERGE INTO likes (film_id, user_id) VALUES (:filmId, :userId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);

        jdbc.update(sql, params);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = :filmId AND user_id = :userId";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId)
                .addValue("userId", userId);

        jdbc.update(sql, params);
    }

    private LinkedHashSet<Genre> loadGenres(Long filmId) {
        String sql = "SELECT g.genre_id, g.name FROM genres g " +
                "INNER JOIN film_genres fg ON g.genre_id = fg.genre_id " +
                "WHERE fg.film_id = :filmId ";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmId", filmId);

        return new LinkedHashSet<>(jdbc.query(sql, params, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("name"))
        ));
    }
}

