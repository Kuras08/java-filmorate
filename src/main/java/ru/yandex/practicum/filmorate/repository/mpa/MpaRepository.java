package ru.yandex.practicum.filmorate.repository.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Set;

public interface MpaRepository {
    Collection<Mpa> getAllMpa();

    Mpa getMpaById(Integer id);

    Set<Integer> getAllMpaIds();
}
