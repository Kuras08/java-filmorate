package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Qualifier("jdbcMpaRepository")
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;

    private final RowMapper<Mpa> mpaRowMapper = (rs, rowNum) -> {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    };

    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbc.query(sql, mpaRowMapper);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.queryForObject(sql, params, mpaRowMapper);
    }

    @Override
    public Set<Integer> getAllMpaIds() {
        String sql = "SELECT mpa_id FROM mpa";
        List<Integer> ids = jdbc.query(sql, (rs, rowNum) -> rs.getInt("mpa_id"));
        return new HashSet<>(ids);
    }
}

