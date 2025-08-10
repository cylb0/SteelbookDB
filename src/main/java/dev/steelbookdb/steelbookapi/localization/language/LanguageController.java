package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PostMapping("/languages")
    public LanguageDto createLanguage(
        @Valid @RequestBody CreateLanguageDto dto
    ) {
        return languageService.createLanguage(dto);
    }

    @GetMapping("/languages")
    public List<LanguageDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping("/languages/{id}")
    public LanguageDto getLanguageByID(@PathVariable Long id) {
        return languageService.getLanguageById(id);
    }
    
    
}
