package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.steelbookdb.steelbookapi.localization.country.Country;
import dev.steelbookdb.steelbookapi.localization.country.CountryDto;
import dev.steelbookdb.steelbookapi.localization.country.CountryMapper;
import dev.steelbookdb.steelbookapi.steelbook.editor.Editor;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorDto;
import dev.steelbookdb.steelbookapi.steelbook.editor.EditorMapper;

@ExtendWith(MockitoExtension.class)
class EditorMapperTest {

    @Mock
    private CountryMapper countryMapper;

    @InjectMocks
    private EditorMapper editorMapper;

    @Test
    void toDto_CorrectlyMaps_GivenEditorEntity() {
        Country country = new Country();
        country.setId(10L);
        country.setName("France");

        Editor editor = new Editor("The Editor", "http://example.com", country, null);
        editor.setId(1L);

        when(countryMapper.toDto(country))
            .thenReturn(new CountryDto(country.getId(), country.getName()));

        EditorDto dto = editorMapper.toDto(editor);

        assertNotNull(dto);
        assert editor.getId().equals(dto.id());
        assert editor.getName().equals(dto.name());
        assert editor.getWebsite().equals(dto.website());

        assert dto.country().id().equals(country.getId());

    }

    @Test
    void toDto_ReturnsNull_WhenEditorIsNull() {
        EditorDto dto = editorMapper.toDto(null);
        assertNull(dto);
    }
}
