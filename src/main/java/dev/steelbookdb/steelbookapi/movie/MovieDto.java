package dev.steelbookdb.steelbookapi.movie;

import java.util.List;
import java.util.Set;

public record MovieDto(
    Long id,
    String title,
    int releaseYear,
    int runtime,
    String posterUrl,
    DirectorDto director,
    Set<String> genres,
    List<MovieTranslationDto> translations
) {

}
