package dev.steelbookdb.steelbookapi.movie.genre;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.movie.genre.dto.CreateGenreDto;
import dev.steelbookdb.steelbookapi.movie.genre.dto.GenreDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

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
    
}
