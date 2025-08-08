package dev.steelbookdb.steelbookapi.steelbook;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AudioFormatService {
    
    private final AudioFormatMapper audioFormatMapper;
    private final AudioFormatRepository audioFormatRepository;

}
