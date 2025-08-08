package dev.steelbookdb.steelbookapi.movie;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieMapper {

    private final DirectorMapper directorMapper;
    private final MovieTranslationMapper movieTranslationMapper;

    public MovieDto toDto(Movie movie) {
        if (movie == null) return null;

        return new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getReleaseYear(),
            movie.getRuntime(),
            movie.getPosterUrl(),
            directorMapper.toDto(movie.getDirector()),
            movie.getGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toSet()),
            movie.getMovieTranslations()
                .stream()
                .map(movieTranslationMapper::toDto)
                .toList()
        );

    }
}
