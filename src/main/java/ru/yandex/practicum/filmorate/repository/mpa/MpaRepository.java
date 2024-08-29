package ru.yandex.practicum.filmorate.repository.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaRepository {
    Collection<Mpa> getAllMpa();

    Mpa getMpaById(Integer id);

    boolean existsById(Integer id);
}
