package dev.steelbookdb.steelbookapi.steelbook.editor;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditorService {

    private final EditorMapper editorMapper;
    private final EditorRepository editorRepository;

}
