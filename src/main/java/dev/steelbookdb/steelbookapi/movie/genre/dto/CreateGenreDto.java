package dev.steelbookdb.steelbookapi.movie.genre.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateGenreDto(
    @NotBlank(message = "Genre name must not be blank")
    String name
) {

}
