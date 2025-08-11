package dev.steelbookdb.steelbookapi.movie.director;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping("/directors")
    public DirectorDto saveDirector(@Valid @RequestBody CreateDirectorDto dto) {
        return directorService.createDirector(dto);
    }

    @GetMapping("/directors")
    public List<DirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/directors/{id}")
    public DirectorDto getDirectorById(@PathVariable Long id) {
        return directorService.getDirectorById(id);
    }
    
}
