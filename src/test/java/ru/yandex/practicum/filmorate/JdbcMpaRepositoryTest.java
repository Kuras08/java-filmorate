package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.JdbcMpaRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcMpaRepository.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcMpaRepositoryTest")
public class JdbcMpaRepositoryTest {

    private static final int TEST_MPA_ID = 1;

    private final JdbcMpaRepository mpaRepository;

    private static Mpa getTestMpa() {
        return new Mpa(TEST_MPA_ID, "G");
    }

    @Test
    @DisplayName("should get all MPA ratings")
    public void shouldGetAllMpa() {
        Collection<Mpa> mpa = mpaRepository.getAllMpa();

        assertThat(mpa).isNotEmpty();
        assertThat(mpa.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should get MPA by id")
    public void shouldGetMpaById() {
        Mpa mpa = mpaRepository.getMpaById(TEST_MPA_ID);

        assertThat(mpa).isNotNull();
        assertThat(mpa).usingRecursiveComparison().isEqualTo(getTestMpa());
    }
}

