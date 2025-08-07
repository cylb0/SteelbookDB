package dev.steelbookdb.steelbookapi.movie;

import org.springframework.stereotype.Service;

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
}
