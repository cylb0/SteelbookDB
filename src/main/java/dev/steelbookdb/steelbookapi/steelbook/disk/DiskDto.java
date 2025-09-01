package dev.steelbookdb.steelbookapi.steelbook.disk;

import java.util.Set;

import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.steelbook.audiotrack.AudioTrackDto;

public record DiskDto(
    Long id,
    String format,
    String region,
    boolean isBonusDisk,
    MovieDto movie,
    Set<AudioTrackDto> audioTracks,
    Set<LanguageDto> subtitleLanguages
) {

}
