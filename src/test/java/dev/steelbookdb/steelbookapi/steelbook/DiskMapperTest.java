package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.Language;
import dev.steelbookdb.steelbookapi.localization.LanguageDto;
import dev.steelbookdb.steelbookapi.localization.LanguageMapper;
import dev.steelbookdb.steelbookapi.movie.Movie;
import dev.steelbookdb.steelbookapi.movie.MovieDto;
import dev.steelbookdb.steelbookapi.movie.MovieMapper;

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

        when(movieMapper.toDto(movie)).thenReturn(new MovieDto(movie.getId(), "The Movie", 2025, 120, null, null, null, null));
        when(audioTrackMapper.toDto(audioTrack)).thenReturn(new AudioTrackDto(audioTrack.getId(), null, null));
        when(languageMapper.toDto(subtitleLanguage)).thenReturn(new LanguageDto(subtitleLanguage.getId(), "en", "English"));

        DiskDto dto = diskMapper.toDto(disk);

        assertNotNull(dto);
        assert disk.getId().equals(dto.id());
        assert disk.getFormat().name().equals(dto.format());
        assert disk.getRegion().equals(dto.region());
        assertTrue(dto.isBonusDisk());
        
        assert dto.movie().id().equals(movie.getId());

        assert dto.audioTracks().size() == 1;
        assert dto.audioTracks().iterator().next().id().equals(audioTrack.getId());

        assert dto.subtitleLanguages().size() == 1;
        assert dto.subtitleLanguages().iterator().next().id().equals(subtitleLanguage.getId());
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
