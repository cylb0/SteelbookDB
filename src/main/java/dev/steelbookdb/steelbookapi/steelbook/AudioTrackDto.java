package dev.steelbookdb.steelbookapi.steelbook;

import dev.steelbookdb.steelbookapi.localization.LanguageDto;

public record AudioTrackDto(
    Long id,
    AudioFormatDto audioFormat,
    LanguageDto languag
) {

}
