package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormat;
import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormatDto;
import dev.steelbookdb.steelbookapi.steelbook.audioformat.AudioFormatMapper;

class AudioFormatMapperTest {

    private AudioFormatMapper audioFormatMapper = new AudioFormatMapper();

    @Test
    void toDto_CorrectlyMaps_GivenAudioFormatEntity() {
        AudioFormat audioFormat = new AudioFormat();
        audioFormat.setId(1L);
        audioFormat.setName("Dolby Atmos");

        AudioFormatDto dto = audioFormatMapper.toDto(audioFormat);

        assertNotNull(dto);
        assertEquals(audioFormat.getId(), dto.id());
        assertEquals(audioFormat.getName(), dto.name());
    }

    @Test
    void toDto_ReturnsNull_GivenNullAudioFormat() {
        AudioFormatDto dto = audioFormatMapper.toDto(null);
        assertEquals(null, dto);
    }
}
