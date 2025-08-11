package dev.steelbookdb.steelbookapi.movie.director.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDirectorDto(
    @NotBlank(message = "Director name must not be blank")
    String name,
    @NotNull(message = "Country ID must not be null")
    Long countryId
) {

}
