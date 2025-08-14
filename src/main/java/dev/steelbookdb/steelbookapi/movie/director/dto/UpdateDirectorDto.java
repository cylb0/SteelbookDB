package dev.steelbookdb.steelbookapi.movie.director.dto;

import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateDirectorDto(
    JsonNullable<String> name,
    JsonNullable<Long> countryId
) {

}

