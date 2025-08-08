package dev.steelbookdb.steelbookapi.localization.country;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.ConflictException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.CreateCountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.UpdateCountryDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryMapper countryMapper;
    private final CountryRepository countryRepository;

    public CountryDto getCountryByID(Long id) {
        return countryRepository.findById(id)
                .map(countryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + id));
    }

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll()
            .stream()
            .map(countryMapper::toDto)
            .toList();
    }
    
    public CountryDto createCountry(CreateCountryDto dto) {
        Country country = countryMapper.toEntity(dto);
        try {
            Country savedCountry = countryRepository.save(country);
            return countryMapper.toDto(savedCountry);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Country with name '" + dto.name() + "' already exists.");
        }
    }

    public CountryDto updateCountry(Long id, UpdateCountryDto dto) {
        Country existingCountry = countryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + id));

        dto.name().ifPresent(newName -> {
            if (newName.isBlank()) {
                throw new ConflictException("Country name cannot be blank.");
            }

            if (!existingCountry.getName().equals(newName) &&
                countryRepository.findByName(newName).isPresent()) {
                throw new ConflictException("Country with name '" + newName + "' already exists.");
            }

            existingCountry.setName(newName);
        });

        Country updatedCountry = countryRepository.save(existingCountry);
        return countryMapper.toDto(updatedCountry);
    }

    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Country not found with ID: " + id);
        }
        countryRepository.deleteById(id);
    }
}
