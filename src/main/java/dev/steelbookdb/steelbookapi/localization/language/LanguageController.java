package dev.steelbookdb.steelbookapi.localization.language;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.localization.language.dto.CreateLanguageDto;
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.localization.language.dto.UpdateLanguageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    
    @PatchMapping("/languages/{id}")
    public LanguageDto updateLanguage(
        @PathVariable Long id,
        @Valid @RequestBody UpdateLanguageDto dto
    ) {
        return languageService.updateLanguage(id, dto);
    }

    @DeleteMapping("/languages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
    }
}
