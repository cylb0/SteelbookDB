package dev.steelbookdb.steelbookapi.localization.country.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCountryDto(
    @NotBlank(message = "Country name cannot be null")
    String name
) {

}
