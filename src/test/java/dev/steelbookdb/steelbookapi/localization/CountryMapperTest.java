package dev.steelbookdb.steelbookapi.localization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;

class CountryMapperTest {

    private final CountryMapper countryMapper = new CountryMapper();

    @Test
    void testTdo_CorrectlyMaps_GivenCountryEntity() {
        Country country = new Country();
        country.setId(1L);
        country.setName("France");

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
}
