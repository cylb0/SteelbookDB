package dev.steelbookdb.steelbookapi.steelbook.steelbook;

import java.time.LocalDate;
import java.util.Set;

import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskDto;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorDto;
import dev.steelbookdb.steelbookapi.steelbook.retailer.RetailerDto;

public record SteelbookDto(
    Long id,
    LocalDate releaseDate,
    Set<MovieDto> movies,
    EditorDto editor,
    Set<RetailerDto> retailers,
    Set<DiskDto> disks
) {

}
