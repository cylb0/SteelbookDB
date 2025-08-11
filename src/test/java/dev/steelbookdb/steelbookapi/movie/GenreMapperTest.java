package dev.steelbookdb.steelbookapi.movie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.movie.genre.Genre;
import dev.steelbookdb.steelbookapi.movie.genre.GenreMapper;
import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;

class GenreMapperTest {

    private final GenreMapper genreMapper = new GenreMapper();

    @Test
    void tDto_CorrectlyMaps_GivenGenreEntity() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Action");

        GenreDto genreDto = genreMapper.toDto(genre);

        assertNotNull(genreDto);
        assert genreDto.id().equals(genre.getId());
        assert genreDto.name().equals(genre.getName());
    }

    @Test
    void toDto_ReturnsNull_WhenGenreIsNull() {
        GenreDto genreDto = genreMapper.toDto(null);
        assertNull(genreDto);
    }

    @Test
    void toEntity_CorrectlyMaps_GivenCreateGenreDto() {
        CreateGenreDto createGenreDto = new CreateGenreDto("Comedy");

        Genre genre = genreMapper.toEntity(createGenreDto);

        assertNotNull(genre);
        assert genre.getName().equals(createGenreDto.name());
    }

    @Test
    void toEntity_ReturnsNull_WhenCreateGenreDtoIsNull() {
        Genre genre = genreMapper.toEntity(null);
        assertNull(genre);
    }
}
