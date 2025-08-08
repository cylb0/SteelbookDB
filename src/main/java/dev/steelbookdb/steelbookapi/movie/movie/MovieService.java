package dev.steelbookdb.steelbookapi.movie.movie;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

}
