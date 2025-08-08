package dev.steelbookdb.steelbookapi.localization.country.dto;

import java.util.Optional;

public record UpdateCountryDto(
    Optional<String> name
) {

}
