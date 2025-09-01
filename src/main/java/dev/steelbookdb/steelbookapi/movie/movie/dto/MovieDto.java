package dev.steelbookdb.steelbookapi.movie.movie.dto;

import java.util.List;
import java.util.Set;

import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.movietranslation.MovieTranslationDto;
import lombok.Builder;

@Builder
public record MovieDto(
    Long id,
    String title,
    int releaseYear,
    int runtime,
    String posterUrl,
    DirectorDto director,
    LanguageDto originalLanguage,
    Set<String> genres,
    List<MovieTranslationDto> translations
) {

}
