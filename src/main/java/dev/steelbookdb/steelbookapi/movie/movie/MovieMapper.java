package dev.steelbookdb.steelbookapi.movie.movie;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.language.LanguageMapper;
import dev.steelbookdb.steelbookapi.movie.director.DirectorMapper;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieMapper {

    private final DirectorMapper directorMapper;
    private final LanguageMapper languageMapper;
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
            languageMapper.toDto(movie.getOriginalLanguage()),
            movie.getGenres() != null ? movie.getGenres()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet(),
            movie.getMovieTranslations() != null ?movie.getMovieTranslations()
                .stream()
                .map(movieTranslationMapper::toDto)
                .toList() : java.util.Collections.emptyList()
        );

    }
}
