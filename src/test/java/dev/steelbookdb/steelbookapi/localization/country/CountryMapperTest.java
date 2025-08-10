package dev.steelbookdb.steelbookapi.localization.country;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.CreateCountryDto;

class CountryMapperTest {

    private final CountryMapper countryMapper = new CountryMapper();

    @Test
    void testTdo_CorrectlyMaps_GivenCountryEntity() {
        Country country = Country.builder()
            .id(1L)
            .name("France")
            .build();

        CountryDto countryDto = countryMapper.toDto(country);

        assertNotNull(countryDto);
        assertEquals(country.getId(), countryDto.id());
        assertEquals(country.getName(), countryDto.name());
    }

    @Test
    void testTdo_ReturnsNull_GivenNullEntity() {
        CountryDto countryDto = countryMapper.toDto(null);
        assertNull(countryDto);
    }

    @Test
    void testToEntity_CorrectlyMaps_GivenCreateCountryDto() {
        CreateCountryDto createCountryDto = new CreateCountryDto("France");

        Country country = countryMapper.toEntity(createCountryDto);

        assertNotNull(country);
        assertEquals(createCountryDto.name(), country.getName());
    }

    @Test
    void testToEntity_ReturnsNull_GivenNullDto() {
        Country country = countryMapper.toEntity(null);
        assertNull(country);
    }   
}
