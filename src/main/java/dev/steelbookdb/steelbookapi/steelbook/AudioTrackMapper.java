package dev.steelbookdb.steelbookapi.steelbook;

import org.springframework.stereotype.Service;

import dev.steelbookdb.steelbookapi.localization.LanguageMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AudioTrackMapper {

    private final AudioFormatMapper audioFormatMapper;
    private final LanguageMapper languageMapper;

    public AudioTrackDto toDto(AudioTrack audioTrack) {
        if (audioTrack == null) return null;

        return new AudioTrackDto(
            audioTrack.getId(),
            audioFormatMapper.toDto(audioTrack.getAudioFormat()),
            languageMapper.toDto(audioTrack.getLanguage())
        );
    }
}
