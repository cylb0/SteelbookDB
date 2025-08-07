package dev.steelbookdb.steelbookapi.movie;

public record MovieTranslationDto(
    Long id,
    String languageCode,
    String title,
    String summary
) {

}
