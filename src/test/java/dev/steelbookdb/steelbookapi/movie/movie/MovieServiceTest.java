package dev.steelbookdb.steelbookapi.movie.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.foreign.Linker.Option;
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
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorRepository;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
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

        CreateMovieDto dto = new CreateMovieDto(
            "Inception",
            2010,
            148,
            "http://example.com/inception.jpg",
            nonExistentDirectorId,
            1L,
            Set.of(1L)
        );

        when(directorRepository.findById(nonExistentDirectorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(dto);
        });
    }

    @Test
    void createMovie_throwsResourceNotFoundException_givenNonExistentGenreId() {
        Long existingDirectorId = 1L;
        Long nonExistentGenreId = 999L;

        CreateMovieDto dto = new CreateMovieDto(
            "Inception",
            2010,
            148,
            "http://example.com/inception.jpg",
            existingDirectorId,
            1L,
            Set.of(nonExistentGenreId)
        );

        when(directorRepository.findById(dto.directorId())).thenReturn(Optional.of(new Director()));

        when(genreRepository.findById(nonExistentGenreId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(dto);
        });
    }

    @Test
    void createMovie_throwsResourceNotFOundException_givenNonExistentLanguageId() {
        Long existingDirectorId = 1L;
        Long nonExistentLanguageId = 1L;
        Long existingGenreId = 999L;

        CreateMovieDto dto = new CreateMovieDto(
            "Inception",
            2010,
            148,
            "http://example.com/inception.jpg",
            existingDirectorId,
            nonExistentLanguageId,
            Set.of(existingGenreId)
        );

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

        CreateMovieDto dto = new CreateMovieDto(
            "Inception",
            2010,
            148,
            "http://example.com/inception.jpg",
            directorId,
            originalLanguageId,
            genreIds
        );

        Director director = Director.builder().id(directorId).name("Christopher Nolan").build();
        Language originalLanguage = Language.builder().id(originalLanguageId).name("English").code("en").build();
        Genre genre1 = Genre.builder().id(1L).name("Action").build();
        Genre genre2 = Genre.builder().id(2L).name("Sci-Fi").build();
        Set<Genre> genres = Set.of(genre1, genre2);

        Movie movieToSave = Movie.builder()
            .title(dto.title())
            .releaseYear(dto.releaseYear())
            .runtime(dto.runtime())
            .posterUrl(dto.posterUrl())
            .director(director)
            .originalLanguage(originalLanguage)
            .genres(genres)
            .build();
        Movie savedMovie = Movie.builder()
            .id(1L)
            .title(dto.title())
            .releaseYear(dto.releaseYear())
            .runtime(dto.runtime())
            .posterUrl(dto.posterUrl())
            .director(director)
            .originalLanguage(originalLanguage)
            .genres(genres)
            .build();

        DirectorDto directorDto = new DirectorDto(director.getId(), director.getName(), null);
        LanguageDto originalLanguageDto = new LanguageDto(originalLanguage.getId(), originalLanguage.getName(), originalLanguage.getCode());
        Set<String> genreNames = Set.of(genre1.getName(), genre2.getName());

        MovieDto expectedDto = new MovieDto(
            savedMovie.getId(),
            savedMovie.getTitle(),
            savedMovie.getReleaseYear(),
            savedMovie.getRuntime(),
            savedMovie.getPosterUrl(),
            directorDto,
            originalLanguageDto,
            genreNames,
            List.of()
        );
        
        when(directorRepository.findById(directorId)).thenReturn(Optional.of(director));
        when(languageRepository.findById(originalLanguageId)).thenReturn(Optional.of(originalLanguage));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre1));
        when(genreRepository.findById(2L)).thenReturn(Optional.of(genre2));
        when(movieMapper.toEntity(dto, director, genres, originalLanguage)).thenReturn(movieToSave);
        when(movieRepository.save(movieToSave)).thenReturn(savedMovie);
        when(movieMapper.toDto(savedMovie)).thenReturn(expectedDto);

        MovieDto result = movieService.createMovie(dto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(movieRepository, times(1)).save(movieToSave);
        verify(movieMapper, times(1)).toDto(savedMovie);
    }
}