package dev.steelbookdb.steelbookapi.localization;

import org.springframework.stereotype.Service;

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
}
