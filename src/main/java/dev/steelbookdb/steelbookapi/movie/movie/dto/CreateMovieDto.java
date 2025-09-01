package dev.steelbookdb.steelbookapi.movie.movie.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateMovieDto(
    @NotBlank(message = "Title must not be blank")
    String title,

    @NotNull(message = "Release year cannot be null")
    @Positive(message = "Release year must be a positive number")
    Integer releaseYear,

    @NotNull(message = "Runtime cannot be null")
    @Positive(message = "Runtime must be a positive number")
    Integer runtime,

    String posterUrl,

    @NotNull(message = "Director ID cannot be null")
    @Positive(message = "Director ID must be a positive number")
    Long directorId,

    @NotNull(message = "Original language ID cannot be null")
    @Positive(message = "Original language ID must be a positive number")
    Long originalLanguageId,

    @NotNull(message = "Genre IDs cannot be null")
    @Size(min = 1, message = "At least one genre ID must be provided")
    Set<@Positive(message = "Genre ID must be a positive number") Long> genreIds
) {

}
