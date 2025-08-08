package dev.steelbookdb.steelbookapi.localization.country;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.CreateCountryDto;

@Service
public class CountryMapper {

    public CountryDto toDto(Country country) {
        if (country == null) return null;

        return new CountryDto(
            country.getId(),
            country.getName()
        );
    }

    public Country toEntity(CreateCountryDto dto) {
        if (dto == null) return null;

        return new Country(
            dto.name()
        );
    }
}
