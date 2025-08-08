package dev.steelbookdb.steelbookapi.steelbook.editor;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditorMapper {

    private final CountryMapper countryMapper;

    public EditorDto toDto(Editor editor) {
        if (editor == null) return null;
        
        return new EditorDto(
            editor.getId(),
            editor.getName(),
            editor.getWebsite(),
            countryMapper.toDto(editor.getCountry())
        );
    }
}
