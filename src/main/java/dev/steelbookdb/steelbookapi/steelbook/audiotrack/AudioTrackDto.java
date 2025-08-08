package dev.steelbookdb.steelbookapi.steelbook.audiotrack;

import dev.steelbookdb.steelbookapi.localization.language.LanguageDto;
import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormatDto;

public record AudioTrackDto(
    Long id,
    AudioFormatDto audioFormat,
    LanguageDto language
) {

}
