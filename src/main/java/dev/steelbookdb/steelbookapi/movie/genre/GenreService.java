package dev.steelbookdb.steelbookapi.movie.genre;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    public GenreDto createGenre(CreateGenreDto dto) {
        if (dto == null) return null;

        if (genreRepository.existsByName(dto.name())) {
            throw new DuplicateEntryException("name", dto.name());
        }
        
        Genre genre = genreMapper.toEntity(dto);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }
}
