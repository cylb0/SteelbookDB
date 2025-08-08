package dev.steelbookdb.steelbookapi.movie.movietranslation;

public record MovieTranslationDto(
    Long id,
    String languageCode,
    String title,
    String summary
) {

}
