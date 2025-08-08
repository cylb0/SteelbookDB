package dev.steelbookdb.steelbookapi.steelbook.editor;

import dev.steelbookdb.steelbookapi.localization.country.CountryDto;

public record EditorDto(
    Long id,
    String name,
    String website,
    CountryDto country
) {

}
