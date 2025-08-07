package dev.steelbookdb.steelbookapi.localization;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryMapper countryMapper;
    private final CountryService countryService;
}
