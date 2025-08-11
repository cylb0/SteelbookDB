package dev.steelbookdb.steelbookapi.movie.director;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryRepository;
import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
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

}
