package dev.steelbookdb.steelbookapi.movie.genre;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.UpdateGenreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping("/genres")
    public GenreDto saveGenre(
        @Valid @RequestBody CreateGenreDto dto
    ) {
        return genreService.createGenre(dto);
    }

    @GetMapping("/genres")
    public List<GenreDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public GenreDto getGenreById(
        @PathVariable Long id
    ) {
        return genreService.getGenreById(id);
    }

    @PatchMapping("/genres/{id}")
    public GenreDto updateGenre(
        @PathVariable Long id,
        @Valid @RequestBody UpdateGenreDto dto
    ) {
        return genreService.updateGenre(id, dto);
    }

    @DeleteMapping("/genres/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }

}
