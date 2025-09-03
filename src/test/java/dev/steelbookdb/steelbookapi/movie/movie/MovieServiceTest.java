package dev.steelbookdb.steelbookapi.movie.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import dev.steelbookdb.steelbookapi.exception.BadRequestException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageRepository;
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorRepository;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.genre.GenreMapper;
import dev.steelbookdb.steelbookapi.movie.genre.GenreRepository;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.CreateMovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.UpdateMovieDto;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private DirectorRepository directorRepository;
    @Mock
    private GenreMapper genreMapper;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;
    
    @Test
    void createMovie_returnsNull_givenNullDto() {
        MovieDto result = movieService.createMovie(null);

        assertNull(result);
    }

    @Test
    void createMovie_throwsResourceNotFoundException_givenNonExistentDirectorId() {
        Long nonExistentDirectorId = 999L;

        CreateMovieDto dto = CreateMovieDto.builder()
            .title("Inception")
            .releaseYear(2010)
            .runtime(148)
            .posterUrl("http://example.com/inception.jpg")
            .directorId(nonExistentDirectorId)
            .originalLanguageId(1L)
            .genreIds(Set.of(1L))
            .build();

        when(directorRepository.findById(nonExistentDirectorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(dto);
        });
    }

    @Test
    void createMovie_throwsResourceNotFoundException_givenNonExistentGenreId() {
        Long existingDirectorId = 1L;
        Long nonExistentGenreId = 999L;

        CreateMovieDto dto = CreateMovieDto.builder()
            .title("Inception")
            .releaseYear(2010)
            .runtime(148)
            .posterUrl("http://example.com/inception.jpg")
            .directorId(existingDirectorId)
            .originalLanguageId(1L)
            .genreIds(Set.of(nonExistentGenreId))
            .build();

        when(directorRepository.findById(dto.directorId())).thenReturn(Optional.of(new Director()));

        when(genreRepository.findById(nonExistentGenreId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(dto);
        });
    }

    @Test
    void createMovie_throwsResourceNotFoundException_givenNonExistentLanguageId() {
        Long existingDirectorId = 1L;
        Long nonExistentLanguageId = 1L;
        Long existingGenreId = 999L;

        CreateMovieDto dto = CreateMovieDto.builder()
            .title("Inception")
            .releaseYear(2010)
            .runtime(148)
            .posterUrl("http://example.com/inception.jpg")
            .directorId(existingDirectorId)
            .originalLanguageId(nonExistentLanguageId)
            .genreIds(Set.of(existingGenreId))
            .build();

        when(directorRepository.findById(dto.directorId())).thenReturn(Optional.of(new Director()));
        when(genreRepository.findById(anyLong())).thenReturn(Optional.of(new Genre()));
        when(languageRepository.findById(nonExistentLanguageId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(dto);
        });
    }

    @Test
    void createMovie_shouldSuccessfullySaveMovie_givenValidDto() {
        Long directorId = 1L;
        Long originalLanguageId = 1L;
        Set<Long> genreIds = Set.of(1L, 2L);

        CreateMovieDto dto = CreateMovieDto.builder()
            .title("Inception")
            .releaseYear(2010)
            .runtime(148)
            .posterUrl("http://example.com/inception.jpg")
            .directorId(directorId)
            .originalLanguageId(originalLanguageId)
            .genreIds(genreIds)
            .build();

        Director director = Director.builder().id(directorId).build();
        Language originalLanguage = Language.builder().id(originalLanguageId).build();
        Genre genre1 = Genre.builder().id(1L).build();
        Genre genre2 = Genre.builder().id(2L).build();
        Set<Genre> genres = Set.of(genre1, genre2);

        MovieDto expectedDto = MovieDto.builder().build();

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(director));
        when(languageRepository.findById(originalLanguageId)).thenReturn(Optional.of(originalLanguage));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre1));
        when(genreRepository.findById(2L)).thenReturn(Optional.of(genre2));
        
        when(movieMapper.toEntity(any(CreateMovieDto.class), any(Director.class), any(Set.class), any(Language.class)))
            .thenReturn(new Movie());
        when(movieRepository.save(any(Movie.class))).thenReturn(new Movie());
        when(movieMapper.toDto(any(Movie.class))).thenReturn(expectedDto);

        MovieDto result = movieService.createMovie(dto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(movieMapper, times(1)).toDto(any(Movie.class));
    }

    @Test
    void getAllMovies_shouldReturnAllMoviesList_whenMoviesExist() {
        List<Movie> movies = List.of(new Movie(), new Movie());
        when(movieRepository.findAll()).thenReturn(movies);
        when(movieMapper.toDto(any(Movie.class))).thenReturn(MovieDto.builder().build());

        List<MovieDto> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(movies.size(), result.size());
        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(movies.size())).toDto(any(Movie.class));
    }

    @Test
    void getAllMovies_returnsEmptyList_whenNoMoviesExist() {
        when(movieRepository.findAll()).thenReturn(List.of());

        List<MovieDto> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(0)).toDto(any(Movie.class));
    }

    @Test
    void getMovieById_returnsMovieDto_whenMovieExists() {
        Long movieId = 1L;
        Movie movie = Movie.builder().id(movieId).build();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieMapper.toDto(movie)).thenReturn(MovieDto.builder().build());

        MovieDto result = movieService.getMovieById(movieId);

        assertNotNull(result);
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieMapper, times(1)).toDto(movie);
    }

    @Test
    void getMovieById_throwsResourceNotFoundException_whenMovieDoesNotExist() {
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.getMovieById(movieId);
        });
    }

    @Test
    void deleteMovie_deletesMovie_whenMovieExists() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(true);

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }

    @Test
    void deleteMovie_throwsResourceNotFoundException_whenMovieDoesNotExist() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.deleteMovie(movieId);
        });
    }
    
    @Test
    void updateMovie_doesNothing_whenNoChangesRequested() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
    }

    @Test
    void updateMovie_changesTitle_whenValid() {
        Long movieId = 1L;
        String oldTitle = "Old Title";
        String newTitle = "New Title";

        Movie existingMovie = Movie.builder().title(oldTitle).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.of(newTitle))
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle(newTitle)).thenReturn(false);
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().title(newTitle).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newTitle, result.title());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).existsByTitle(newTitle);
        verify(movieRepository, times(1)).save(existingMovie);
        verify(movieMapper, times(1)).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenTitleIsBlank() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.of(""))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsDuplicateEntryException_whenTitleAlreadyExists() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        String newTitle = "Existing Title";

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.of(newTitle))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitle(newTitle)).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).existsByTitle(newTitle);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesReleaseYear_whenValid() {
        Long movieId = 1L;
        Integer oldReleaseYear = 2020;
        Integer newReleaseYear = 2021;

        Movie existingMovie = Movie.builder().releaseYear(oldReleaseYear).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.of(newReleaseYear))
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().releaseYear(newReleaseYear).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newReleaseYear, result.releaseYear());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
        verify(movieMapper, times(1)).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenReleaseYearIsNull() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.of(null))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenReleaseYearIsInvalid() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        Integer newReleaseYear = -1;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.of(newReleaseYear))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesRuntime_whenValid() {
        Long movieId = 1L;
        Integer oldRunTime = 120;
        Integer newRunTime = 150;

        Movie existingMovie = Movie.builder().runtime(oldRunTime).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.of(newRunTime))
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().runtime(newRunTime).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newRunTime, result.runtime());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
        verify(movieMapper, times(1)).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenRuntimeIsNull() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.of(null))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenRuntimeIsInvalid() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        Integer newRuntime = -1;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.of(newRuntime))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesPosterUrl_whenValid() {
        Long movieId = 1L;
        String oldPosterUrl = "http://old-url.com/poster.jpg";
        String newPosterUrl = "http://new-url.com/poster.jpg";

        Movie existingMovie = Movie.builder().posterUrl(oldPosterUrl).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.of(newPosterUrl))
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().posterUrl(newPosterUrl).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newPosterUrl, result.posterUrl());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
        verify(movieMapper, times(1)).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenPosterUrlIsBlank() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.of(" "))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesPosterUrl_givenNull() {
        Long movieId = 1L;
        String oldPosterUrl = "http://old-url.com/poster.jpg";

        Movie existingMovie = Movie.builder().posterUrl(oldPosterUrl).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.of(null))
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().posterUrl(null).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(null, result.posterUrl());
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, times(1)).save(existingMovie);
        verify(movieMapper, times(1)).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesDirectorId_whenValid() {
        Long movieId = 1L;
        Long oldDirectorId = 1L;
        Long newDirectorId = 2L;

        Director oldDirector = Director.builder().id(oldDirectorId).build();
        Director newDirector = Director.builder().id(newDirectorId).build();

        DirectorDto expectedDirectorDto = DirectorDto.builder().id(newDirectorId).build();

        Movie existingMovie = Movie.builder().director(oldDirector).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.of(newDirectorId))
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(directorRepository.findById(newDirectorId)).thenReturn(Optional.of(newDirector));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().director(expectedDirectorDto).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newDirectorId, result.director().id());
        verify(directorRepository, times(1)).findById(newDirectorId);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenDirectorIdIsNull() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.of(null))
            .languageId(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenDirectorIdIsInvalid() {
        Long movieId = 1L;
        Long newDirectorId = -1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.of(newDirectorId))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesLanguageId_whenValid() {
        Long movieId = 1L;
        Long oldLanguageId = 1L;
        Long newLanguageId = 2L;

        Language oldLanguage = Language.builder().id(oldLanguageId).build();
        Language newLanguage = Language.builder().id(newLanguageId).build();

        LanguageDto expectedLanguageDto = LanguageDto.builder().id(newLanguageId).build();

        Movie existingMovie = Movie.builder().originalLanguage(oldLanguage).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.of(newLanguageId))
            .genreIds(JsonNullable.undefined())
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(languageRepository.findById(newLanguageId)).thenReturn(Optional.of(newLanguage));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);
        when(movieMapper.toDto(existingMovie)).thenReturn(MovieDto.builder().originalLanguage(expectedLanguageDto).build());

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(newLanguageId, result.originalLanguage().id());
        verify(languageRepository, times(1)).findById(newLanguageId);
        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenLanguageIdIsNull() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.of(null))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenLanguageIdIsInvalid() {
        Long movieId = 1L;
        Long newLanguageId = -1L;
        Movie existingMovie = new Movie();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.of(newLanguageId))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(existingMovie);
        verify(movieMapper, never()).toDto(existingMovie);
    }

    @Test
    void updateMovie_changesGenres_whenGenreIdsAreValid() {
        Long movieId = 1L;
        Long existingGenreId = 1L;
        Long newGenreId = 2L;

        Genre existingGenre = Genre.builder().id(existingGenreId).build();
        GenreDto existingGenreDto = GenreDto.builder().id(existingGenreId).build();
        Genre newGenre = Genre.builder().id(newGenreId).build();
        GenreDto newGenreDto = GenreDto.builder().id(newGenreId).build();
        Movie existingMovie = Movie.builder().genres(Set.of(existingGenre)).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(Set.of(existingGenreId, newGenreId)))
            .build();

        MovieDto expectedMovieDto = MovieDto.builder().genres(Set.of(existingGenreDto, newGenreDto)).build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        when(genreRepository.findById(existingGenreId)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.findById(newGenreId)).thenReturn(Optional.of(newGenre));

        when(movieRepository.save(any(Movie.class))).thenReturn(existingMovie);
        when(movieMapper.toDto(any(Movie.class))).thenReturn(expectedMovieDto);

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        assertEquals(2, existingMovie.getGenres().size());
        assertEquals(Set.of(existingGenreId, newGenreId),
            result.genres().stream().map(GenreDto::id).collect(Collectors.toSet()));

        verify(movieRepository, times(1)).save(existingMovie);
    }

    @Test
    void updateMovie_throwsBadRequestException_whenGenreIdsIsNull() {
        Long movieId = 1L;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(null))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    void updateMovie_throwsBadRequestException_whenGenreIdsIsEmpty() {
        Long movieId = 1L;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(Set.of()))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    void updateMovie_throwsBadRequestException_whenAGenreIsNull() {
        Long movieId = 1L;

        Set<Long> genreIdsWithNull = new HashSet<>();
        genreIdsWithNull.add(1L);
        genreIdsWithNull.add(null);

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(genreIdsWithNull))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    void updateMovie_throwsBadRequestException_whenAGenreIsInvalid() {
        Long movieId = 1L;
        Long invalidGenreId = -99L;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(Set.of(1L, invalidGenreId)))
            .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(new Movie()));

        assertThrows(BadRequestException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    void updateMovie_throwsResourceNotFoundException_whenAGenreDoesNotExist() {
        Long movieId = 1L;
        Long nonExistentGenreId = 999L;

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.of(Set.of(nonExistentGenreId)))
            .build();

        Movie existingMovie = Movie.builder().genres(Set.of()).build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(genreRepository.findById(nonExistentGenreId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.updateMovie(movieId, dto);
        });

        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
        verify(movieMapper, never()).toDto(any(Movie.class));
    }

    @Test
    void updateMovie_doesNothing_whenGenreIdsIsOmitted() {
        Long movieId = 1L;

        Movie existingMovie = Movie.builder().genres(Set.of()).build();

        UpdateMovieDto dto = UpdateMovieDto.builder()
            .title(JsonNullable.undefined())
            .releaseYear(JsonNullable.undefined())
            .runtime(JsonNullable.undefined())
            .posterUrl(JsonNullable.undefined())
            .directorId(JsonNullable.undefined())
            .languageId(JsonNullable.undefined())
            .genreIds(JsonNullable.undefined())
            .build();

        MovieDto expectedDto = MovieDto.builder().genres(Set.of()).build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieMapper.toDto(existingMovie)).thenReturn(expectedDto);

        MovieDto result = movieService.updateMovie(movieId, dto);

        assertNotNull(result);
        verify(movieRepository, times(1)).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }
}