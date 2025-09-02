package dev.steelbookdb.steelbookapi.movie.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageRepository;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorRepository;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.genre.GenreRepository;
import dev.steelbookdb.steelbookapi.movie.movie.dto.CreateMovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private DirectorRepository directorRepository;
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
}