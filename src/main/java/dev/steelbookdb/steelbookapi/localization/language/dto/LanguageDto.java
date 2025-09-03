package dev.steelbookdb.steelbookapi.localization.language.dto;

import lombok.Builder;

@Builder
public record LanguageDto(
    Long id,
    String code,
    String name
) {

}
