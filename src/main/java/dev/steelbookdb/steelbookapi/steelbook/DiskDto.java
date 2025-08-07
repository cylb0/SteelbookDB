package dev.steelbookdb.steelbookapi.steelbook;

import java.util.Set;

import dev.steelbookdb.steelbookapi.localization.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.MovieDto;

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
