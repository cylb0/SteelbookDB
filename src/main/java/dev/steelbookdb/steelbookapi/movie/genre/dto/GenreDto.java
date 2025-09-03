package dev.steelbookdb.steelbookapi.movie.genre.dto;

import lombok.Builder;

@Builder
public record GenreDto(
    Long id,
    String name
) {

}
