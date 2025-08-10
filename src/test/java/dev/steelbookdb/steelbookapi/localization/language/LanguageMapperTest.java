package dev.steelbookdb.steelbookapi.localization.language;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;

class LanguageMapperTest {

    private final LanguageMapper languageMapper = new LanguageMapper();

    @Test
    void toDto_CorrectlyMaps_GivenLanguageEntity() {
        Language language = Language.builder()
            .id(1L)
            .code("en")
            .name("English")
            .build();

        LanguageDto languageDto = languageMapper.toDto(language);

        assertNotNull(languageDto);
        assert languageDto.id().equals(language.getId());
        assert languageDto.code().equals(language.getCode());
        assert languageDto.name().equals(language.getName());
    }

    @Test
    void toDto_ReturnsNull_GivenNullLanguageEntity() {
        LanguageDto languageDto = languageMapper.toDto(null);
        
        assertNull(languageDto);
    }

    @Test
    void toEntity_CorrectlyMaps_GivenCreateLanguageDto() {
        CreateLanguageDto createLanguageDto = new CreateLanguageDto("fr", "French");

        Language language = languageMapper.toEntity(createLanguageDto);

        assertNotNull(language);
        assert language.getCode().equals(createLanguageDto.code());
        assert language.getName().equals(createLanguageDto.name());
    }

    @Test
    void toEntity_ReturnsNull_GivenNullCreateLanguageDto() {
        Language language = languageMapper.toEntity(null);
        
        assertNull(language);
    }
}
