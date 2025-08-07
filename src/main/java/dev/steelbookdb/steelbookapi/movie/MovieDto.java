package dev.steelbookdb.steelbookapi.movie;

import java.util.List;
import java.util.Set;

public record MovieDto(
    Long ig,
    String title,
    int releaseYear,
    int runTime,
    String posterUrl,
    DirectorDto director,
    Set<String> genres,
    List<MovieTranslationDto> translations
) {

}
