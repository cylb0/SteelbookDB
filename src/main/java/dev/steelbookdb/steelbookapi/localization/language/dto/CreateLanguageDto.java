package dev.steelbookdb.steelbookapi.localization.language.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLanguageDto(
    @NotBlank(message = "Code cannot be blank")
    String code,
    @NotBlank(message = "Name cannot be blank")
    String name
) {

}
