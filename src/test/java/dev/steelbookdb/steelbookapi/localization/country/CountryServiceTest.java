package dev.steelbookdb.steelbookapi.localization.country;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.ConflictException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.CreateCountryDto;
import dev.steelbookdb.steelbookapi.localization.country.dto.UpdateCountryDto;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private CountryService countryService;

    @Test
    void getCountryByID_shouldReturnCountryDto_whenCountryExists() {
        Long id = 1L;
        Country country = new Country();
        CountryDto expectedDto = new CountryDto(id, "Country");

        when(countryRepository.findById(id)).thenReturn(java.util.Optional.of(country));
        when(countryMapper.toDto(country)).thenReturn(expectedDto);

        CountryDto dto = countryService.getCountryByID(id);

        assertNotNull(dto);
        assert expectedDto.equals(dto);
        verify(countryRepository, times(1)).findById(id);
        verify(countryMapper, times(1)).toDto(country);
    }
    
    @Test
    void getCountryByID_shouldThrowResourceNotFoundException_whenCountryDoesNotExist() {
        Long id = 1L;

        when(countryRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            countryService.getCountryByID(id);
        });

        verify(countryRepository, times(1)).findById(id);
        verifyNoInteractions(countryMapper);
    }

    @Test
    void createCountry_returnsDto_WhenGivenValidDto() {
        String countryName = "France";
        CreateCountryDto createDto = new CreateCountryDto(countryName);
        Country entityToSave = new Country(countryName);
        Country savedEntity = new Country(countryName);
        savedEntity.setId(1L);
        CountryDto expectedDto = new CountryDto(1L, countryName);

        when(countryMapper.toEntity(createDto)).thenReturn(entityToSave);
        when(countryRepository.save(entityToSave)).thenReturn(savedEntity);
        when(countryMapper.toDto(savedEntity)).thenReturn(expectedDto);

        CountryDto result = countryService.createCountry(createDto);

        assertNotNull(result);
        assert expectedDto.name().equals(result.name());
        verify(countryMapper, times(1)).toEntity(createDto);
        verify(countryRepository, times(1)).save(entityToSave);
        verify(countryMapper, times(1)).toDto(savedEntity);
    }

    @Test
    void getAllCountries_shouldReturnListOfCountryDtos_whenCountriesExist() {
        Country france = new Country();
        france.setId(1L);
        france.setName("France");
        Country germany = new Country();
        germany.setId(2L);
        germany.setName("Germany");

        List<Country> countries = List.of(france, germany);

        CountryDto franceDto = new CountryDto(france.getId(), france.getName());
        CountryDto germanyDto = new CountryDto(germany.getId(), germany.getName());
        List<CountryDto> expectedDtos = List.of(franceDto, germanyDto);

        when(countryRepository.findAll()).thenReturn(countries);
        when(countryMapper.toDto(countries.get(0))).thenReturn(franceDto);
        when(countryMapper.toDto(countries.get(1))).thenReturn(germanyDto);

        List<CountryDto> result = countryService.getAllCountries();

        assertNotNull(result);
        assert result.size() == 2;
        assert result.equals(expectedDtos);
        verify(countryRepository, times(1)).findAll();
        verify(countryMapper, times(2)).toDto(any(Country.class));
    }

    @Test
    void getAllCountries_shouldReturnEmptyList_whenNoCountriesExist() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        List<CountryDto> result = countryService.getAllCountries();

        assertNotNull(result);
        assert result.isEmpty();
        verify(countryRepository, times(1)).findAll();
        verifyNoInteractions(countryMapper);
    }

    @Test
    void updateCountry_shouldReturnUpdatedDto_whenCountryExists() {
        Long countryId = 1L;
        String newName = "Updated Country";

        Country existingCountry = new Country();
        existingCountry.setId(countryId);
        existingCountry.setName("Old Country");

        Country updatedCountryEntity = new Country();
        updatedCountryEntity.setId(countryId);
        updatedCountryEntity.setName(newName);
        CountryDto expectedDto = new CountryDto(countryId, newName);

        UpdateCountryDto updateDto = new UpdateCountryDto(Optional.of(newName));

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));
        when(countryRepository.findByName(newName)).thenReturn(Optional.empty());
        when(countryRepository.save(existingCountry)).thenReturn(updatedCountryEntity);
        when(countryMapper.toDto(updatedCountryEntity)).thenReturn(expectedDto);

        CountryDto result = countryService.updateCountry(countryId, updateDto);

        assertNotNull(result);
        assert result.name().equals(newName);
        verify(countryRepository, times(1)).findById(countryId);
        verify(countryRepository, times(1)).findByName(newName);
        verify(countryRepository, times(1)).save(existingCountry);
        verify(countryMapper, times(1)).toDto(updatedCountryEntity);
        assert existingCountry.getName().equals(newName);
    }

    @Test
    void updateCountry_shouldThrowResourceNotFoundException_whenCountryDoesNotExist() {
        Long countryId = 1L;
        UpdateCountryDto updateDto = new UpdateCountryDto(Optional.of("Updated Country"));

        when(countryRepository.findById(countryId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            countryService.updateCountry(countryId, updateDto);
        });

        verify(countryRepository, times(1)).findById(countryId);
        verify(countryRepository, never()).save(any());
        verifyNoInteractions(countryMapper);
    }

    @Test
    void updateCountry_shouldThrowConflictException_whenNewNameIsBlank() {
        Long countryId = 1L;
        String newName = "  ";

        Country existingCountry = new Country();
        existingCountry.setId(countryId);
        existingCountry.setName("Old Country");
        UpdateCountryDto updateDto = new UpdateCountryDto(Optional.of(newName));

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));

        assertThrows(ConflictException.class, () -> {
            countryService.updateCountry(countryId, updateDto);
        });

        verify(countryRepository, times(1)).findById(countryId);
        verify(countryRepository, never()).findByName(any());
        verify(countryRepository, never()).save(any());
        verifyNoInteractions(countryMapper);
    }

    @Test
    void updateCountry_shouldThrowConflictException_whenNewNameAlreadyExists() {
        Long countryId = 1L;
        String newName = "France";

        Country existingCountry = new Country();
        existingCountry.setId(countryId);
        existingCountry.setName("USA");
        Country secondExistingCountry = new Country();
        secondExistingCountry.setId(2L);
        secondExistingCountry.setName(newName);
        UpdateCountryDto updateDto = new UpdateCountryDto(Optional.of(newName));

        when(countryRepository.findById(countryId)).thenReturn(Optional.of(existingCountry));
        when(countryRepository.findByName(newName)).thenReturn(Optional.of(secondExistingCountry));
        
        assertThrows(ConflictException.class, () -> {
            countryService.updateCountry(countryId, updateDto);
        });

        verify(countryRepository, times(1)).findById(countryId);
        verify(countryRepository, times(1)).findByName(newName);
        verify(countryRepository, never()).save(any());
        verifyNoInteractions(countryMapper);
    }

    @Test
    void deleteCountry_shouldDeleteCountry_whenCountryExists() {
        Long countryId = 1L;

        when(countryRepository.existsById(countryId)).thenReturn(true);

        countryService.deleteCountry(countryId);

        verify(countryRepository, times(1)).existsById(countryId);
        verify(countryRepository, times(1)).deleteById(countryId);
    }

    @Test
    void deleteCountry_shouldThrowResourceNotFoundException_whenCountryDoesNotExist() {
        Long countryId = 1L;

        when(countryRepository.existsById(countryId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            countryService.deleteCountry(countryId);
        });

        verify(countryRepository, times(1)).existsById(countryId);
        verify(countryRepository, never()).deleteById(any());
    }
}
