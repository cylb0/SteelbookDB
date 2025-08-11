package dev.steelbookdb.steelbookapi.movie.director;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;
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
}
