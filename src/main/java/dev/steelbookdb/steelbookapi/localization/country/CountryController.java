package dev.steelbookdb.steelbookapi.localization.country;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.CreateCountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.UpdateCountryDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/countries/{country-id}")
    public CountryDto getCountryById(
        @PathVariable("country-id") Long id
    ) {
        return countryService.getCountryByID(id);
    }

    @GetMapping("/countries")
    public List<CountryDto> getAllCountries() {
        return countryService.getAllCountries();
    }
    
    @PostMapping("/countries")
    public CountryDto saveCountry(
        @Valid @RequestBody CreateCountryDto entity
    ) {
        return countryService.createCountry(entity);
    }

    @PatchMapping("/countries/{country-id}")
    public CountryDto updateCountry(
        @PathVariable("country-id") Long id,
        @Valid @RequestBody UpdateCountryDto dto
    ) {
        return countryService.updateCountry(id, dto);
    }

    @DeleteMapping("/countries/{country-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCountry(
        @PathVariable("country-id") Long id
    ) {
        countryService.deleteCountry(id);
    }

}
