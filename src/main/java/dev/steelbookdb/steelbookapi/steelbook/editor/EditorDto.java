package dev.steelbookdb.steelbookapi.steelbook.editor;

import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import lombok.Builder;

@Builder
public record EditorDto(
    Long id,
    String name,
    String website,
    CountryDto country
) {

}
