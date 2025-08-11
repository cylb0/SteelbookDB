package dev.steelbookdb.steelbookapi.movie.director;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryRepository;
import dev.steelbookdb.steelbookapi.localization.country.dto.CountryDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;

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

}
