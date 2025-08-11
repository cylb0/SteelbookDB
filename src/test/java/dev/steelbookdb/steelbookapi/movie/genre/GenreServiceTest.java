package dev.steelbookdb.steelbookapi.movie.genre;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.exception.DuplicateEntryException;
import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;

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
    
}
