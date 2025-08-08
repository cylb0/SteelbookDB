package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
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
}
