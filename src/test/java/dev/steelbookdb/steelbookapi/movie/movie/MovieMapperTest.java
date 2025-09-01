package dev.steelbookdb.steelbookapi.movie.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageMapper;
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorMapper;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.movie.dto.CreateMovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslation;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationMapper;

@ExtendWith(MockitoExtension.class)
class MovieMapperTest {

    @Mock
    private DirectorMapper directorMapper;
    @Mock
    private MovieTranslationMapper movieTranslationMapper;
    @Mock
    private LanguageMapper languageMapper;

    @InjectMocks
    private MovieMapper movieMapper;

    @Test
    void toDto_CorrectlyMaps_GivenMovieEntity() {
        Director director = Director.builder()
            .id(1L)
            .name("John Doe")
            .build();

        MovieTranslation translation = MovieTranslation.builder()
            .id(1L)
            .title("Le film")
            .build();

        Language originalLanguage = Language.builder()
            .id(1L)
            .name("French")
            .build();

        LanguageDto originalLanguageDto = new LanguageDto(
            originalLanguage.getId(),
            originalLanguage.getName(),
            originalLanguage.getCode()
        );

        Genre genre = Genre.builder()
            .id(1L)
            .name("Action")
            .build();

        Movie movie = Movie.builder()
            .id(1L)
            .title("The movie")
            .releaseYear(2025)
            .runtime(120)
            .posterUrl("https://example.com/poster.jpg")
            .director(director)
            .originalLanguage(originalLanguage)
            .genres(Set.of(genre))
            .movieTranslations(Set.of(translation))
            .build();

        when(directorMapper.toDto(director))
            .thenReturn(new DirectorDto(director.getId(), director.getName(), null));
        when(movieTranslationMapper.toDto(translation))
            .thenReturn(new MovieTranslationDto(translation.getId(), "fr", translation.getTitle(), translation.getSummary()));
        when(languageMapper.toDto(originalLanguage))
            .thenReturn(originalLanguageDto);

        MovieDto dto = movieMapper.toDto(movie);

        assertNotNull(dto);
        assertEquals(movie.getId(), dto.id());
        assertEquals(movie.getTitle(), dto.title());
        assertEquals(movie.getReleaseYear(), dto.releaseYear());
        assertEquals(movie.getRuntime(), dto.runtime());
        assertEquals(movie.getPosterUrl(), dto.posterUrl());

        assertNotNull(dto.director());
        assertEquals(director.getId(), dto.director().id());
        assertEquals(director.getName(), dto.director().name());

        assertEquals(1, dto.genres().size());
        assertTrue(dto.genres().contains(genre.getName()));

        assertEquals(1, dto.translations().size());
        assertEquals(translation.getId(), dto.translations().get(0).id());
        assertEquals("fr", dto.translations().get(0).languageCode());

        assertNotNull(dto.originalLanguage());
        assertEquals(originalLanguageDto.id(), dto.originalLanguage().id());
        assertEquals(originalLanguageDto.code(), dto.originalLanguage().code());
        assertEquals(originalLanguageDto.name(), dto.originalLanguage().name());
    }

    @Test
    void toDto_ReturnsNull_GivenNullEntity() {
        MovieDto dto = movieMapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toDto_ReturnsEmptyCollections_GivenNullCollections() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The null collection movie");
        movie.setDirector(new Director());

        MovieDto dto = movieMapper.toDto(movie);

        assertNotNull(dto);
        assertNotNull(dto.genres());
        assert dto.genres().isEmpty();
        assertNotNull(dto.translations());
        assert dto.translations().isEmpty();
    }

    @Test
    void toEntity_CorrectlyMaps_GivenCreateMovieDto() {
        CreateMovieDto dto = new CreateMovieDto(
            "The movie",
            2025,
            120,
            "https://example.com/poster.jpg",
            1L,
            1L,
            Set.of(1L, 2L)
        );

        Director director = Director.builder()
            .id(1L)
            .name("John Doe")
            .build();

        Language originalLanguage = Language.builder()
            .id(1L)
            .name("French")
            .build();

        Set<Genre> genres = Set.of(Genre.builder().id(1L).name("Action").build());

        Movie movie = movieMapper.toEntity(dto, director, genres, originalLanguage);

        assertNotNull(movie);
        assertEquals(dto.title(), movie.getTitle());
        assertEquals(dto.releaseYear(), movie.getReleaseYear());
        assertEquals(dto.runtime(), movie.getRuntime());
        assertEquals(dto.posterUrl(), movie.getPosterUrl());
        assertEquals(director, movie.getDirector());
        assertEquals(originalLanguage, movie.getOriginalLanguage());
        assertEquals(genres, movie.getGenres());
    }
}
