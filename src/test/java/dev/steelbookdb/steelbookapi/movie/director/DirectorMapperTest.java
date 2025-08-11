package dev.steelbookdb.steelbookapi.movie.director;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;

@ExtendWith(MockitoExtension.class)
class DirectorMapperTest {

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private DirectorMapper directorMapper;
    
    @Test
    void toDto_mapCorrectly_givenValidDirectorEntity() {
        Director director = new Director();
        director.setId(1L);
        director.setName("John Doe");

        DirectorDto dto = directorMapper.toDto(director);

        assertEquals(1L, dto.id());
        assertEquals("John Doe", dto.name());
    }

    @Test
    void toDto_returnsNull_givenNullDirectorEntity() {
        DirectorDto dto = directorMapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void toEntity_returnsDirectorEntity_givenValidDirectorDto() {
        String directorName = "John Doe";
        Long countryId = 1L;
        CreateDirectorDto dto = new CreateDirectorDto(directorName, countryId);
        Country country = Country.builder().id(countryId).name("USA").build();

        Director result = directorMapper.toEntity(dto, country);

        assertNotNull(result);
        assertEquals(directorName, result.getName());
        assertEquals(country, result.getCountry());
        assertEquals(countryId, result.getCountry().getId());
    }

    @Test
    void toEntity_returnsNull_givenNullDirectorDto() {
        Country country = Country.builder().id(1L).name("USA").build();
        Director result = directorMapper.toEntity(null, country);

        assertNull(result);
    }
}
