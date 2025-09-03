package dev.steelbookdb.steelbookapi.movie.movie.dto;

import java.util.Set;

import org.openapitools.jackson.nullable.JsonNullable;

import lombok.Builder;

@Builder
public record UpdateMovieDto(
    JsonNullable<String> title,
    JsonNullable<Integer> releaseYear,
    JsonNullable<Integer> runtime,
    JsonNullable<String> posterUrl,
    JsonNullable<Long> directorId,
    JsonNullable<Long> languageId,
    JsonNullable<Set<Long>> genreIds
) {

}
