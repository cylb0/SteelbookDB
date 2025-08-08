package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.Language;
import dev.steelbookdb.steelbookapi.localization.LanguageDto;
import dev.steelbookdb.steelbookapi.localization.LanguageMapper;

@ExtendWith(MockitoExtension.class)
class AudioTrackMapperTest {

    @Mock
    private AudioFormatMapper audioFormatMapper;
    @Mock
    private LanguageMapper languageMapper;

    @InjectMocks
    private AudioTrackMapper audioTrackMapper;

    @Test
    void toDto_CorrectlyMaps_GivenAudioTrackEntity() {
        Language language = new Language();
        language.setId(1L);
        language.setCode("en");
        language.setName("English");

        AudioFormat audioFormat = new AudioFormat();
        audioFormat.setId(1L);
        audioFormat.setName("Stereo");

        AudioTrack audioTrack = new AudioTrack();
        audioTrack.setId(1L);
        audioTrack.setAudioFormat(audioFormat);
        audioTrack.setLanguage(language);
        
        when(audioFormatMapper.toDto(audioFormat))
            .thenReturn(new AudioFormatDto(audioFormat.getId(), audioFormat.getName()));
        when(languageMapper.toDto(language))
            .thenReturn(new LanguageDto(language.getId(), language.getCode(), language.getName()));

        AudioTrackDto dto = audioTrackMapper.toDto(audioTrack);
        
        assertNotNull(dto);
        assert dto.id() == audioTrack.getId();
        assert dto.audioFormat().id() == audioFormat.getId();
        assert dto.audioFormat().name().equals(audioFormat.getName());

        assert dto.language().id() == language.getId();
        assert dto.language().code().equals(language.getCode());
        assert dto.language().name().equals(language.getName());
    }

    @Test
    void toDto_ReturnsNull_WhenAudioTrackIsNull() {
        AudioTrackDto dto = audioTrackMapper.toDto(null);
        assertNull(dto);
    }   
}
