package dev.steelbookdb.steelbookapi.movie.director;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.openapitools.jackson.nullable.JsonNullable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.BadRequestException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryRepository;
import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.UpdateDirectorDto;

@ExtendWith(MockitoExtension.class)
class DirectorServiceTest {

    @Mock
    private CountryRepository countryRepository;
    @Mock
    private DirectorMapper directorMapper;
    @Mock
    private DirectorRepository directorRepository;

    @InjectMocks
    private DirectorService directorService;

    @Test
    void createDirector_shouldReturnDirectorDto_whenValidCreateDirectorDto() {
        CreateDirectorDto createDirectorDto = new CreateDirectorDto("John Doe", 1L);
        Country country = Country.builder().id(1L).name("USA").build();
        Director director = Director.builder().id(1L).name("John Doe").country(country).build();

        DirectorDto expectedDto = new DirectorDto(1L, "John Doe", new CountryDto(1L, "USA"));

        when(countryRepository.findById(createDirectorDto.countryId())).thenReturn(Optional.of(country));
        when(directorMapper.toEntity(createDirectorDto, country)).thenReturn(director);
        when(directorRepository.save(director)).thenReturn(director);
        when(directorMapper.toDto(director)).thenReturn(expectedDto);

        DirectorDto result = directorService.createDirector(createDirectorDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(countryRepository, times(1)).findById(createDirectorDto.countryId());
        verify(directorMapper, times(1)).toEntity(createDirectorDto, country);
        verify(directorRepository, times(1)).save(director);
        verify(directorMapper, times(1)).toDto(director);
    }

    @Test
    void createDirector_shouldReturnNull_whenProvidedNullDto() {
        DirectorDto result = directorService.createDirector(null);

        assertNull(result);
        verifyNoInteractions(countryRepository, directorMapper, directorRepository);
    }

    @Test
    void createDirector_shouldThrowResourceNotFoundException_whenCountryNotFound() {
        CreateDirectorDto createDirectorDto = new CreateDirectorDto("John Doe", 1L);

        when(countryRepository.findById(createDirectorDto.countryId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> directorService.createDirector(createDirectorDto));
        verify(countryRepository, times(1)).findById(createDirectorDto.countryId());
        verifyNoInteractions(directorMapper, directorRepository);
    }

    @Test
    void getAllDirectors_returnsList_whenDirectorsExist() {
        Director director = Director.builder().id(1L).name("John Doe").country(Country.builder().id(1L).name("USA").build()).build();
        DirectorDto directorDto = new DirectorDto(1L, "John Doe", new CountryDto(1L, "USA"));

        when(directorRepository.findAll()).thenReturn(List.of(director));
        when(directorMapper.toDto(director)).thenReturn(directorDto);

        List<DirectorDto> result = directorService.getAllDirectors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(directorDto, result.get(0));
        verify(directorRepository, times(1)).findAll();
        verify(directorMapper, times(1)).toDto(director);
    }

    @Test
    void getAllDirectors_returnsEmptyList_whenNoDirectorsExist() {
        when(directorRepository.findAll()).thenReturn(List.of());

        List<DirectorDto> result = directorService.getAllDirectors();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(directorRepository, times(1)).findAll();
        verifyNoInteractions(directorMapper);
    }

    @Test
    void getDirectorById_returnsDirectorDto_whenDirectorExists() {
        Long directorId = 1L;
        Country country = Country.builder().id(1L).name("USA").build();
        Director director = Director.builder().id(directorId).name("John Doe").country(country).build();
        DirectorDto expectedDto = new DirectorDto(directorId, "John Doe", new CountryDto(1L, "USA"));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(director));
        when(directorMapper.toDto(director)).thenReturn(expectedDto);

        DirectorDto result = directorService.getDirectorById(directorId);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(directorRepository, times(1)).findById(directorId);
        verify(directorMapper, times(1)).toDto(director);
    }

    @Test
    void getDirectorById_throwsResourceNotFoundException_whenDirectorDoesNotExist() {
        Long directorId = 1L;

        when(directorRepository.findById(directorId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> directorService.getDirectorById(directorId));
        verify(directorRepository, times(1)).findById(directorId);
        verifyNoInteractions(directorMapper);
    }

    @Test
    void deleteDirector_removesDirector_whenDirectorExists() {
        Long directorId = 1L;

        when(directorRepository.existsById(directorId)).thenReturn(true);

        directorService.deleteDirector(directorId);

        verify(directorRepository, times(1)).existsById(directorId);
        verify(directorRepository, times(1)).deleteById(directorId);
    }

    @Test
    void deleteDirector_throwsResourceNotFoundException_whenDirectorDoesNotExist() {
        Long directorId = 1L;

        when(directorRepository.existsById(directorId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> directorService.deleteDirector(directorId));
        verify(directorRepository, times(1)).existsById(directorId);
        verify(directorRepository, never()).deleteById(directorId);
    }

    @Test
    void updateDirector_changesName_whenValid() {
        Long directorId = 1L;
        String oldName = "Old name";
        String newName = "New name";

        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name(oldName).country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.of(newName), JsonNullable.undefined());
        Director updatedDirector = Director.builder().id(directorId).name(newName).country(existingCountry).build();
        DirectorDto expectedDto = new DirectorDto(directorId, newName, new CountryDto(1L, "USA"));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));
        when(directorRepository.existsByName(newName)).thenReturn(false);
        when(directorRepository.save(updatedDirector)).thenReturn(updatedDirector);
        when(directorMapper.toDto(updatedDirector)).thenReturn(expectedDto);

        DirectorDto result = directorService.updateDirector(directorId, updateDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, times(1)).existsByName(newName);
        verify(directorRepository, times(1)).save(updatedDirector);
        verify(directorMapper, times(1)).toDto(updatedDirector);
    }

    @Test
    void updateDirector_throwsBadRequestException_whenNameIsBlank() {
        Long directorId = 1L;
        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name("Stanley Kubrick").country(existingCountry).build();

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));

        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.of(" "), JsonNullable.undefined());

        assertThrows(BadRequestException.class, () -> directorService.updateDirector(directorId, updateDto));

        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateDirector_throwsBadRequestException_whenNameIsNull() {
        Long directorId = 1L;
        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name("Stanley Kubrick").country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.of(null), JsonNullable.undefined());

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));

        assertThrows(BadRequestException.class, () -> directorService.updateDirector(directorId, updateDto));
        
        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateDirector_changesCountryId_whenValid() {
        Long directorId = 1L;
        Long newCountryId = 2L;
        String directorName = "John Doe";
        String existingCountryName = "USA";
        String newCountryName = "Canada";

        Country existingCountry = Country.builder().id(1L).name(existingCountryName).build();
        Country newCountry = Country.builder().id(newCountryId).name(newCountryName).build();
        Director existingDirector = Director.builder().id(directorId).name(directorName).country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.undefined(), JsonNullable.of(newCountryId));
        Director updatedDirector = Director.builder().id(directorId).name(directorName).country(newCountry).build();
        DirectorDto expectedDto = new DirectorDto(directorId, directorName, new CountryDto(newCountryId, newCountryName));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));
        when(countryRepository.findById(newCountryId)).thenReturn(Optional.of(newCountry));
        when(directorRepository.save(updatedDirector)).thenReturn(updatedDirector);
        when(directorMapper.toDto(updatedDirector)).thenReturn(expectedDto);

        DirectorDto result = directorService.updateDirector(directorId, updateDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(directorRepository, times(1)).findById(directorId);
        verify(countryRepository, times(1)).findById(newCountryId);
        verify(directorRepository, times(1)).save(updatedDirector);
        verify(directorMapper, times(1)).toDto(updatedDirector);
    }

    @Test
    void updateDirector_throwsBadRequestException_whenCountryIdIsNull() {
        Long directorId = 1L;
        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name("John Doe").country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.undefined(), JsonNullable.of(null));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));

        assertThrows(BadRequestException.class, () -> directorService.updateDirector(directorId, updateDto));

        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateDirector_throwsBadRequestException_whenCountryIdIsNotPositiveNumber() {
        Long directorId = 1L;
        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name("John Doe").country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.undefined(), JsonNullable.of(-1L));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));

        assertThrows(BadRequestException.class, () -> directorService.updateDirector(directorId, updateDto));

        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateDirector_throwsResourceNotFoundException_whenCountryIdDoesNotExist() {
        Long directorId = 1L;
        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Director existingDirector = Director.builder().id(directorId).name("John Doe").country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.undefined(), JsonNullable.of(999L));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));
        when(countryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> directorService.updateDirector(directorId, updateDto));

        verify(directorRepository, times(1)).findById(directorId);
        verify(countryRepository, times(1)).findById(999L);
        verify(directorRepository, never()).save(any());
    }

    @Test
    void updateDirector_updatesAllFields_whenValid() {
        Long directorId = 1L;
        String directorName = "John Doe";
        String newDirectorName = "Jane Doe";

        Country existingCountry = Country.builder().id(1L).name("USA").build();
        Country newCountry = Country.builder().id(2L).name("Canada").build();
        Director existingDirector = Director.builder().id(directorId).name(directorName).country(existingCountry).build();
        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.of(newDirectorName), JsonNullable.of(newCountry.getId()));
        DirectorDto expectedDto = new DirectorDto(directorId, newDirectorName, new CountryDto(newCountry.getId(), newCountry.getName()));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));
        when(countryRepository.findById(newCountry.getId())).thenReturn(Optional.of(newCountry));
        when(directorRepository.save(existingDirector)).thenReturn(existingDirector);
        when(directorMapper.toDto(existingDirector)).thenReturn(expectedDto);

        DirectorDto result = directorService.updateDirector(directorId, updateDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(directorRepository, times(1)).findById(directorId);
        verify(countryRepository, times(1)).findById(2L);
        verify(directorRepository, times(1)).save(existingDirector);
        verify(directorMapper, times(1)).toDto(existingDirector);
    }

    @Test
    void updateDirector_returnsExistingDirector_whenNoFieldsAreUpdated() {
        Long directorId = 1L;
        String directorName = "Stanley Kubrick";
        String countryName = "USA";

        Country existingCountry = Country.builder().id(1L).name(countryName).build();
        Director existingDirector = Director.builder().id(directorId).name(directorName).country(existingCountry).build();

        UpdateDirectorDto updateDto = new UpdateDirectorDto(JsonNullable.undefined(), JsonNullable.undefined());
        DirectorDto expectedDto = new DirectorDto(directorId, directorName, new CountryDto(1L, countryName));

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(existingDirector));
        when(directorMapper.toDto(existingDirector)).thenReturn(expectedDto);

        DirectorDto result = directorService.updateDirector(directorId, updateDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(directorRepository, times(1)).findById(directorId);
        verify(directorRepository, never()).save(any());
        verify(directorMapper, times(1)).toDto(existingDirector);
    }

}
