package dev.steelbookdb.steelbookapi.movie.movie;

import java.util.List;
import java.util.Set;

import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationDto;

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
