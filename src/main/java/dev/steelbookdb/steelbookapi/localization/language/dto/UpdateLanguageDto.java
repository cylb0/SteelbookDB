package dev.steelbookdb.steelbookapi.localization.language.dto;

import java.util.Optional;

public record UpdateLanguageDto(
    Optional<String> code,
    Optional<String> name
) {

}
