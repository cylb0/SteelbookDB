package dev.steelbookdb.steelbookapi.localization.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {
    
    @Mock
    private LanguageMapper languageMapper;
    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    @Test
    void createLanguage_returnsLanguageDto_GivenValidCreateLanguageDto() {
        CreateLanguageDto createLanguageDto = new CreateLanguageDto("fr", "French");
        Language language = new Language();
        language.setId(1L);
        language.setCode(createLanguageDto.code());
        language.setName(createLanguageDto.name());

        LanguageDto expectedDto = new LanguageDto(language.getId(), language.getCode(), language.getName());

        when(languageMapper.toEntity(createLanguageDto)).thenReturn(language);
        when(languageRepository.save(language)).thenReturn(language);
        when(languageMapper.toDto(language)).thenReturn(expectedDto);

        LanguageDto result = languageService.createLanguage(createLanguageDto);

        assertNotNull(result);
        assertEquals(result.id(), expectedDto.id());
        assertEquals(result.code(), expectedDto.code());
        assertEquals(result.name(), expectedDto.name());

        verify(languageMapper, times(1)).toEntity(createLanguageDto);
        verify(languageRepository, times(1)).save(language);
        verify(languageMapper, times(1)).toDto(language);
    }

    @Test
    void createLanguage_returnsNull_GivenNullCreateLanguageDto() {
        LanguageDto result = languageService.createLanguage(null);
        assertEquals(null, result);
    }

    @Test
    void createLanguage_throwsDuplicateLanguageCodeException_GivenExistingCode() {
        CreateLanguageDto createLanguageDto = new CreateLanguageDto("en", "English");

        when(languageRepository.existsByCode(createLanguageDto.code())).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> {
            languageService.createLanguage(createLanguageDto);
        });

        verify(languageRepository, times(1)).existsByCode(createLanguageDto.code());
    }

    @Test
    void createLanguage_throwsDuplicateLanguageNameException_GivenExistingName() {
        CreateLanguageDto createLanguageDto = new CreateLanguageDto("en", "English");

        when(languageRepository.existsByName(createLanguageDto.name())).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> {
            languageService.createLanguage(createLanguageDto);
        });

        verify(languageRepository, times(1)).existsByName(createLanguageDto.name());
    }
}
