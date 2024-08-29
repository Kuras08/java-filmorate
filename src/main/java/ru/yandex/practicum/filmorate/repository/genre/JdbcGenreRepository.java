package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Qualifier("jdbcGenreRepository")
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Genre> genreRowMapper = (rs, rowNum) -> {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    };

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbc.query(sql, genreRowMapper);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE genre_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.queryForObject(sql, params, genreRowMapper);
    }

    @Override
    public Set<Integer> getAllGenreIds() {
        String sql = "SELECT genre_id FROM genres";
        List<Integer> ids = jdbc.query(sql, (rs, rowNum) -> rs.getInt("genre_id"));
        return new HashSet<>(ids);
    }
}

