package dev.steelbookdb.steelbookapi.movie.director;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DirectorMapper {

    private final CountryMapper countryMapper;

    public DirectorDto toDto(Director director) {
        if (director == null) return null;

        return new DirectorDto(
            director.getId(),
            director.getName(),
            countryMapper.toDto(director.getCountry())
        );
    }

    public Director toEntity(CreateDirectorDto dto, Country country) {
        if (dto == null) return null;

        return Director.builder()
            .name(dto.name())
            .country(country)
            .build();
    }
}
