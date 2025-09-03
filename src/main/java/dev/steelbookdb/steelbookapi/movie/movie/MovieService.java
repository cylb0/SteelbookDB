package dev.steelbookdb.steelbookapi.movie.movie;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.BadRequestException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageRepository;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorRepository;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.genre.GenreRepository;
import dev.steelbookdb.steelbookapi.movie.movie.dto.CreateMovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.UpdateMovieDto;
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

    public MovieDto updateMovie(Long id, UpdateMovieDto dto) {
        Movie existingMovie = movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Movie.class.getSimpleName(), id));

        boolean titleUpdated = updateTitleIfPresent(dto.title(), existingMovie);
        boolean releaseYearUpdated = updateReleaseYearIfPresent(dto.releaseYear(), existingMovie);
        boolean runtimeUpdated = updateRuntimeIfPresent(dto.runtime(), existingMovie);
        boolean posterUrlUpdated = updatePosterUrlIfPresent(dto.posterUrl(), existingMovie);
        boolean directorUpdated = updateDirectorIfPresent(dto.directorId(), existingMovie);
        boolean languageUpdated = updateLanguageIfPresent(dto.languageId(), existingMovie);
        boolean genreUpdated = updateGenresIfPresent(dto.genreIds(), existingMovie);
        boolean updated = titleUpdated || releaseYearUpdated || runtimeUpdated || posterUrlUpdated || directorUpdated || languageUpdated || genreUpdated;

        if (updated) {
            Movie savedMovie = movieRepository.save(existingMovie);
            return movieMapper.toDto(savedMovie);
        }

        return movieMapper.toDto(existingMovie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException(Movie.class.getSimpleName(), id);
        }
        movieRepository.deleteById(id);
    }

    private boolean updateTitleIfPresent(JsonNullable<String> newTitleWrapper, Movie existingMovie) {
        if (newTitleWrapper.isPresent()) {
            String newTitle = newTitleWrapper.get();
            if (newTitle == null || newTitle.isBlank()) {
                throw new BadRequestException("Movie title must not be blank");
            }
            if (!newTitle.equals(existingMovie.getTitle())) {
                if (movieRepository.existsByTitle(newTitle)) {
                    throw new DuplicateEntryException("title", newTitle);
                }
                existingMovie.setTitle(newTitle);
                return true;
            }
        }
        return false;
    }

    private boolean updateReleaseYearIfPresent(JsonNullable<Integer> newReleaseYearWrapper, Movie existingMovie) {
        if (newReleaseYearWrapper.isPresent()) {
            Integer newReleaseYear = newReleaseYearWrapper.get();
            if (newReleaseYear == null || newReleaseYear <= 0) {
                throw new BadRequestException("Release year must be a positive number");
            }
            if (!newReleaseYear.equals(existingMovie.getReleaseYear())) {
                existingMovie.setReleaseYear(newReleaseYear);
                return true;
            }
        }
        return false;
    }

    private boolean updateRuntimeIfPresent(JsonNullable<Integer> newRuntimeWrapper, Movie existingMovie) {
        if (newRuntimeWrapper.isPresent()) {
            Integer newRuntime = newRuntimeWrapper.get();
            if (newRuntime == null || newRuntime <= 0) {
                throw new BadRequestException("Runtime must be a positive number");
            }
            if (!newRuntime.equals(existingMovie.getRuntime())) {
                existingMovie.setRuntime(newRuntime);
                return true;
            }
        }
        return false;
    }

    private boolean updatePosterUrlIfPresent(JsonNullable<String> newPosterUrlWrapper, Movie existingMovie) {
        if (newPosterUrlWrapper.isPresent()) {
            String newPosterUrl = newPosterUrlWrapper.get();

            if (newPosterUrl != null && newPosterUrl.isBlank()) {
                throw new BadRequestException("Poster URL must not be blank");
            }

            if (!Objects.equals(newPosterUrl, existingMovie.getPosterUrl())) {
                existingMovie.setPosterUrl(newPosterUrl);
                return true;
            }
        }
        return false;
    }

    private boolean updateDirectorIfPresent(JsonNullable<Long> newDirectorWrapper, Movie existingMovie) {
        if (newDirectorWrapper.isPresent()) {
            Long newDirectorId = newDirectorWrapper.get();

            if (newDirectorId == null || newDirectorId <= 0) {
                throw new BadRequestException("Director ID must be a positive number");
            }

            if (!newDirectorId.equals(existingMovie.getDirector().getId())) {
                Director newDirector = directorRepository.findById(newDirectorId)
                    .orElseThrow(() -> new ResourceNotFoundException(Director.class.getSimpleName(), newDirectorId));
                existingMovie.setDirector(newDirector);
                return true;
            }
        }
        return false;
    }

    private boolean updateLanguageIfPresent(JsonNullable<Long> newLanguageMapper, Movie existingMovie) {
        if (newLanguageMapper.isPresent()) {
            Long newLanguageId = newLanguageMapper.get();

            if (newLanguageId == null || newLanguageId <= 0) {
                throw new BadRequestException("Language ID must be a positive number");
            }

            if (!newLanguageId.equals(existingMovie.getOriginalLanguage().getId())) {
                Language newLanguage = languageRepository.findById(newLanguageId)
                    .orElseThrow(() -> new ResourceNotFoundException(Language.class.getSimpleName(), newLanguageId));
                existingMovie.setOriginalLanguage(newLanguage);
                return true;
            }
        }
        return false;
    }

    private boolean updateGenresIfPresent(JsonNullable<Set<Long>> newGenresWrapper, Movie existingMovie) {
        if (newGenresWrapper.isPresent()) {
            Set<Long> newGenreIds = newGenresWrapper.get();

            if (newGenreIds == null || newGenreIds.isEmpty()) {
                throw new BadRequestException("A movie must have at least one genre.");
            }

            for (Long genreId : newGenreIds) {
                if (genreId == null || genreId <= 0) {
                    throw new BadRequestException("Genre IDs must be positive numbers");
                }
            }

            Set<Long> existingGenreIds = existingMovie.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());

            if (!newGenreIds.equals(existingGenreIds)) {
                Set<Genre> newGenres = newGenreIds.stream()
                    .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException(Genre.class.getSimpleName(), genreId))
                    )
                    .collect(Collectors.toSet());
                existingMovie.setGenres(newGenres);
                return true;
            }
        }
        return false;
    }
}
