package dev.steelbookdb.steelbookapi.movie;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreMapper genreMapper;
    private final GenreRepository genreRepository;

}
