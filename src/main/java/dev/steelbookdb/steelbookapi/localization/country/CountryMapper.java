package dev.steelbookdb.steelbookapi.localization.country;

import org.springframework.stereotype.Service;

@Service
public class CountryMapper {

    public CountryDto toDto(Country country) {
        if (country == null) return null;

        return new CountryDto(
            country.getId(),
            country.getName()
        );
    }
}
