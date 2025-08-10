package dev.steelbookdb.steelbookapi.localization.language;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.ConflictException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
import dev.steelbookdb.steelbookapi.localization.language.dto.UpdateLanguageDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;

    public LanguageDto createLanguage(CreateLanguageDto dto) {
        if (dto == null) return null;

        if (languageRepository.existsByCode(dto.code())) {
            throw new DuplicateEntryException("code", dto.code());
        }

        if (languageRepository.existsByName(dto.name())) {
            throw new DuplicateEntryException("name", dto.name());
        }

        Language language = languageMapper.toEntity(dto);
        Language savedLanguage =languageRepository.save(language);
        return languageMapper.toDto(savedLanguage);
    }

    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll()
            .stream()
            .map(languageMapper::toDto)
            .toList();
    }

    public LanguageDto getLanguageById(Long id) {
        return languageRepository.findById(id)
            .map(languageMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Language not found with ID: " + id));
    }

    public LanguageDto updateLanguage(Long id, UpdateLanguageDto dto) {
        Language existingLanguage = languageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("language", id));

        final AtomicBoolean isUpdated = new AtomicBoolean(false);
        
        dto.code().ifPresent(newCode -> {
            if (newCode.isBlank()) {
                throw new ConflictException("Code cannot be blank.");
            }
            if (!newCode.equals(existingLanguage.getCode()) && languageRepository.findByCode(newCode).isPresent()) {
                throw new DuplicateEntryException("code", newCode);
            }
            existingLanguage.setCode(newCode);
            isUpdated.set(true);
        });

        dto.name().ifPresent(newName -> {
            if (newName.isBlank()) {
                throw new ConflictException("Name cannot be blank.");
            }
            if (!newName.equals(existingLanguage.getName()) && languageRepository.findByName(newName).isPresent()) {
                throw new DuplicateEntryException("name", newName);
            }
            existingLanguage.setName(newName);
            isUpdated.set(true);
        });

        if (isUpdated.get()) {
            Language updatedLanguage = languageRepository.save(existingLanguage);
            return languageMapper.toDto(updatedLanguage);
        }

        return languageMapper.toDto(existingLanguage);
    }
}
