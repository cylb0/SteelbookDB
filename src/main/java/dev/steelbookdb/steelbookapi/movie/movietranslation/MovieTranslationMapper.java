package dev.steelbookdb.steelbookapi.movie.movietranslation;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.language.LanguageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieTranslationMapper {

    private final LanguageMapper languageMapper;

    public MovieTranslationDto toDto(MovieTranslation movieTranslation) {
        if (movieTranslation == null) return null;

        return new MovieTranslationDto(
            movieTranslation.getId(),
            languageMapper.toDto(movieTranslation.getLanguage()).code(),
            movieTranslation.getTitle(),
            movieTranslation.getSummary()
        );
    }
}
