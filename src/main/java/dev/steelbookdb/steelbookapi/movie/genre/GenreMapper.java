package dev.steelbookdb.steelbookapi.movie.genre;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;

@Service
public class GenreMapper {

    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            return null;
        }

        return new GenreDto(
            genre.getId(),
            genre.getName()
        );
    }

    public Genre toEntity(CreateGenreDto dto) {
        if (dto == null) return null;

        return Genre.builder()
            .name(dto.name())
            .build();
    }
}
