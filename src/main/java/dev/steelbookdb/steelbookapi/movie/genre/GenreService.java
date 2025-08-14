package dev.steelbookdb.steelbookapi.movie.genre;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.exception.ConflictException;
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.UpdateGenreDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

    public GenreDto createGenre(CreateGenreDto dto) {
        if (dto == null) return null;

        if (genreRepository.existsByName(dto.name())) {
            throw new DuplicateEntryException("name", dto.name());
        }
        
        Genre genre = genreMapper.toEntity(dto);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll()
            .stream()
            .map(genreMapper::toDto)
            .toList();
    }

    public GenreDto getGenreById(Long id) {
        return genreRepository.findById(id)
            .map(genreMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
    }

    public GenreDto updateGenre(Long id, UpdateGenreDto dto) {
        Genre existingGenre = genreRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(Genre.class.getSimpleName(), id));

        AtomicBoolean isUpdated = new AtomicBoolean(false);

        dto.name().ifPresent(newName -> {
            if (newName.isBlank()) {
                throw new ConflictException("Name cannot be blank.");
            }
            if (!newName.equals(existingGenre.getName()) && genreRepository.existsByName(newName)) {
                throw new DuplicateEntryException("name", newName);
            }
            existingGenre.setName(newName);
            isUpdated.set(true);
        });

        if (isUpdated.get()) {
            Genre updatedGenre = genreRepository.save(existingGenre);
            return genreMapper.toDto(updatedGenre);
        }

        return genreMapper.toDto(existingGenre);
    }

    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException(Genre.class.getSimpleName(), id);
        }
        genreRepository.deleteById(id);
    }
}
