package dev.steelbookdb.steelbookapi.movie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.DirectorMapper;
import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.movie.Movie;
import dev.steelbookdb.steelbookapi.movie.movie.MovieDto;
import dev.steelbookdb.steelbookapi.movie.movie.MovieMapper;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslation;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationMapper;

@ExtendWith(MockitoExtension.class)
class MovieMapperTest {

    @Mock
    private DirectorMapper directorMapper;
    @Mock
    private MovieTranslationMapper movieTranslationMapper;

    @InjectMocks
    private MovieMapper movieMapper;

    @Test
    void toDto_CorrectlyMaps_GivenMovieEntity() {
        Director director = new Director();
        director.setId(1L);
        director.setName("John Doe");

        MovieTranslation translation = new MovieTranslation();
        translation.setId(1L);
        translation.setTitle("Le film");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Action");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("The movie");
        movie.setReleaseYear(2025);
        movie.setRuntime(120);
        movie.setPosterUrl("https://example.com/poster.jpg");
        movie.setDirector(director);
        movie.setGenres(Set.of(genre));
        movie.setMovieTranslations(Set.of(translation));

        when(directorMapper.toDto(director))
            .thenReturn(new DirectorDto(director.getId(), director.getName(), null));
        when(movieTranslationMapper.toDto(translation))
            .thenReturn(new MovieTranslationDto(translation.getId(), "fr", translation.getTitle(), translation.getSummary()));
        
        MovieDto dto = movieMapper.toDto(movie);

        assertNotNull(dto);
        assert dto.id().equals(movie.getId());
        assert dto.title().equals(movie.getTitle());
        assert dto.releaseYear() == movie.getReleaseYear();
        assert dto.runtime() == movie.getRuntime();
        assert dto.posterUrl().equals(movie.getPosterUrl());

        assert dto.director().id().equals(director.getId());
        assert dto.director().name().equals(director.getName());

        assert dto.genres().size() == 1;
        assert dto.genres().iterator().next().equals(genre.getName());

        assert dto.translations().size() == 1;
        assert dto.translations().get(0).id().equals(translation.getId());
        assert dto.translations().get(0).languageCode().equals("fr");
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
}
