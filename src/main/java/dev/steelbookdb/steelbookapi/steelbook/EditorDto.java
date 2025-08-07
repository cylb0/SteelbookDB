package dev.steelbookdb.steelbookapi.steelbook;

import dev.steelbookdb.steelbookapi.localization.CountryDto;

public record EditorDto(
    Long id,
    String name,
    String website,
    CountryDto country
) {

}
