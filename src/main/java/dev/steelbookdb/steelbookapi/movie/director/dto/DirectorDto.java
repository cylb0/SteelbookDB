package dev.steelbookdb.steelbookapi.movie.director.dto;

import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;

public record DirectorDto(
    Long id,
    String name,
    CountryDto country
) {

}
