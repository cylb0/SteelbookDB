package dev.steelbookdb.steelbookapi.localization;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService {

    private final LanguageMapper languageMapper;
    private final LanguageRepository languageRepository;
}
