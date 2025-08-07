package dev.steelbookdb.steelbookapi.movie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

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
}
