package dev.steelbookdb.steelbookapi.steelbook;

import org.springframework.stereotype.Service;

@Service
public class AudioFormatMapper {

    public AudioFormatDto toDto(AudioFormat audioFormat) {
        if (audioFormat == null) return null;
        return new AudioFormatDto(audioFormat.getId(), audioFormat.getName());
    }
}
