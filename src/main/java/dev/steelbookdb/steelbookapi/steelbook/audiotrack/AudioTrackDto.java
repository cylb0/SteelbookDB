package dev.steelbookdb.steelbookapi.steelbook.audiotrack;

import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormatDto;
import lombok.Builder;

@Builder
public record AudioTrackDto(
    Long id,
    AudioFormatDto audioFormat,
    LanguageDto language
) {

}
