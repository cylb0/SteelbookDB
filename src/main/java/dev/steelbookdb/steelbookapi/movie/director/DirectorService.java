package dev.steelbookdb.steelbookapi.movie.director;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.BadRequestException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryRepository;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.UpdateDirectorDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private final CountryRepository countryRepository;
    private final DirectorRepository directorRepository;
    private final DirectorMapper directorMapper;

    public DirectorDto createDirector(CreateDirectorDto dto) {
        if (dto == null) return null;

        Country country = countryRepository.findById(dto.countryId())
            .orElseThrow(() -> new ResourceNotFoundException(Country.class.getSimpleName(), dto.countryId()));

        Director director = directorMapper.toEntity(dto, country);
        Director savedDirector = directorRepository.save(director);
        return directorMapper.toDto(savedDirector);
    }

    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAll()
            .stream()
            .map(directorMapper::toDto)
            .toList();
    }

    public DirectorDto getDirectorById(Long id) {
        Director director = directorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Director.class.getSimpleName(), id));
        return directorMapper.toDto(director);
    }

    public DirectorDto updateDirector(Long id, UpdateDirectorDto dto) {
        Director existingDirector = directorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Director.class.getSimpleName(), id));
        
        boolean updated = false;

        if (dto.name().isPresent()) {
            String newName = dto.name().get();
            if (newName == null || newName.isBlank()) {
                throw new BadRequestException("Director name must not be blank");
            }
            if (!newName.equals(existingDirector.getName())) {
                if (directorRepository.existsByName(newName)) {
                    throw new DuplicateEntryException("name", newName);
                }
                existingDirector.setName(newName);
                updated = true;
            }
        }

        if (dto.countryId().isPresent()) {
            Long newCountryId = dto.countryId().get();
            if (newCountryId == null) {
                throw new BadRequestException("Country ID must not be null");
            }
            if (newCountryId <= 0) {
                throw new BadRequestException("Country ID must be a positive number");
            }
            Country newCountry = countryRepository.findById(newCountryId)
                .orElseThrow(() -> new ResourceNotFoundException(Country.class.getSimpleName(), newCountryId));
            existingDirector.setCountry(newCountry);
            updated = true;
        }

        if (updated) {
            Director updatedDirector = directorRepository.save(existingDirector);
            return directorMapper.toDto(updatedDirector);
        }

        return directorMapper.toDto(existingDirector);
    }

    public void deleteDirector(Long id) {
        if (!directorRepository.existsById(id)) {
            throw new ResourceNotFoundException(Director.class.getSimpleName(), id);
        }
        directorRepository.deleteById(id);
    }

}
