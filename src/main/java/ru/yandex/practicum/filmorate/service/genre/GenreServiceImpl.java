package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(@Qualifier("jdbcGenreRepository") GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.info("Fetching all genres");
        Collection<Genre> genres = genreRepository.getAllGenres();
        log.info("Fetched {} genres", genres.size());
        return genres;
    }

    @Override
    public Genre getGenreById(Integer id) {
        log.info("Fetching genre with id: {}", id);
        if (!genreRepository.existsById(id)) {
            throw new NotFoundException("Genre with id " + id + " not found");
        }
        return genreRepository.getGenreById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return genreRepository.existsById(id);
    }
}
