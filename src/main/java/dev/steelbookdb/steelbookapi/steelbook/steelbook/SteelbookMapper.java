package dev.steelbookdb.steelbookapi.steelbook.steelbook;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.movie.movie.MovieMapper;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskMapper;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorMapper;
import dev.steelbookdb.steelbookapi.steelbook.retailer.RetailerMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SteelbookMapper {

    private final MovieMapper movieMapper;
    private final EditorMapper editorMapper;
    private final RetailerMapper retailerMapper;
    private final DiskMapper diskMapper;

    public SteelbookDto toDto(Steelbook steelbook) {
        if (steelbook == null) return null;

        return new SteelbookDto(
            steelbook.getId(),
            steelbook.getReleaseDate(),
            steelbook.getMovies() != null ? steelbook.getMovies()
                .stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet(),
            editorMapper.toDto(steelbook.getEditor()),
            steelbook.getRetailers() != null ? steelbook.getRetailers()
                .stream()
                .map(retailerMapper::toDto)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet(),
            steelbook.getDisks() != null ? steelbook.getDisks()
                .stream()
                .map(diskMapper::toDto)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet()
        );
    }
}
