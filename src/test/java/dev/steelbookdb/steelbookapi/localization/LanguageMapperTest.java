package dev.steelbookdb.steelbookapi.localization;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class LanguageMapperTest {

    private final LanguageMapper languageMapper = new LanguageMapper();

    @Test
    void testDto_CorrectlyMaps_GivenLanguageEntity() {
        Language language = new Language();
        language.setId(1L);
        language.setCode("en");
        language.setName("English");

        LanguageDto languageDto = languageMapper.toDto(language);

        assertNotNull(languageDto);
        assert languageDto.id().equals(language.getId());
        assert languageDto.code().equals(language.getCode());
        assert languageDto.name().equals(language.getName());
    }

    @Test
    void testDto_ReturnsNull_GivenNullLanguageEntity() {
        LanguageDto languageDto = languageMapper.toDto(null);
        
        assertNull(languageDto);
    }
}
