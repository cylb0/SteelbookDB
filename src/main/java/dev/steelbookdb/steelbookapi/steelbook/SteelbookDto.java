package dev.steelbookdb.steelbookapi.steelbook;

import java.time.LocalDate;
import java.util.Set;

import dev.steelbookdb.steelbookapi.movie.MovieDto;

public record SteelbookDto(
    Long id,
    LocalDate releaseDate,
    Set<MovieDto> movies,
    EditorDto editor,
    Set<RetailerDto> retailers,
    Set<DiskDto> disks
) {

}
