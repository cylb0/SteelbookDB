package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;

@Service
public class LanguageMapper {

    public LanguageDto toDto(Language language) {
        if (language == null) return null;

        return new LanguageDto(
            language.getId(),
            language.getCode(),
            language.getName()
        );
    }

    public Language toEntity(CreateLanguageDto dto) {
        if (dto == null) return null;

        Language language = new Language();
        language.setCode(dto.code());
        language.setName(dto.name());

        return language;
    }
}
