package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.movie.director.Director;
import dev.steelbookdb.steelbookapi.movie.movie.Movie;
import dev.steelbookdb.steelbookapi.movie.movie.MovieMapper;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.steelbook.disk.Disk;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskDto;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskMapper;
import dev.steelbookdb.steelbookapi.steelbook.editor.Editor;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorDto;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorMapper;
import dev.steelbookdb.steelbookapi.steelbook.retailer.Retailer;
import dev.steelbookdb.steelbookapi.steelbook.retailer.RetailerDto;
import dev.steelbookdb.steelbookapi.steelbook.retailer.RetailerMapper;
import dev.steelbookdb.steelbookapi.steelbook.steelbook.Steelbook;
import dev.steelbookdb.steelbookapi.steelbook.steelbook.SteelbookDto;
import dev.steelbookdb.steelbookapi.steelbook.steelbook.SteelbookMapper;

@ExtendWith(MockitoExtension.class)
class SteelbookMapperTest {

    @Mock
    private MovieMapper movieMapper;
    @Mock
    private EditorMapper editorMapper;
    @Mock
    private RetailerMapper retailerMapper;
    @Mock
    private DiskMapper diskMapper;

    @InjectMocks
    private SteelbookMapper steelbookMapper;

    @Test
    void toDto_CorrectlyMaps_GivenSteelbookEntity() {
        Editor editor = new Editor();
        editor.setId(10L);

        Retailer retailer = new Retailer();
        retailer.setId(20L);

        Movie movie = new Movie();
        movie.setId(30L);
        movie.setDirector(new Director());

        Disk disk = new Disk();
        disk.setId(40L);

        Steelbook steelbook = new Steelbook();
        steelbook.setId(1L);
        steelbook.setReleaseDate(LocalDate.of(2025, 8, 8));
        steelbook.setEditor(editor);
        steelbook.setRetailers(Set.of(retailer));
        steelbook.setMovies(Set.of(movie));
        steelbook.setDisks(Set.of(disk));

        when(movieMapper.toDto(movie))
            .thenReturn(MovieDto.builder()
                .id(movie.getId())
                .title("The movie")
                .releaseYear(2025)
                .runtime(120)
                .posterUrl("https://example.com/poster.jpg")
                .build());
        when(editorMapper.toDto(editor))
            .thenReturn(EditorDto.builder()
                .id(editor.getId())
                .name("Test Editor")
                .build());
        when(retailerMapper.toDto(retailer))
            .thenReturn(RetailerDto.builder()
                .id(retailer.getId())
                .name("Test Retailer")
                .website("https://example.com/poster.jpg")
                .build());

        when(diskMapper.toDto(disk))
            .thenReturn(DiskDto.builder()
                .id(disk.getId())
                .build());

        SteelbookDto dto = steelbookMapper.toDto(steelbook);

        assertNotNull(dto);
        assertEquals(steelbook.getId(), dto.id());
        assertEquals(steelbook.getId(), dto.id());
        assertEquals(1, dto.movies().size());
        assertEquals(movie.getId(), dto.movies().iterator().next().id());
        assertEquals(editor.getId(), dto.editor().id());
        assertEquals(1, dto.retailers().size());
        assertEquals(retailer.getId(), dto.retailers().iterator().next().id());
        assertEquals(1, dto.disks().size());
        assertEquals(disk.getId(), dto.disks().iterator().next().id());
    }

    @Test
    void toDto_ReturnsNull_GivenNullEntity() {
        SteelbookDto dto = steelbookMapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void toDto_ReturnsEmptySets_GivenEmptyOrNullCollections() {
        Steelbook steelbook = new Steelbook();
        steelbook.setId(1L);
        steelbook.setMovies(Collections.emptySet());
        steelbook.setRetailers(Collections.emptySet());
        steelbook.setDisks(null);
        
        SteelbookDto dto = steelbookMapper.toDto(steelbook);

        assertNotNull(dto);
        assertNotNull(dto.movies());
        assert dto.movies().isEmpty();
        assertNotNull(dto.retailers());
        assert dto.retailers().isEmpty();
        assertNotNull(dto.disks());
        assert dto.disks().isEmpty();
    }
}
