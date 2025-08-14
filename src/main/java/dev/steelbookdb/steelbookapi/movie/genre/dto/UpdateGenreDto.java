package dev.steelbookdb.steelbookapi.movie.genre.dto;

import java.util.Optional;

public record UpdateGenreDto(
    Optional<String> name
) {

}
