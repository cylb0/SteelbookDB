package dev.steelbookdb.steelbookapi.movie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.director.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.DirectorMapper;

class DirectorMapperTest {

    private final DirectorMapper directorMapper = new DirectorMapper();
    
    @Test
    void toTdo_MapCorrectly_GivenValidDirectorEntity() {
        Director director = new Director();
        director.setId(1L);
        director.setName("John Doe");

        DirectorDto dto = directorMapper.toDto(director);

        assertEquals(1L, dto.id());
        assertEquals("John Doe", dto.name());
    }

    @Test
    void toDto_ReturnsNull_GivenNullDirectorEntity() {
        DirectorDto dto = directorMapper.toDto(null);

        assertNull(dto);
    }
}
