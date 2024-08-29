package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;

import java.util.Collection;

@Slf4j
@Service
public class MpaServiceImpl implements MpaService {
    private final MpaRepository mpaRepository;

    @Autowired
    public MpaServiceImpl(@Qualifier("jdbcMpaRepository") MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("Fetching all MPAs");
        Collection<Mpa> mpa = mpaRepository.getAllMpa();
        log.info("Fetched {} MPAs", mpa.size());
        return mpa;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        log.info("Fetching MPA rating with id: {}", id);
        if (!mpaRepository.existsById(id)) {
            throw new NotFoundException("MPA rating with id " + id + " not found");
        }
        return mpaRepository.getMpaById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return mpaRepository.existsById(id);
    }
}
