package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;

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

        return Language.builder()
            .code(dto.code())
            .name(dto.name())
            .build();
    }
}
