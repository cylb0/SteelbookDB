package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.language.Language;
import dev.steelbookdb.steelbookapi.localization.language.LanguageMapper;
import dev.steelbookdb.steelbookapi.localization.language.dto.LanguageDto;
import dev.steelbookdb.steelbookapi.movie.movie.Movie;
import dev.steelbookdb.steelbookapi.movie.movie.MovieMapper;
import dev.steelbookdb.steelbookapi.movie.movie.dto.MovieDto;
import dev.steelbookdb.steelbookapi.steelbook.audiotrack.AudioTrack;
import dev.steelbookdb.steelbookapi.steelbook.audiotrack.AudioTrackDto;
import dev.steelbookdb.steelbookapi.steelbook.audiotrack.AudioTrackMapper;
import dev.steelbookdb.steelbookapi.steelbook.disk.BlurayRegion;
import dev.steelbookdb.steelbookapi.steelbook.disk.Disk;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskDto;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskFormat;
import dev.steelbookdb.steelbookapi.steelbook.disk.DiskMapper;

@ExtendWith(MockitoExtension.class)
class DiskMapperTest {

    @Mock
    private AudioTrackMapper audioTrackMapper;
    @Mock
    private LanguageMapper languageMapper;
    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private DiskMapper diskMapper;

    @Test
    void toDto_CorrectlyMaps_GivenDiskEntity() {
        Movie movie = new Movie();
        movie.setId(10L);

        AudioTrack audioTrack = new AudioTrack();
        audioTrack.setId(20L);

        Language subtitleLanguage = new Language();
        subtitleLanguage.setId(30L);

        Disk disk = new Disk();
        disk.setId(1L);
        disk.setFormat(DiskFormat.BLU_RAY_DISC);
        disk.setRegion(BlurayRegion.REGION_A.name());
        disk.setBonusDisk(true);
        disk.setMovie(movie);
        disk.setAudioTracks(Set.of(audioTrack));
        disk.setSubtitleLanguages(Set.of(subtitleLanguage));

        when(movieMapper.toDto(movie)).thenReturn(MovieDto.builder()
                .id(movie.getId())
                .title("The movie")
                .releaseYear(2025)
                .runtime(120)
                .posterUrl("https://example.com/poster.jpg")
                .build());
        when(audioTrackMapper.toDto(audioTrack)).thenReturn(AudioTrackDto.builder()
                .id(audioTrack.getId())
                .audioFormat(null)
                .language(null)
                .build());
        when(languageMapper.toDto(subtitleLanguage)).thenReturn(LanguageDto.builder()
                .id(subtitleLanguage.getId())
                .code("en")
                .name("English")
                .build());

        DiskDto dto = diskMapper.toDto(disk);

        assertNotNull(dto);
        assertEquals(disk.getId(), dto.id());
        assertEquals(disk.getFormat().name(), dto.format());
        assertEquals(disk.getRegion(), dto.region());
        assertTrue(dto.isBonusDisk());

        assertEquals(dto.movie().id(), movie.getId());

        assertEquals(1, dto.audioTracks().size());
        assertEquals(audioTrack.getId(), dto.audioTracks().iterator().next().id());

        assertEquals(1, dto.subtitleLanguages().size());
        assertEquals(subtitleLanguage.getId(), dto.subtitleLanguages().iterator().next().id());
    }

    @Test
    void toDto_ReturnsNull_WhenDiskIsNull() {
        DiskDto dto = diskMapper.toDto(null);
        assertEquals(null, dto);
    }

    @Test
    void toDto_ReturnsEmptySets_WhenDiskHasNoAudioTracksOrSubtitleLanguages() {
        Disk disk = new Disk();
        disk.setFormat(DiskFormat.BLU_RAY_DISC);
        disk.setAudioTracks(Set.of());

        DiskDto dto = diskMapper.toDto(disk);

        assertNotNull(dto);
        assertNotNull(dto.audioTracks());
        assert dto.audioTracks().isEmpty();
        assertNotNull(dto.subtitleLanguages());
        assert dto.subtitleLanguages().isEmpty();
    }
}
