package dev.steelbookdb.steelbookapi.movie;

import org.springframework.stereotype.Service;

@Service
public class DirectorMapper {

    public DirectorDto toDto(Director director) {
        if (director == null) return null;

        return new DirectorDto(
            director.getId(),
            director.getName()
        );
    }
}
