package dev.steelbookdb.steelbookapi.movie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.Language;
import dev.steelbookdb.steelbookapi.localization.LanguageDto;
import dev.steelbookdb.steelbookapi.localization.LanguageMapper;

@ExtendWith(MockitoExtension.class)
class MovieTranslationMapperTest {

    @Mock
    private LanguageMapper languageMapper;

    @InjectMocks
    private MovieTranslationMapper movieTranslationMapper;

    @Test
    void toDto_CorrectlyMaps_GivenMovieTranslationEntity() {
        Language language = new Language();

        MovieTranslation movieTranslation = new MovieTranslation();
        movieTranslation.setId(1L);
        movieTranslation.setTitle("The Movie");
        movieTranslation.setSummary("A great movie.");
        movieTranslation.setLanguage(language);
        
        when(languageMapper.toDto(language))
            .thenReturn(new LanguageDto(1L, "en", "English"));

        MovieTranslationDto dto = movieTranslationMapper.toDto(movieTranslation);

        assertNotNull(dto);
        assert dto.languageCode().equals("en");
        assert dto.title().equals("The Movie");
        assert dto.summary().equals("A great movie.");
        
    }

    @Test
    void toDto_ReturnsNull_GivenNullMovieTranslation() {
        MovieTranslationMapper mapper = new MovieTranslationMapper(new LanguageMapper());

        assertNull(mapper.toDto(null));
    }
}

