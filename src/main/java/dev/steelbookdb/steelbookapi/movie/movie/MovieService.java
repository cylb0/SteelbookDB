package dev.steelbookdb.steelbookapi.movie.movie;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageRepository;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorRepository;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.genre.GenreRepository;
import dev.steelbookdb.steelbookapi.movie.movie.dto.CreateMovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public MovieDto createMovie(CreateMovieDto dto) {
        if (dto == null) return null;

        Director director = directorRepository.findById(dto.directorId())
            .orElseThrow(() -> new ResourceNotFoundException(Director.class.getSimpleName(), dto.directorId()));

        Set<Genre> genres = dto.genreIds().stream()
            .map(genreId -> genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException(Genre.class.getSimpleName(), genreId))
            )
            .collect(Collectors.toSet());

        Language originalLanguage = languageRepository.findById(dto.originalLanguageId())
            .orElseThrow(() -> new ResourceNotFoundException(Language.class.getSimpleName(), dto.originalLanguageId()));

        Movie movie = movieMapper.toEntity(dto, director, genres, originalLanguage);
        Movie savedmovie = movieRepository.save(movie);
        return movieMapper.toDto(savedmovie);
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
            .stream()
            .map(movieMapper::toDto)
            .toList();
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Movie.class.getSimpleName(), id));
        return movieMapper.toDto(movie);
    }
}
