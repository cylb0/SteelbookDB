package dev.steelbookdb.steelbookapi.movie.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.exception.ResourceNotFoundException;
import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.UpdateGenreDto;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    @Test
    void createGenre_returnsGenreDto_givenValidCreateGenreDto() {
        CreateGenreDto createGenreDto = new CreateGenreDto("action");
        Genre genre = Genre.builder().id(1L).name(createGenreDto.name()).build();

        GenreDto expectedDto = new GenreDto(genre.getId(), genre.getName());

        when(genreMapper.toEntity(createGenreDto)).thenReturn(genre);
        when(genreRepository.save(any(Genre.class))).thenReturn(genre);
        when(genreMapper.toDto(genre)).thenReturn(expectedDto);

        GenreDto result = genreService.createGenre(createGenreDto);

        assertEquals(expectedDto, result);

        verify(genreMapper, times(1)).toEntity(createGenreDto);
        verify(genreRepository, times(1)).save(genre);
        verify(genreMapper, times(1)).toDto(genre);
    }

    @Test
    void createGenre_returnsNull_givenNullCreateGenreDto() {
        GenreDto result = genreService.createGenre(null);
        assertNull(result);
    }

    @Test
    void createGenre_throwsDuplicateEntryException_givenExistingGenreName() {
        String newName = "action";
        CreateGenreDto createGenreDto = new CreateGenreDto("action");

        when(genreRepository.existsByName(newName)).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> {
            genreService.createGenre(createGenreDto);
        });

        verify(genreRepository, times(1)).existsByName(newName);
        verifyNoInteractions(genreMapper);
        verify(genreRepository, never()).save(any(Genre.class));
    }
    
    @Test
    void getAllGenres_returnsGenreDtoList_whenGenresExist() {
        Genre genre = Genre.builder()
            .id(1L)
            .name("Action")
            .build();

        List<Genre> genres = List.of(genre);
        GenreDto genreDto = new GenreDto(genre.getId(), genre.getName());

        when(genreRepository.findAll()).thenReturn(genres);
        when(genreMapper.toDto(any(Genre.class))).thenReturn(genreDto);

        List<GenreDto> result = genreService.getAllGenres();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(1)).toDto(genre);
    }

    @Test
    void getAllGenres_returnsEmptyList_whenNoGenresExist() {
        when(genreRepository.findAll()).thenReturn(Collections.emptyList());

        List<GenreDto> result = genreService.getAllGenres();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(genreRepository, times(1)).findAll();
        verifyNoInteractions(genreMapper);
    }

    @Test
    void getGenreById_returnsGenreDto_whenGenreExists() {
        Long genreId = 1L;
        GenreDto expectedDto = new GenreDto(genreId, "Action");

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(new Genre()));
        when(genreMapper.toDto(any(Genre.class))).thenReturn(expectedDto);

        GenreDto result = genreService.getGenreById(genreId);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(genreRepository, times(1)).findById(genreId);
        verify(genreMapper, times(1)).toDto(any(Genre.class));
    }

    @Test
    void getGenreById_throwsResourceNotFoundException_whenGenreDoesNotExist() {
        Long genreId = 1L;

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.getGenreById(genreId);
        });

        verify(genreRepository, times(1)).findById(genreId);
        verifyNoInteractions(genreMapper);
    }

    @Test
    void updateGenre_updatesName_whenNameIsProvided() {
        Long genreId = 1L;
        String newName = "Adventure";
        Genre existingGenre = Genre.builder().id(genreId).name("Action").build();
        UpdateGenreDto updateDto = new UpdateGenreDto(Optional.of(newName));
        GenreDto expectedDto = new GenreDto(existingGenre.getId(), newName);

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.existsByName(newName)).thenReturn(false);
        when(genreRepository.save(existingGenre)).thenReturn(Genre.builder()
            .id(existingGenre.getId())
            .name(newName)
            .build());
        when(genreMapper.toDto(any(Genre.class))).thenReturn(expectedDto);

        GenreDto result = genreService.updateGenre(genreId, updateDto);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        
        verify(genreRepository, times(1)).findById(genreId);
        verify(genreRepository, times(1)).existsByName(newName);
        verify(genreRepository, times(1)).save(existingGenre);
        verify(genreMapper, times(1)).toDto(existingGenre);
    }

    @Test
    void updateGenre_throwsResourceNotFoundException_whenGenreDoesNotExist() {
        Long genreId = 1L;
        UpdateGenreDto updateDto = new UpdateGenreDto(Optional.of("New Name"));

        when(genreRepository.findById(genreId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.updateGenre(genreId, updateDto);
        });

        verify(genreRepository, times(1)).findById(genreId);
        verifyNoInteractions(genreMapper);
    }

    @Test
    void updateGenre_shouldNotUpdateAnything_whenDtoIsEmpty() {
        Long genreId = 1L;
        Genre existingGenre = Genre.builder().id(genreId).name("Action").build();
        UpdateGenreDto updateDto = new UpdateGenreDto(Optional.empty());

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreMapper.toDto(existingGenre)).thenReturn(new GenreDto(existingGenre.getId(), existingGenre.getName()));

        GenreDto result = genreService.updateGenre(genreId, updateDto);

        assertNotNull(result);
        assertEquals(new GenreDto(existingGenre.getId(), existingGenre.getName()), result);
        verify(genreRepository, times(1)).findById(genreId);
        verify(genreRepository, never()).save(any(Genre.class));
        verifyNoInteractions(genreRepository.existsByName(any()));
    }

    @Test
    void updateGenre_throwsDuplicateEntryException_whenGenreNameAlreadyExists() {
        Long genreId = 1L;
        String newName = "Adventure";
        Genre existingGenre = Genre.builder().id(genreId).name("Action").build();
        UpdateGenreDto updateDto = new UpdateGenreDto(Optional.of(newName));

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreRepository.existsByName(newName)).thenReturn(true);

        assertThrows(DuplicateEntryException.class, () -> {
            genreService.updateGenre(genreId, updateDto);
        });

        verify(genreRepository, times(1)).findById(genreId);
        verify(genreRepository, times(1)).existsByName(newName);
        verifyNoInteractions(genreMapper);
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void updateGenre_throwsConflictException_whenNameIsBlank() {
        Long genreId = 1L;
        UpdateGenreDto updateDto = new UpdateGenreDto(Optional.of(""));

        when(genreRepository.findById(genreId)).thenReturn(Optional.of(Genre.builder().id(genreId).name("Action").build()));

        assertThrows(ConflictException.class, () -> {
            genreService.updateGenre(genreId, updateDto);
        });

        verify(genreRepository, times(1)).findById(genreId);
        verifyNoInteractions(genreMapper);
        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void deleteGenre_removesGenre_whenExists() {
        Long genreId = 1L;

        when(genreRepository.existsById(genreId)).thenReturn(true);

        genreService.deleteGenre(genreId);

        verify(genreRepository, times(1)).existsById(genreId);
        verify(genreRepository, times(1)).deleteById(genreId);
    }

    @Test
    void deleteGenre_throwsResourceNotFoundException_whenGenreDoesNotExist() {
        Long genreId = 1L;

        when(genreRepository.existsById(genreId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->  {
            genreService.deleteGenre(genreId);
        });

        verify(genreRepository, times(1)).existsById(genreId);
        verify(genreRepository, never()).deleteById(anyLong());
    }
}
