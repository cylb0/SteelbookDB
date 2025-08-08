package dev.steelbookdb.steelbookapi.movie.movietranslation;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieTranslationService {

    private final MovieTranslationMapper movieTranslationMapper;
    private final MovieTranslationRepository movieTranslationRepository;

}
