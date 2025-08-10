package dev.steelbookdb.steelbookapi.localization.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.ConflictException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
import dev.steelbookdb.steelbookapi.localization.language.dto.UpdateLanguageDto;

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
        Language language = Language.builder()
            .id(1L)
            .code(createLanguageDto.code())
            .name(createLanguageDto.name())
            .build();

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

    @Test
    void getAllLanguages_returnsListOfLanguageDto_whenLanguagesExist() {
        Language english = Language.builder().id(1L).code("en").name("English").build();
        Language french = Language.builder().id(2L).code("fr").name("French").build();
        List<Language> languages = List.of(english, french);

        LanguageDto englishDto = new LanguageDto(english.getId(), english.getCode(), english.getName());
        LanguageDto frenchDto = new LanguageDto(french.getId(), french.getCode(), french.getName());
        List<LanguageDto> expectedDtos = List.of(englishDto, frenchDto);

        when(languageRepository.findAll()).thenReturn(languages);
        when(languageMapper.toDto(languages.get(0))).thenReturn(englishDto);
        when(languageMapper.toDto(languages.get(1))).thenReturn(frenchDto);

        List<LanguageDto> result = languageService.getAllLanguages();

        assertNotNull(result);
        assertEquals(result, expectedDtos);
        assertEquals(2, result.size());
        assertEquals(2, result.size());
        verify(languageRepository, times(1)).findAll();
        verify(languageMapper, times(2)).toDto(any(Language.class));
    }

    @Test
    void getAllLanguages_returnsEmptyList_whenNoLanguagesExist() {
        when(languageRepository.findAll()).thenReturn(Collections.emptyList());

        List<LanguageDto> result = languageService.getAllLanguages();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(languageRepository, times(1)).findAll();
        verifyNoInteractions(languageMapper);
    }

    @Test
    void getLanguageById_returnsLanguageDto_whenLanguageExists() {
        Long languageId = 1L;
        Language language = Language.builder().id(languageId).code("en").name("English").build();
        LanguageDto expectedDto = new LanguageDto(language.getId(), language.getCode(), language.getName());

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
        when(languageMapper.toDto(language)).thenReturn(expectedDto);

        LanguageDto result = languageService.getLanguageById(languageId);

        assertNotNull(result);
        assertEquals(result.id(), expectedDto.id());
        assertEquals(result.code(), expectedDto.code());
        assertEquals(result.name(), expectedDto.name());
        verify(languageRepository, times(1)).findById(languageId);
        verify(languageMapper, times(1)).toDto(language);
    }

    @Test
    void getLanguageById_throwsResourceNotFoundException_whenLanguageDoesNotExist() {
        Long languageId = 1L;

        when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            LanguageDto result = languageService.getLanguageById(languageId);
        });
        
        verify(languageRepository, times(1)).findById(languageId);
        verifyNoInteractions(languageMapper);
    }

    @Test
    void updateLanguage_shouldUpdateCode_whenCodeIsProvided() {
        Long languageId = 1L;
        String newCode = "fr";
        Language existingLanguage = Language.builder().id(languageId).code("f").name("france").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.of(newCode), Optional.empty());
        LanguageDto expectedDto = new LanguageDto(languageId, newCode, existingLanguage.getName());

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageRepository.findByCode(newCode)).thenReturn(Optional.empty());
        when(languageRepository.save(existingLanguage)).thenReturn(Language.builder()
            .id(languageId)
            .code(newCode)
            .name(existingLanguage.getName())
            .build()); 
        when(languageMapper.toDto(any(Language.class))).thenReturn(expectedDto);

        LanguageDto result = languageService.updateLanguage(languageId, updateDto);

        assertNotNull(result);
        assertEquals(result.id(), expectedDto.id());
        assertEquals(result.code(), expectedDto.code());
        assertEquals(result.name(), expectedDto.name());

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, times(1)).findByCode(newCode);
        verify(languageRepository, times(1)).save(existingLanguage);
        verify(languageMapper, times(1)).toDto(existingLanguage);
    }

    @Test
    void updateLanguage_shouldUpdateName_whenNameIsProvided() {
        Long languageId = 1L;
        String newName = "france";
        Language existingLanguage = Language.builder().id(languageId).code("fr").name("franc").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.empty(), Optional.of(newName));
        LanguageDto expectedDto = new LanguageDto(languageId, existingLanguage.getCode(), newName);

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageRepository.findByName(newName)).thenReturn(Optional.empty());
        when(languageRepository.save(existingLanguage)).thenReturn(Language.builder()
            .id(languageId)
            .code(existingLanguage.getCode())
            .name(newName)
            .build()); 
        when(languageMapper.toDto(any(Language.class))).thenReturn(expectedDto);

        LanguageDto result = languageService.updateLanguage(languageId, updateDto);

        assertNotNull(result);
        assertEquals(result.id(), expectedDto.id());
        assertEquals(result.code(), expectedDto.code());
        assertEquals(result.name(), expectedDto.name());

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, times(1)).findByName(newName);
        verify(languageRepository, times(1)).save(existingLanguage);
        verify(languageMapper, times(1)).toDto(existingLanguage);
    }

    @Test
    void updateLanguage_shouldNotUpdateAnything_whenDtoIsEmpty() {
        Long languageId = 1L;
        Language existingLanguage = Language.builder().id(languageId).code("fr").name("france").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.empty(), Optional.empty());

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageMapper.toDto(existingLanguage)).thenReturn(new LanguageDto(languageId, existingLanguage.getCode(), existingLanguage.getName()));

        LanguageDto result = languageService.updateLanguage(languageId, updateDto);

        assertNotNull(result);
        verify(languageRepository, times(1)).findById(languageId);
        verify(languageMapper, times(1)).toDto(existingLanguage);
        verify(languageRepository, never()).save(any(Language.class));
    }

    @Test
    void updateLanguage_throwsResourceNotFoundException_whenLanguageDoesNotExist() {
        Long languageId = 99L;
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.of("fr"), Optional.of("France"));

        when(languageRepository.findById(languageId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            languageService.updateLanguage(languageId, updateDto);
        });
        verify(languageRepository, never()).save(any(Language.class));
    }

    @Test
    void updateLanguage_throwsDuplicateEntryException_whenNewCodeAlreadyExists() {
        Long languageId = 1L;
        String newCode = "fr";
        Language existingLanguage = Language.builder().id(languageId).code("en").name("English").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.of(newCode), Optional.empty());

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageRepository.findByCode(newCode)).thenReturn(Optional.of(Language.builder().id(2L).code(newCode).name("French").build()));

        assertThrows(DuplicateEntryException.class, () -> {
            languageService.updateLanguage(languageId, updateDto);
        });

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, times(1)).findByCode(newCode);
        verify(languageRepository, never()).save(any(Language.class));
    }

    @Test
    void updateLanguage_throwsDuplicateEntryException_whenNewNameAlreadyExists() {
        Long languageId = 1L;
        String newName = "French";
        Language existingLanguage = Language.builder().id(languageId).code("en").name("English").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.empty(), Optional.of(newName));

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));
        when(languageRepository.findByName(newName)).thenReturn(Optional.of(Language.builder().id(2L).code("fr").name(newName).build()));

        assertThrows(DuplicateEntryException.class, () -> {
            languageService.updateLanguage(languageId, updateDto);
        });

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, times(1)).findByName(newName);
        verify(languageRepository, never()).save(any(Language.class));
    }

    @Test
    void updateLanguage_throwsConflictException_whenNewCodeIsBlank() {
        Long languageId = 1L;
        Language existingLanguage = Language.builder().id(languageId).code("fr").name("France").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.of(""), Optional.empty());

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));

        assertThrows(ConflictException.class, () -> {
            languageService.updateLanguage(languageId, updateDto);
        });

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, never()).save(any(Language.class));
    }

    @Test
    void updateLanguage_throwsConflictException_whenNewNameIsBlank() {
        Long languageId = 1L;
        Language existingLanguage = Language.builder().id(languageId).code("fr").name("France").build();
        UpdateLanguageDto updateDto = new UpdateLanguageDto(Optional.empty(), Optional.of(""));

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(existingLanguage));

        assertThrows(ConflictException.class, () -> {
            languageService.updateLanguage(languageId, updateDto);
        });

        verify(languageRepository, times(1)).findById(languageId);
        verify(languageRepository, never()).save(any(Language.class));
    }
}