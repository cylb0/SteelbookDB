package dev.steelbookdb.steelbookapi.steelbook;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.LanguageMapper;
import dev.steelbookdb.steelbookapi.movie.MovieMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiskMapper {

    private final AudioTrackMapper audioTrackMapper;
    private final LanguageMapper languageMapper;
    private final MovieMapper movieMapper;

    public DiskDto toDto(Disk disk) {
        if (disk == null) return null;

        return new DiskDto(
            disk.getId(),
            disk.getFormat().name(),
            disk.getRegion(),
            disk.isBonusDisk(),
            disk.getMovie() != null ? movieMapper.toDto(disk.getMovie()) : null,
            disk.getAudioTracks() != null ? disk.getAudioTracks()
                .stream()
                .map(audioTrackMapper::toDto)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet(),
            disk.getSubtitleLanguages() != null ? disk.getSubtitleLanguages()
                .stream()
                .map(languageMapper::toDto)
                .collect(Collectors.toSet()) : java.util.Collections.emptySet()
        );
    }
}
